package com.orlovsky.mooc_platform.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountServiceConfig {
    //        QUEUE NAMES
    public static final  String QUEUE_STUDENT_CREATE_NAME = "student_create_queue";
    public static final  String QUEUE_STUDENT_DELETE_NAME = "student_delete_queue";

    public static final  String QUEUE_AUTHOR_CREATE_NAME = "author_create_queue";
    public static final  String QUEUE_AUTHOR_DELETE_NAME = "author_delete_queue";

    //           ROUTING KEYS NAMES
    public static final  String ROUTING_KEY_STUDENT_CREATE = "student.create";
    public static final  String ROUTING_KEY_STUDENT_DELETE = "student.delete";

    public static final  String ROUTING_KEY_AUTHOR_CREATE = "author.create";
    public static final  String ROUTING_KEY_AUTHOR_DELETE = "author.delete";



    //    -----------------Queues-----------------
    @Bean
    @Qualifier("queueStudentCreate")
    public Queue queueStudentCreate(){
        return new Queue(QUEUE_STUDENT_CREATE_NAME);
    }

    @Bean
    @Qualifier("queueAuthorCreate")
    public Queue queueAuthorCreate(){
        return new Queue(QUEUE_AUTHOR_CREATE_NAME);
    }

    @Bean
    @Qualifier("queueStudentDelete")
    public Queue queueStudentDelete(){
        return new Queue(QUEUE_STUDENT_DELETE_NAME);
    }

    @Bean
    @Qualifier("queueAuthorDelete")
    public Queue queueAuthorDelete(){
        return new Queue(QUEUE_AUTHOR_DELETE_NAME);
    }

    //  -----------------Bindings-----------------
    @Bean
    @Qualifier("bindingStudentCreate")
    public Binding bindingStudentCreate(@Qualifier("queueStudentCreate") Queue queueStudentCreate,
                                        DirectExchange exchange){
        return BindingBuilder.bind(queueStudentCreate)
                .to(exchange)
                .with(ROUTING_KEY_STUDENT_CREATE);
    }
    @Bean
    @Qualifier("bindingAuthorCreate")
    public Binding bindingAuthorCreate(@Qualifier("queueAuthorCreate") Queue queueAuthorCreate,
                                       DirectExchange exchange){
        return BindingBuilder.bind(queueAuthorCreate)
                .to(exchange)
                .with(ROUTING_KEY_AUTHOR_CREATE);
    }

    @Bean
    @Qualifier("bindingStudentDelete")
    public Binding bindingStudentDelete(@Qualifier("queueStudentDelete") Queue queueStudentDelete,
                                        DirectExchange exchange){
        return BindingBuilder.bind(queueStudentDelete)
                .to(exchange)
                .with(ROUTING_KEY_STUDENT_DELETE);
    }
    @Bean
    @Qualifier("bindingAuthorDelete")
    public Binding bindingAuthorDelete(@Qualifier("queueAuthorDelete") Queue queueAuthorDelete,
                                       DirectExchange exchange){
        return BindingBuilder.bind(queueAuthorDelete)
                .to(exchange)
                .with(ROUTING_KEY_AUTHOR_DELETE);
    }
}
