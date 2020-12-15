package com.orlovsky.mooc_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDTO implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String description;
}
