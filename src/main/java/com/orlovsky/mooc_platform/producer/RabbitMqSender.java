package com.orlovsky.mooc_platform.producer;

import com.google.gson.Gson;
import com.orlovsky.mooc_platform.config.RabbitMqConfig;
import com.orlovsky.mooc_platform.dto.StudentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Slf4j
//@Component
public class RabbitMqSender {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    public void sendCreateStudentMessage(StudentDTO studentDTO) {
//        log.info("Sending message to create student: {}", studentDTO);
//        rabbitTemplate.convertAndSend(
//                RabbitMqConfig.EXCHANGE_STUDENTS,
//                RabbitMqConfig.ROUTING_KEY_STUDENT_CREATE,
//                studentDTO);
//    }
}


//
//    public static void main(String[] args) {
//        SpringApplication.run(RabbitMQSender.class, args);
//    }
//
//    @Bean
//    public ApplicationRunner runner(RabbitTemplate rabbitTemplate) {
//        String message = " payload is broadcast";
//        return args -> {
//            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE, "", "fanout" + message);
//        };
//    }
//
//    @RabbitListener(queues = { FANOUT_QUEUE_1_NAME })
//    public void receiveMessageFromFanout1(String message) {
//        System.out.println("Received fanout 1 message: " + message);
//    }
//
//    @RabbitListener(queues = { FANOUT_QUEUE_2_NAME })
//    public void receiveMessageFromFanout2(String message) {
//        System.out.println("Received fanout 2 message: " + message);
//    }


//    @Autowired
//    private Queue queueCreateStudent;
//    @Autowired
//    private Binding bindingStudentCreate;
//    @Autowired
//    private TopicExchange topicExchange;
//
//    public RabbitMQSender() {
//        super();
//        this.rabbitTemplate = rabbitTemplate;
//    }


//        String json = new Gson().toJson(message);
//        log.info("Sending message {}", json);
//        rabbitTemplate.convertAndSend(bindingStudentCreate.getRoutingKey(), studentDTO);
