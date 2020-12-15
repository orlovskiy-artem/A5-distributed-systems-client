package com.orlovsky.mooc_platform.rabbitMessages.courseProgressServerMessages;

import com.orlovsky.mooc_platform.dto.TestStepOptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MakeProcessedTestStepMessageBody implements Serializable {
    private UUID courseId;
    private UUID studentId;
    private UUID testStepId;
    private TestStepOptionDTO chosenAnswer;
}
