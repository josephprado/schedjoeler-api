package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Represents a new application user
 */
@Getter
@Builder
public class UserCreateDto extends UserDtoBase {
    @NotBlank
    @NonNull
    private String firstName;

    @NotBlank
    @NonNull
    private String lastName;

    private String email;

    private String phone;
}
