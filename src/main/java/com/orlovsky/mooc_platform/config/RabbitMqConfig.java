package com.orlovsky.mooc_platform.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan(basePackages = {"com.orlovsky.mooc_platform"})
//@EnableRabbit
public class RabbitMqConfig {
    /*
     * Connection configuration is in the end of the file
     * Queues, exchanges, bindings are grouped for better task checking
     * */
//    @Value("${spring.rabbitmq.host}")
    private String host = "localhost";
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.port}")
    private Integer port;

    public static final  String EXCHANGE_NAME = "mooc_platform_exchange";


    @Bean
    @Qualifier("exchange")
    public DirectExchange exchange(){
        return new DirectExchange(RabbitMqConfig.EXCHANGE_NAME);
    }


    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
}






//    @Bean
//    public Queue queueCreateAuthor(){
//        return new Queue(QUEUE_AUTHOR_CREATE_NAME);
//    }
//    @Bean
//    public Queue queueCreateCourse(){
//        return new Queue(QUEUE_COURSE_CREATE_NAME);
//    }
//    @Bean
//    public Queue queueCreateEducationalStep(){
//        return new Queue(QUEUE_EDUCATIONALSTEP_CREATE_NAME);
//    }
//    @Bean
//    public Queue queueCreateTestStep(){
//        return new Queue(QUEUE_TESTSTEP_CREATE_NAME);
//    }
//    @Bean
//    public Queue queueCreateTestStepOption(){
//        return new Queue(QUEUE_TESTSTEPOPTION_CREATE_NAME);
//    }
//
//    //    To client
//    @Bean
//    public Queue queueCreateStudentClient(){
//        return new Queue(QUEUE_STUDENT_CREATE_CLIENT_NAME);
//    }
//    @Bean
//    public Queue queueCreateAuthorClient(){
//        return new Queue(QUEUE_AUTHOR_CREATE_CLIENT_NAME);
//    }
//    @Bean
//    public Queue queueCreateCourseClient(){
//        return new Queue(QUEUE_COURSE_CREATE_CLIENT_NAME);
//    }
//    @Bean
//    public Queue queueCreateEducationalStepClient(){
//        return new Queue(QUEUE_EDUCATIONALSTEP_CREATE_CLIENT_NAME);
//    }
//    @Bean
//    public Queue queueCreateTestStepClient(){
//        return new Queue(QUEUE_TESTSTEP_CREATE_CLIENT_NAME);
//    }
//    @Bean
//    public Queue queueCreateTestStepOptionClient(){
//        return new Queue(QUEUE_TESTSTEPOPTION_CREATE_CLIENT_NAME);
//    }



//    @Bean
//    public TopicExchange exchangeAuthors(){
//        return new TopicExchange(EXCHANGE_AUTHORS);
//    }
//    @Bean
//    public TopicExchange exchangeCourses(){
//        return new TopicExchange(EXCHANGE_COURSES);
//    }
//    @Bean
//    public TopicExchange exchangeEducationalSteps(){
//        return new TopicExchange(EXCHANGE_EDUCATIONALSTEPS);
//    }
//    @Bean
//    public TopicExchange exchangeTestSteps(){
//        return new TopicExchange(EXCHANGE_TESTSTEPS);
//    }
//    @Bean
//    public TopicExchange exchangeTestStepOptions(){
//        return new TopicExchange(EXCHANGE_TESTSTEPSOPTIONS);
//    }
















//    public static final  String EXCHANGE_AUTHORS = "authors";
//    public static final  String EXCHANGE_COURSES = "courses";
//    public static final  String EXCHANGE_EDUCATIONALSTEPS = "educationalsteps";
//    public static final  String EXCHANGE_TESTSTEPS = "teststeps";
//    public static final  String EXCHANGE_TESTSTEPSOPTIONS = "teststepoptions";





//    public static final  String EXCHANGE_AUTHORS_CLIENT = "client.authors";
//    public static final  String EXCHANGE_COURSES_CLIENT = "client.courses";
//    public static final  String EXCHANGE_EDUCATIONALSTEPS_CLIENT = "client.educationalsteps";
//    public static final  String EXCHANGE_TESTSTEPS_CLIENT = "client.teststeps";
//    public static final  String EXCHANGE_TESTSTEPSOPTIONS_CLIENT = "client.teststepoptions";








//    //    QUEUE CLIENT NAMES
//    public static final  String QUEUE_STUDENT_CREATE_CLIENT_NAME = "student_create_client_queue";
//    public static final  String QUEUE_STUDENT_UPDATE_CLIENT_NAME = "student_update_client_queue";
//    public static final  String QUEUE_STUDENT_DELETE_CLIENT_NAME = "student_delete_client_queue";
//
//    public static final  String QUEUE_AUTHOR_CREATE_CLIENT_NAME = "author_create_client_queue";
//    public static final  String QUEUE_AUTHOR_UPDATE_CLIENT_NAME = "author_update_client_queue";
//    public static final  String QUEUE_AUTHOR_DELETE_CLIENT_NAME = "author_delete_client_queue";
//
//    public static final  String QUEUE_COURSE_CREATE_CLIENT_NAME = "course_create_client_queue";
//    public static final  String QUEUE_COURSE_UPDATE_CLIENT_NAME = "course_update_client_queue";
//    public static final  String QUEUE_COURSE_DELETE_CLIENT_NAME = "course_delete_client_queue";
//
//    public static final  String QUEUE_EDUCATIONALSTEP_CREATE_CLIENT_NAME = "educationalstep_create_client_queue";
//    public static final  String QUEUE_EDUCATIONALSTEP_UPDATE_CLIENT_NAME = "educationalstep_update_client_queue";
//    public static final  String QUEUE_EDUCATIONALSTEP_DELETE_CLIENT_NAME = "educationalstep_delete_client_queue";
//
//    public static final  String QUEUE_TESTSTEP_CREATE_CLIENT_NAME = "teststep_create_client_queue";
//    public static final  String QUEUE_TESTSTEP_UPDATE_CLIENT_NAME = "teststep_update_client_queue";
//    public static final  String QUEUE_TESTSTEP_DELETE_CLIENT_NAME = "teststep_delete_client_queue";
//
//    public static final  String QUEUE_TESTSTEPOPTION_CREATE_CLIENT_NAME = "teststepoption_create_client_queue";
//    public static final  String QUEUE_TESTSTEPOPTION_UPDATE_CLIENT_NAME = "teststepoption_update_client_queue";
//    public static final  String QUEUE_TESTSTEPOPTION_DELETE_CLIENT_NAME = "teststepoption_delete_client_queue";







//    public static final  String QUEUE_NAME = "mooc_platform_queue";
//    public static final  String EXCHANGE = "mooc_platform_exchange";
//    public static final  String ROUTING_KEY = "mooc_platform_routingKey";
