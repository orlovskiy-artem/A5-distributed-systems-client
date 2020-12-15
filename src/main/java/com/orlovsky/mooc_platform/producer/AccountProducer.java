package com.orlovsky.mooc_platform.producer;

import com.orlovsky.mooc_platform.config.AccountServiceConfig;
import com.orlovsky.mooc_platform.config.RabbitMqConfig;
import com.orlovsky.mooc_platform.dto.AuthorDTO;
import com.orlovsky.mooc_platform.dto.StudentDTO;
import com.orlovsky.mooc_platform.model.Author;
import com.orlovsky.mooc_platform.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountProducer {
    @Autowired
    private RabbitTemplate template;

    public Student sendCreateStudentMessage(StudentDTO studentDTO) {
        log.info("Sending message to create student: {}", studentDTO);
        Student student = (Student) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                AccountServiceConfig.ROUTING_KEY_STUDENT_CREATE,
                studentDTO);
        log.info("Student created: {}",student);
        return student;
    }
    public Author sendCreateAuthorMessage(AuthorDTO authorDTO) {
        log.info("Sending message to create author: {}", authorDTO);
        Author author = (Author) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                AccountServiceConfig.ROUTING_KEY_AUTHOR_CREATE,
                authorDTO);
        log.info("Author created: {}", author);
        return author;
    }
}
