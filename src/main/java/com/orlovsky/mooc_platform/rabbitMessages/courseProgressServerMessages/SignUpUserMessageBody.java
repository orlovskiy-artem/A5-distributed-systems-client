package com.orlovsky.mooc_platform.rabbitMessages.courseProgressServerMessages;

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
public class SignUpUserMessageBody implements Serializable {
    private UUID courseId;
    private UUID studentId;
}
