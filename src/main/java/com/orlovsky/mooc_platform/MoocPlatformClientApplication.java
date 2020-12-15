package com.orlovsky.mooc_platform;

import com.orlovsky.mooc_platform.config.RabbitMqConfig;
import com.orlovsky.mooc_platform.dto.*;
import com.orlovsky.mooc_platform.grpc.*;
import com.orlovsky.mooc_platform.model.Author;
import com.orlovsky.mooc_platform.model.Course;
import com.orlovsky.mooc_platform.model.EducationalStep;
import com.orlovsky.mooc_platform.model.Student;
import com.orlovsky.mooc_platform.model.TestStep;
import com.orlovsky.mooc_platform.model.TestStepOption;
import com.orlovsky.mooc_platform.producer.RabbitMqSender;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class MoocPlatformClientApplication {
    private static final String SPLITTER = "----------------------------------------------------------------------------------------";
    private static final String NLP_URI = "https://www.youtube.com/watch?v=8rXD5-xhemo&list=PLoROMvodv4rOhcuXMZkNm7j3fVwBBY42z&ab_channel=stanfordonline";
    private static final String URL = "http://localhost:8080";
    private static final RestTemplate restTemplate  = new RestTemplate();
    private static final HttpHeaders httpHeaders  = new HttpHeaders();

    private static final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 10000)
            .usePlaintext()
            .build();
    private static final AccountServiceGrpc.AccountServiceBlockingStub accountServiceBlockingStub =
            AccountServiceGrpc.newBlockingStub(channel);
    private static final EducationalMaterialServiceGrpc.EducationalMaterialServiceBlockingStub educationalMaterialServiceBlockingStub =
            EducationalMaterialServiceGrpc.newBlockingStub(channel);
    private static final CourseProgressServiceGrpc.CourseProgressServiceBlockingStub courseProgressServiceBlockingStub =
            CourseProgressServiceGrpc.newBlockingStub(channel);

    @Autowired
    private static RabbitMqConfig rabbitmqConfig;

    @Autowired
    private static RabbitMqSender rabbitMQSender;


    @Autowired
    private static AmqpTemplate template;

    public static void main(String[] args) throws Exception {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //----------------------------------------------------------------------------------
        //----------------- -----------------REST CLIENT ----------------- -----------------
        // ----------------- Create entities -----------------
        Course course = createCourse("DataBases introduction",
                "SQL,NoSQL, PostgreSQL,MySQL etc",
                100,
                40);
        Student student = createStudent("Thomas","Anderson","Matrix");
        Author author = createAuthor("Andrew", "Ng","Stanford");

        // ----------------- Filling the course -----------------
        addAuthorToCourse(course.getId(),author.getId());
        printSplitter();

        EducationalStep educationalStep = addEducationalStep(course.getId(),URI.create(NLP_URI))
                .orElseThrow( ()-> new HttpClientErrorException(HttpStatus.NOT_MODIFIED));
        printSplitter();

        TestStep testStep = addTestStep(course.getId(),URI.create(NLP_URI),1)
                .orElseThrow( ()-> new HttpClientErrorException(HttpStatus.NOT_MODIFIED));
        printSplitter();

        TestStepOption testStepOptionCorrect = addTestStepOption(course.getId(),
                testStep.getId(),
                "NLP",true)
                .orElseThrow( ()-> new HttpClientErrorException(HttpStatus.NOT_MODIFIED));
        TestStepOption testStepOptionIncorrect = addTestStepOption(course.getId(),
                testStep.getId(),
                "Stupid answer",false)
                .orElseThrow( ()-> new HttpClientErrorException(HttpStatus.NOT_MODIFIED));

        printSplitter();

        // ----------------- Progress in the course -----------------
        addStudentToCourse(course.getId(),student.getId());

        passEducationalStep(course.getId(),student.getId(),educationalStep.getId());

        passTestStep(course.getId(),student.getId(),
                testStep.getId(),mapStepOptionToDto(testStepOptionCorrect));


        // ----------------- Delete -----------------
        deleteCourse(course.getId());
        deleteAuthor(author.getId());
        deleteStudent(student.getId());

        //----------------------------------------------------------------------------------
        //----------------- -----------------GRPC CLIENT ----------------- -----------------
//      // ----------------- Create entities -----------------

        com.orlovsky.mooc_platform.grpc.Course grpcCourse = grpcCreateCourse("DataBases introduction",
                "SQL,NoSQL, PostgreSQL,MySQL etc",
                100,
                40);
        com.orlovsky.mooc_platform.grpc.Student grpcStudent = grpcCreateStudent("Thomas","Anderson","Matrix");
        com.orlovsky.mooc_platform.grpc.Author grpcAuthor = grpcCreateAuthor("Andrew", "Ng","Stanford");

        System.out.println(grpcAuthor.toString());
        System.out.println(grpcStudent.toString());
        System.out.println(grpcCourse.toString());
        printSplitter();

//        // ----------------- Filling the course -----------------
        grpcAddAuthor(grpcCourse.getId(),grpcAuthor.getId());

        com.orlovsky.mooc_platform.grpc.EducationalStep grpcEducationalStep =
                grpcAddEducationalStep(grpcCourse.getId(),NLP_URI);

        com.orlovsky.mooc_platform.grpc.TestStep grpcTestStep =
                grpcAddTestStep(grpcCourse.getId(),NLP_URI,1);

        com.orlovsky.mooc_platform.grpc.TestStepOption grpcTestStepOption =
                grpcAddTestStepOption(grpcCourse.getId(),grpcTestStep.getId(),"chair",false);

        printSplitter();

//        // ----------------- Progress in the course -----------------
        grpcSignUpUserOnCourse(grpcCourse.getId(),grpcStudent.getId());
        grpcMakePassedEducationalStep(grpcCourse.getId(),grpcEducationalStep.getId(),grpcStudent.getId());
        MakeProcessedTestStepResponse passedResponse =
                grpcMakeProcessedTestStep(grpcCourse.getId(),grpcTestStep.getId(),grpcStudent.getId(),grpcTestStepOption);

//        // ----------------- Delete -----------------
        educationalMaterialServiceBlockingStub.deleteCourse(
                DeleteCourseRequest.newBuilder()
                        .setCourseId(grpcCourse.getId())
                        .build());
        accountServiceBlockingStub.deleteAuthorById(grpcAuthor.getId());
        accountServiceBlockingStub.deleteStudentById(grpcStudent.getId());
        channel.shutdown();
        SpringApplication.run(MoocPlatformClientApplication.class, args);
}

