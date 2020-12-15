package com.orlovsky.mooc_platform.rabbitMessages.accountServerMessages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteStudentMessageBody implements Serializable {
    private UUID studentId;
}
