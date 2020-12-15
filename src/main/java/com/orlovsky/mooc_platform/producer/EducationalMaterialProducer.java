package com.orlovsky.mooc_platform.producer;

import com.orlovsky.mooc_platform.config.EducationalMaterialServiceConfig;
import com.orlovsky.mooc_platform.config.RabbitMqConfig;
import com.orlovsky.mooc_platform.dto.CourseDTO;
import com.orlovsky.mooc_platform.dto.EducationalStepDTO;
import com.orlovsky.mooc_platform.dto.TestStepDTO;
import com.orlovsky.mooc_platform.dto.TestStepOptionRequestDTO;
import com.orlovsky.mooc_platform.model.Course;
import com.orlovsky.mooc_platform.model.EducationalStep;
import com.orlovsky.mooc_platform.model.TestStep;
import com.orlovsky.mooc_platform.model.TestStepOption;
import com.orlovsky.mooc_platform.rabbitMessages.educationalMaterialServerMessages.AddEducationalStepMessageBody;
import com.orlovsky.mooc_platform.rabbitMessages.educationalMaterialServerMessages.AddTestStepMessageBody;
import com.orlovsky.mooc_platform.rabbitMessages.educationalMaterialServerMessages.AddTestStepOptionMessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class EducationalMaterialProducer {
    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange exchange;

    public Course sendCreateCourseMessage(CourseDTO courseDTO) {
        log.info("Sending message to create course: {}",courseDTO);
        Course course = (Course) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                EducationalMaterialServiceConfig.ROUTING_KEY_COURSE_CREATE,
                courseDTO);
        log.info("Course created: {}",course);
        return course;
    }

    public EducationalStep sendCreateEducationalStepMessage(UUID courseId,
                                                   EducationalStepDTO educationalStepDTO) {
        AddEducationalStepMessageBody addEducationalStepMessageBody =
                AddEducationalStepMessageBody.builder().courseId(courseId).educationalStepDTO(educationalStepDTO).build();
        log.info("Sending message to create educational step: {}",addEducationalStepMessageBody);
        EducationalStep educationalStep = (EducationalStep) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                EducationalMaterialServiceConfig.ROUTING_KEY_EDUCATIONALSTEP_CREATE,
                addEducationalStepMessageBody);
        log.info("Educational step added: {}",educationalStep);
        return educationalStep;
    }

    public TestStep sendCreateTestStepMessage(UUID courseId,
                                              TestStepDTO testStepDTO) {
        AddTestStepMessageBody addTestStepMessageBody =
                AddTestStepMessageBody.builder().courseId(courseId).testStepDTO(testStepDTO).build();
        log.info("Sending message to create educational step: {}",addTestStepMessageBody);
        TestStep testStep = (TestStep) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                EducationalMaterialServiceConfig.ROUTING_KEY_TESTSTEP_CREATE,
                addTestStepMessageBody);
        log.info("Test step added: {}",testStep);
        return testStep;
    }

    public TestStepOption sendCreateTestStepOptionMessage(UUID courseId,
                                                    UUID testStepId,
                                                    TestStepOptionRequestDTO testStepOptionRequestDTO) {
        AddTestStepOptionMessageBody addTestStepOptionMessageBody =
                AddTestStepOptionMessageBody.builder().courseId(courseId)
                        .testStepId(testStepId).testStepOptionRequestDTO(testStepOptionRequestDTO).build();
        log.info("Sending message to create educational step: {}",addTestStepOptionMessageBody);
        TestStepOption testStepOption = (TestStepOption) template.convertSendAndReceive(
                RabbitMqConfig.EXCHANGE_NAME,
                EducationalMaterialServiceConfig.ROUTING_KEY_TESTSTEPOPTION_CREATE,
                addTestStepOptionMessageBody);
        log.info("Test step option added: {}",testStepOption);
        return testStepOption;
    }
}
