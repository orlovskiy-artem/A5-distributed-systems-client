package com.orlovsky.mooc_platform.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EducationalStep implements Step {
    private UUID id;

    private URI eduMaterialUri;

    private int position;
}