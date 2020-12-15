package com.orlovsky.mooc_platform;


import com.orlovsky.mooc_platform.dto.*;
import com.orlovsky.mooc_platform.model.*;
import com.orlovsky.mooc_platform.producer.AccountProducer;
import com.orlovsky.mooc_platform.producer.CourseProgressProducer;
import com.orlovsky.mooc_platform.producer.EducationalMaterialProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Order(1)
@Slf4j
public class RabbitMqRunner implements CommandLineRunner {
    private static final String DB_URI = "https://www.youtube.com/watch?v=qw--VYLpxG4&t=435s&ab_channel=freeCodeCamp.org";
    @Autowired
    AccountProducer accountProducer;

    @Autowired
    CourseProgressProducer courseProgressProducer;

    @Autowired
    EducationalMaterialProducer educationalMaterialProducer;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Ready ... running ");

//      ---------- Create main entities ----------
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("Alexander");
        studentDTO.setLastName("Johansson");
        studentDTO.setDescription("CS student");
        Student student = accountProducer.sendCreateStudentMessage(studentDTO);

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName("Andrew");
        authorDTO.setLastName("Ng");
        authorDTO.setDescription("Stanford, Coursera, Baidu");
        Author author = accountProducer.sendCreateAuthorMessage(authorDTO);

        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setTitle("DataBases introduction");
        courseDTO.setDescription("SQL,NoSQL, PostgreSQL,MySQL etc");
        courseDTO.setPrice(100);
        courseDTO.setDuration(40);
        Course course = educationalMaterialProducer.sendCreateCourseMessage(courseDTO);

        //        ---------- Filling the course ----------
        EducationalStepDTO educationalStepDTO = new EducationalStepDTO();
        educationalStepDTO.setCourseId(course.getId());
        educationalStepDTO.setEduMaterialUri(URI.create(DB_URI));
        EducationalStep educationalStep =
                educationalMaterialProducer.sendCreateEducationalStepMessage(course.getId(),educationalStepDTO);
        TestStepDTO testStepDTO = new TestStepDTO();
        testStepDTO.setCourseId(course.getId());
        testStepDTO.setDescriptionUri(URI.create(DB_URI));
        testStepDTO.setScore(10);
        TestStep testStep =
                educationalMaterialProducer.sendCreateTestStepMessage(course.getId(),testStepDTO);

        TestStepOptionRequestDTO testStepOptionRequestDTO = new TestStepOptionRequestDTO();
        testStepOptionRequestDTO.setCorrect(false);
        testStepOptionRequestDTO.setOptionText("optionText");
        TestStepOption testStepOption = educationalMaterialProducer.sendCreateTestStepOptionMessage(course.getId(),
                testStep.getId(),testStepOptionRequestDTO);

        //        ---------- Progress in the course ----------
        courseProgressProducer.sendSignUpStudentMessage(course.getId(),student.getId());
        courseProgressProducer.sendMakePassedEducationalStepMessage(
                course.getId(),student.getId(),educationalStep.getId());
    }
}
