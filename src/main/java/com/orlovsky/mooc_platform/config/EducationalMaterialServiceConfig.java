package com.orlovsky.mooc_platform.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EducationalMaterialServiceConfig {

    //    ------------------QUEUES------------------
    public static final  String QUEUE_COURSE_CREATE_NAME = "course_create_queue";
    public static final  String QUEUE_COURSE_UPDATE_NAME = "course_update_queue";
    public static final  String QUEUE_COURSE_DELETE_NAME = "course_delete_queue";

    public static final  String QUEUE_EDUCATIONALSTEP_CREATE_NAME = "educationalStep_create_queue";
    public static final  String QUEUE_EDUCATIONALSTEP_UPDATE_NAME = "educationalStep_update_queue";
    public static final  String QUEUE_EDUCATIONALSTEP_DELETE_NAME = "educationalStep_delete_queue";

    public static final  String QUEUE_TESTSTEP_CREATE_NAME = "testStep_create_queue";
    public static final  String QUEUE_TESTSTEP_UPDATE_NAME = "testStep_update_queue";
    public static final  String QUEUE_TESTSTEP_DELETE_NAME = "testStep_delete_queue";

    public static final  String QUEUE_TESTSTEPOPTION_CREATE_NAME = "testStepOption_create_queue";
    public static final  String QUEUE_TESTSTEPOPTION_UPDATE_NAME = "testStepOption_update_queue";
    public static final  String QUEUE_TESTSTEPOPTION_DELETE_NAME = "testStepOption_delete_queue";

    public static final  String QUEUE_ADD_AUTHOR_TO_COURSE = "add_author_to_course_queue";


    //    ------------------ROUTING KEYS NAMES------------------
    public static final  String ROUTING_KEY_COURSE_CREATE = "course.create";
    public static final  String ROUTING_KEY_EDUCATIONALSTEP_CREATE = "educationalStep.create";
    public static final  String ROUTING_KEY_TESTSTEP_CREATE = "testStep.create";
    public static final  String ROUTING_KEY_TESTSTEPOPTION_CREATE = "testStepOption.create";
    public static final  String ROUTING_KEY_ADD_AUTHOR_TO_COURSE = "author.course.add";


    //    ------------------Queues------------------
//     Create
    @Bean
    @Qualifier("queueCourseCreate")
    public Queue queueCourseCreate(){
        return new Queue(QUEUE_COURSE_CREATE_NAME);
    }

    @Bean
    @Qualifier("queueEducationalStepCreate")
    public Queue queueEducationalStepCreate(){
        return new Queue(QUEUE_EDUCATIONALSTEP_CREATE_NAME);
    }

    @Bean
    @Qualifier("queueTestStepCreate")
    public Queue queueTestStepCreate(){
        return new Queue(QUEUE_TESTSTEP_CREATE_NAME);
    }

    @Bean
    @Qualifier("queueTestStepOptionCreate")
    public Queue queueTestStepOptionCreate(){
        return new Queue(QUEUE_TESTSTEPOPTION_CREATE_NAME);
    }

    @Bean
    @Qualifier("queueAddAuthorToCourse")
    public Queue queueAddAuthorToCourse(){ return new Queue(QUEUE_ADD_AUTHOR_TO_COURSE); }

    //  ------------------Bindings------------------
    @Bean
    @Qualifier("bindingCourseCreate")
    public Binding bindingCourseCreate(@Qualifier("queueCourseCreate") Queue queueCourseCreate,
                                       DirectExchange exchange){
        return BindingBuilder.bind(queueCourseCreate)
                .to(exchange)
                .with(ROUTING_KEY_COURSE_CREATE);
    }

    @Bean
    @Qualifier("bindingEducationalStepCreate")
    public Binding bindingEducationalStepCreate(@Qualifier("queueEducationalStepCreate") Queue queueEducationalStepCreate,
                                                DirectExchange exchange){
        return BindingBuilder.bind(queueEducationalStepCreate)
                .to(exchange)
                .with(ROUTING_KEY_EDUCATIONALSTEP_CREATE);
    }

    @Bean
    @Qualifier("bindingTestStepCreate")
    public Binding bindingTestStepCreate(@Qualifier("queueTestStepCreate") Queue queueTestStepCreate,
                                         DirectExchange exchange){
        return BindingBuilder.bind(queueTestStepCreate)
                .to(exchange)
                .with(ROUTING_KEY_TESTSTEP_CREATE);
    }

    @Bean
    @Qualifier("bindingTestStepOptionCreate")
    public Binding bindingTestStepOptionCreate(@Qualifier("queueTestStepOptionCreate") Queue queueTestStepOptionCreate,
                                               DirectExchange exchange){
        return BindingBuilder.bind(queueTestStepOptionCreate)
                .to(exchange)
                .with(ROUTING_KEY_TESTSTEPOPTION_CREATE);
    }

    @Bean
    @Qualifier("bindingAddAuthorToCourse")
    public Binding bindingAddAuthorToCourse(@Qualifier("queueAddAuthorToCourse") Queue queueAddAuthorToCourse,
                                            DirectExchange exchange){
        return BindingBuilder.bind(queueAddAuthorToCourse)
                .to(exchange)
                .with(ROUTING_KEY_ADD_AUTHOR_TO_COURSE);
    }
}
