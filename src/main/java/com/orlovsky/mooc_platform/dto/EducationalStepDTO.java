package com.orlovsky.mooc_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationalStepDTO  implements Serializable {
    private UUID id;
    private UUID courseId;
    private URI eduMaterialUri;
    private int position;
}

