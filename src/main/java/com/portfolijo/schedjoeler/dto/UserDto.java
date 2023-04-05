package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Represents an application user
 */
@Getter
@Builder
public class UserDto extends UserDtoBase {
    @NotNull
    private UUID uuid;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String email;
    private String phone;
}
