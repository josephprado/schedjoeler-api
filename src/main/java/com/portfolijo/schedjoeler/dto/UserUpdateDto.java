package com.portfolijo.schedjoeler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Represents an update to an existing application user
 */
@Getter
@Builder
public class UserUpdateDto {
    @NotNull
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
