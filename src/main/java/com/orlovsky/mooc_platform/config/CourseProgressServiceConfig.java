package com.orlovsky.mooc_platform.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseProgressServiceConfig {

    //    ------------------QUEUES------------------
    public static final  String QUEUE_SIGN_UP_STUDENT = "sign_up_student_queue";
    public static final  String QUEUE_PASS_EDUCATIONALSTEP = "pass_educationalStep_queue";
    public static final  String QUEUE_PROCESS_TESTSTEP = "process_educationalStep_queue";

    //    ------------------ROUTING KEYS NAMES------------------
    public static final  String ROUTING_KEY_SIGN_UP_STUDENT = "signUp.course.student";
    public static final  String ROUTING_KEY_PASS_EDUCATIONALSTEP = "pass.educationalStep";
    public static final  String ROUTING_KEY_PROCESS_TESTSTEP = "process.testStep";

    //    ------------------Queues------------------
//     Create
    @Bean
    @Qualifier("queueSignUpStudent")
    public Queue queueSignUpStudent(){
        return new Queue(QUEUE_SIGN_UP_STUDENT);
    }

    @Bean
    @Qualifier("queuePassEducationalStep")
    public Queue queuePassEducationalStep(){
        return new Queue(QUEUE_PASS_EDUCATIONALSTEP);
    }

    @Bean
    @Qualifier("queueProcessTestStep")
    public Queue queueProcessTestStep(){
        return new Queue(QUEUE_PROCESS_TESTSTEP);
    }

    //  ------------------Bindings------------------
    @Bean
    @Qualifier("bindingSignUpStudent")
    public Binding bindingSignUpStudent(@Qualifier("queueSignUpStudent") Queue queueSignUpStudent,
                                        DirectExchange exchange){
        return BindingBuilder.bind(queueSignUpStudent)
                .to(exchange)
                .with(ROUTING_KEY_SIGN_UP_STUDENT);
    }

    @Bean
    @Qualifier("bindingPassEducationalStep")
    public Binding bindingPassEducationalStep(@Qualifier("queuePassEducationalStep") Queue queuePassEducationalStep,
                                              DirectExchange exchange){
        return BindingBuilder.bind(queuePassEducationalStep)
                .to(exchange)
                .with(ROUTING_KEY_PASS_EDUCATIONALSTEP);
    }

    @Bean
    @Qualifier("bindingProcessTestStep")
    public Binding bindingProcessTestStep(@Qualifier("queueProcessTestStep") Queue queueProcessTestStep,
                                          DirectExchange exchange){
        return BindingBuilder.bind(queueProcessTestStep)
                .to(exchange)
                .with(ROUTING_KEY_PROCESS_TESTSTEP);
    }
}