//    @RabbitListener(queues = RabbitmqConfig.QUEUE_NAME)
//    public void listen(String in) {
//        System.out.println("Message read from" + RabbitmqConfig.QUEUE_NAME + " : "+ in);
//    }


    public static void printSplitter(){
        System.out.println(SPLITTER);
    }

//    -----------------GRPC-----------------
    public static com.orlovsky.mooc_platform.grpc.Course grpcCreateCourse(String title,
                                                                          String description,
                                                                          int price,
                                                                          int duration){
        return  educationalMaterialServiceBlockingStub.createEmptyCourse(
                CourseDto.newBuilder()
                        .setTitle(title)
                        .setPrice(price)
                        .setDuration(duration)
                        .setDescription(description)
                        .build()
        );
    }

    public static com.orlovsky.mooc_platform.grpc.Student grpcCreateStudent(String firstName,
                                                                            String lastName,
                                                                            String description){
        return accountServiceBlockingStub.signUpStudent(
                StudentDto.newBuilder()
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setDescription(description)
                        .build()
        );
    }

    public static com.orlovsky.mooc_platform.grpc.Author grpcCreateAuthor(String firstName,
                                                                          String lastName,
                                                                          String description){
        return accountServiceBlockingStub.signUpAuthor(
                AuthorDto.newBuilder()
                        .setFirstName(firstName)
                        .setLastName(lastName)
                        .setDescription(description)
                        .build()
        );
    }

    public static void grpcAddAuthor(com.orlovsky.mooc_platform.grpc.UUID courseId,
                                     com.orlovsky.mooc_platform.grpc.UUID authorId){
        educationalMaterialServiceBlockingStub.addAuthor(
                AddAuthorOnCourseRequest.newBuilder()
                        .setCourseId(courseId)
                        .setAuthorId(authorId).build());
    }

    public static com.orlovsky.mooc_platform.grpc.EducationalStep grpcAddEducationalStep(
            com.orlovsky.mooc_platform.grpc.UUID courseId,
            String educationalMaterialUri)
    {
        return educationalMaterialServiceBlockingStub.addEducationalStep(
                AddEducationalStepOnCourseRequest.newBuilder()
                        .setCourseId(courseId)
                        .setEducationalStepDto(
                                EducationalStepDto.newBuilder()
                                        .setCourseId(courseId)
                                        .setEduMaterialUri(educationalMaterialUri)
                                        .build()
                        ).build()
        );
    }

    public static com.orlovsky.mooc_platform.grpc.TestStep grpcAddTestStep(com.orlovsky.mooc_platform.grpc.UUID courseId,
                                                                           String descriptionUri,
                                                                           int score){
        return educationalMaterialServiceBlockingStub.addTestStep(
                AddTestStepOnCourseRequest.newBuilder()
                        .setCourseId(courseId)
                        .setTestStepDto(
                                TestStepDto.newBuilder()
                                        .setCourseId(courseId)
                                        .setDescriptionUri(descriptionUri)
                                        .setScore(score)
                                        .build()
                        ).build()
        );
    }

    public static com.orlovsky.mooc_platform.grpc.TestStepOption grpcAddTestStepOption(com.orlovsky.mooc_platform.grpc.UUID courseId,
                                                                                       com.orlovsky.mooc_platform.grpc.UUID testStepId,
                                                                                       String optionText,
                                                                                       Boolean isCorrect) {
         return educationalMaterialServiceBlockingStub.addTestStepOption(
                        AddTestStepOptionOnCourseRequest.newBuilder()
                                .setCourseId(courseId)
                                .setTestStepId(testStepId)
                                .setTestStepOptionRequestDto(
                                        TestStepOptionRequestDto.newBuilder()
                                                .setIsCorrect(isCorrect)
                                                .setOptionText(optionText).build()
                                ).build()
                );
    }

    public static void grpcSignUpUserOnCourse(com.orlovsky.mooc_platform.grpc.UUID courseId,
                                      com.orlovsky.mooc_platform.grpc.UUID studentId){
        courseProgressServiceBlockingStub.signUpUser(
                SignUpUserRequest.newBuilder()
                        .setCourseId(courseId)
                        .setStudentId(studentId).build());
    }

    public static void grpcMakePassedEducationalStep(com.orlovsky.mooc_platform.grpc.UUID courseId,
                                                 com.orlovsky.mooc_platform.grpc.UUID educationalStepId,
                                                 com.orlovsky.mooc_platform.grpc.UUID studentId){
        courseProgressServiceBlockingStub.makePassedEducationalStep(
                MakePassedEducationalStepRequest.newBuilder()
                        .setCourseId(courseId)
                        .setEducationalStepId(educationalStepId)
                        .setStudentId(studentId).build()
        );
    }
    public static MakeProcessedTestStepResponse  grpcMakeProcessedTestStep(com.orlovsky.mooc_platform.grpc.UUID courseId,
                                                                           com.orlovsky.mooc_platform.grpc.UUID testStepId,
                                                                           com.orlovsky.mooc_platform.grpc.UUID studentId,
                                                                           com.orlovsky.mooc_platform.grpc.TestStepOption
                                                                                   grpcTestStepOption){
         return courseProgressServiceBlockingStub.makeProcessedTestStep(
                MakeProcessedTestStepRequest.newBuilder()
                        .setCourseId(courseId)
                        .setTestStepId(testStepId)
                        .setStudentId(studentId)
                        .setChosenAnswer(
                                TestStepOptionDto.newBuilder()
                                        .setId(grpcTestStepOption.getId())
                                        .setOptionText(grpcTestStepOption.getOptionText())
                                        .build()
                        ).build()
        );
    }


