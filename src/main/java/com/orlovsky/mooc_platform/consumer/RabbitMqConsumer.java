package com.orlovsky.mooc_platform.consumer;

import com.orlovsky.mooc_platform.config.RabbitMqConfig;
import com.orlovsky.mooc_platform.model.Student;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

//@Component
public class RabbitMqConsumer {

//    @RabbitListener(queues = RabbitMqConfig.QUEUE_STUDENT_CREATE_NAME_CLIENT)
//    public void consumeMessageFromQueue(Student student){
//        System.out.println("Message recieved from queue : " + student.toString());
//    }
}
