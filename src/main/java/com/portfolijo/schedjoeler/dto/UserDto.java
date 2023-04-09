package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

/**
 * Represents an application user
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserDto extends UserDtoBase {
    @NotNull
    @NonNull
    private UUID uuid;

    @NotBlank
    @NonNull
    private String firstName;

    @NotBlank
    @NonNull
    private String lastName;

    private String email;

    private String phone;
}