//    REST API
    public static Course createCourse(String title,
                             String description,
                             int price,
                             int duration){
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle(title);
        courseDTO.setDescription(description);
        courseDTO.setPrice(price);
        courseDTO.setDuration(duration);
        HttpEntity<CourseDTO> entity = new HttpEntity<>(courseDTO);
        ResponseEntity<Course> responseEntity =  restTemplate
                .exchange(URL+"/courses",
                        HttpMethod.POST,
                        entity,
                        Course.class);
        return responseEntity.getBody();
    }


    public static Student createStudent(String firstName,
                                 String lastName,
                                 String description){
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName(firstName);
        studentDTO.setLastName(lastName);
        studentDTO.setDescription(description);
        HttpEntity<StudentDTO> entity = new HttpEntity<>(studentDTO);
        ResponseEntity<Student> responseEntity =  restTemplate
                .exchange(URL+"/students",
                        HttpMethod.POST,
                        entity,
                        Student.class);
        return responseEntity.getBody();
    }

    public static Author createAuthor(String firstName,
                               String lastName,
                               String description){
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName(firstName);
        authorDTO.setLastName(lastName);
        authorDTO.setDescription(description);
        HttpEntity<AuthorDTO> entity = new HttpEntity<>(authorDTO);
        ResponseEntity<Author> responseEntity =  restTemplate
                .exchange(URL+"/authors",
                        HttpMethod.POST,
                        entity,
                        Author.class);
        return responseEntity.getBody();
    }

    public static Optional<EducationalStep> addEducationalStep(UUID courseId, URI uri){
        EducationalStepDTO educationalStepDTO = new EducationalStepDTO();
        educationalStepDTO.setCourseId(courseId);
        educationalStepDTO.setEduMaterialUri(uri);

        HttpEntity<EducationalStepDTO> entity = new HttpEntity<>(educationalStepDTO);
        ResponseEntity<EducationalStep> responseEntity =  restTemplate
                .exchange(URL+"/courses/"+courseId.toString()+"/steps/educational-steps",
                        HttpMethod.POST,
                        entity,
                        EducationalStep.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Educational step has been added");
            return Optional.ofNullable(responseEntity.getBody());
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
        return Optional.empty();
    }

    public static Optional<TestStep> addTestStep(UUID courseId, URI descriptionUri, int score){
        TestStepDTO testStepDTO = TestStepDTO.builder()
                .courseId(courseId)
                .descriptionUri(descriptionUri)
                .score(score)
                .build();
        HttpEntity<TestStepDTO> entity = new HttpEntity<>(testStepDTO);
        ResponseEntity<TestStep> responseEntity =  restTemplate
                .exchange(URL+"/courses/"+courseId.toString()+"/steps/test-steps",
                        HttpMethod.POST,
                        entity,
                        TestStep.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Test step has been added");
            return Optional.ofNullable(responseEntity.getBody());
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
        return Optional.empty();
    }

    public static Optional<TestStepOption> addTestStepOption(UUID courseId,
                                  UUID testStepId,
                                  String optionText,
                                  Boolean isCorrect){
        TestStepOptionRequestDTO testStepOptionRequestDTO = new TestStepOptionRequestDTO();
        testStepOptionRequestDTO.setCorrect(isCorrect);
        testStepOptionRequestDTO.setOptionText(optionText);
        HttpEntity<TestStepOptionRequestDTO> entity = new HttpEntity<>(testStepOptionRequestDTO);
        String testStepOptionAddUri = URL+"/courses/"+
                courseId.toString()+"/steps/test-steps/"+
                testStepId.toString()+"/answers";
        ResponseEntity<TestStepOption> responseEntity
                = restTemplate.exchange(testStepOptionAddUri,
                                        HttpMethod.POST,
                                        entity,
                TestStepOption.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Test step option has been added");
            return Optional.ofNullable(responseEntity.getBody());
        } else {
            System.out.println("Something has gone wrong, http status: "+responseEntity.getStatusCode());
        }
        return Optional.empty();
    }

    public static void addAuthorToCourse(UUID courseId,UUID authorId){
        String addAuthorToCourseURI = URL+"/courses/"+
                courseId.toString()+"/authors/"+authorId.toString();
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(addAuthorToCourseURI,
                HttpMethod.POST,
                entity,
                Void.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Author has been added to the course");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static void addStudentToCourse(UUID courseId,UUID studentId){
        String addStudentToCourseURI = URL+"/progress/"+
                courseId.toString()+"/students/"+studentId.toString();
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(addStudentToCourseURI,
                HttpMethod.POST,
                entity,
                Void.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Student has been added to the course");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static void passEducationalStep(UUID courseId,
                                    UUID studentId,
                                    UUID educationalStepId){
        String passEducationalStepUri = URL+"/progress/"+
                courseId.toString()+"/students/"+studentId.toString()+
                "/steps/educational-steps/"+educationalStepId.toString();
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(passEducationalStepUri,
                HttpMethod.POST,
                entity,
                Void.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Student has passed the educational step");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static void passTestStep(UUID courseId,
                             UUID studentId,
                             UUID testStepId,
                             TestStepOptionDTO chosenAnswer){
        String passTestStepUri = URL+"/progress/"+
                courseId.toString()+"/students/"+studentId.toString()+
                "/steps/test-steps/"+testStepId.toString();
        HttpEntity<TestStepOptionDTO> entity = new HttpEntity<>(chosenAnswer);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(passTestStepUri,
                HttpMethod.POST,
                entity,
                Void.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("Student has tried to pass the test step");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static TestStepOptionDTO mapStepOptionToDto(TestStepOption testStepOption){
        return TestStepOptionDTO.builder()
                .optionText(testStepOption.getOptionText())
                .id(testStepOption.getId())
                .build();
    }


    public static void deleteCourse(UUID courseId){
        String deleteCourseUri = URL+"/courses/"+
                courseId.toString();
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(deleteCourseUri,
                HttpMethod.DELETE,
                entity,
                Void.class);
        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Course has been deleted");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static void deleteStudent(UUID studentId){
        String deleteStudentUri = URL+"/students/"+
                studentId.toString();
        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(deleteStudentUri,
                HttpMethod.DELETE,
                entity,
                Void.class);
        if(responseEntity.getStatusCode()== HttpStatus.OK) {
            System.out.println("Student has been deleted");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static void deleteAuthor(UUID authorId){
        String deleteAuthorUri = URL+"/authors/"+
                authorId.toString();
        HttpEntity<Void> entity = new HttpEntity<>(httpHeaders);
        ResponseEntity<Void> responseEntity
                = restTemplate.exchange(deleteAuthorUri,
                HttpMethod.DELETE,
                entity,
                Void.class);
        if(responseEntity.getStatusCode()== HttpStatus.OK) {
            System.out.println("Author has been deleted");
        } else {
            System.out.println("Something has gone wrong, http status:"+responseEntity.getStatusCode());
        }
    }

    public static void getAllCourses() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<List<CourseDTO>> entity = new HttpEntity<List<CourseDTO>>(headers);
        String getCoursesUrl = URL+"/courses";
        ResponseEntity<List> response
                = restTemplate.exchange(getCoursesUrl,
                HttpMethod.GET,entity,List.class);
        System.out.println(response);
    }



}

