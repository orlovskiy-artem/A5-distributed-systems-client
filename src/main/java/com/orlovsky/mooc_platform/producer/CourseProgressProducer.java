package com.orlovsky.mooc_platform.producer;

import com.orlovsky.mooc_platform.config.AccountServiceConfig;
import com.orlovsky.mooc_platform.config.CourseProgressServiceConfig;
import com.orlovsky.mooc_platform.config.RabbitMqConfig;
import com.orlovsky.mooc_platform.dto.AuthorDTO;
import com.orlovsky.mooc_platform.dto.StudentDTO;
import com.orlovsky.mooc_platform.dto.TestStepOptionDTO;
import com.orlovsky.mooc_platform.model.Author;
import com.orlovsky.mooc_platform.model.Student;
import com.orlovsky.mooc_platform.rabbitMessages.courseProgressServerMessages.MakePassedEducationalStepMessageBody;
import com.orlovsky.mooc_platform.rabbitMessages.courseProgressServerMessages.MakeProcessedTestStepMessageBody;
import com.orlovsky.mooc_platform.rabbitMessages.courseProgressServerMessages.SignUpUserMessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CourseProgressProducer {
    @Autowired
    private RabbitTemplate template;

    public void sendSignUpStudentMessage(UUID courseId, UUID studentId) {
        SignUpUserMessageBody messageBody = SignUpUserMessageBody.builder()
                .courseId(courseId).studentId(studentId).build();
        log.info("Sending message to sign up a student: {}", messageBody);
        template.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                CourseProgressServiceConfig.ROUTING_KEY_SIGN_UP_STUDENT,
                messageBody);
        log.info("Student signed up.");
    }
    public void sendMakePassedEducationalStepMessage(UUID courseId,
                                                     UUID studentId,
                                                     UUID educationalStepId) {
        MakePassedEducationalStepMessageBody messageBody = MakePassedEducationalStepMessageBody.builder()
                .courseId(courseId).studentId(studentId).educationalStepId(educationalStepId).build();
        log.info("Sending message to pass educational step: {}", messageBody);
        template.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                CourseProgressServiceConfig.ROUTING_KEY_PASS_EDUCATIONALSTEP,
                messageBody);
        log.info("Educational step passed");
    }

    public Boolean sendMakeProcessedTestStepMessage(UUID courseId,
                                                 UUID studentId,
                                                 UUID testStepId,
                                                 TestStepOptionDTO chosenAnswer) {
        MakeProcessedTestStepMessageBody messageBody = MakeProcessedTestStepMessageBody.builder()
                .courseId(courseId).studentId(studentId).testStepId(testStepId).chosenAnswer(chosenAnswer).build();
        log.info("Sending message to process test step: {}", messageBody);
        Boolean passed = (Boolean) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                CourseProgressServiceConfig.ROUTING_KEY_PROCESS_TESTSTEP,
                messageBody);
        return passed;
    }
}
