package com.orlovsky.mooc_platform.rabbitMessages.educationalMaterialServerMessages;

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
public class DeleteCourseMessageBody implements Serializable {
    private UUID courseId;
}
