package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

/**
 * Represents a new application user
 */
@Getter
@Builder
public class UserCreateDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String email;
    private String phone;
}
