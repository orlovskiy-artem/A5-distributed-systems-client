package com.orlovsky.mooc_platform.dto;

import com.orlovsky.mooc_platform.model.CourseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO implements Serializable {
    private UUID id;
    private String title;
    private String description;
    private int duration;
    private CourseStatus status;
    private long price;
    private int numberOfSteps;
}
