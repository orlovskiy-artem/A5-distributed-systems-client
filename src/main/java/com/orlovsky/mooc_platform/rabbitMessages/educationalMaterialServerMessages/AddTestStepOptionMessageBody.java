package com.orlovsky.mooc_platform.rabbitMessages.educationalMaterialServerMessages;

import com.orlovsky.mooc_platform.dto.TestStepOptionRequestDTO;
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
public class AddTestStepOptionMessageBody implements Serializable {
    private UUID courseId;
    private UUID testStepId;
    private TestStepOptionRequestDTO testStepOptionRequestDTO;
}
