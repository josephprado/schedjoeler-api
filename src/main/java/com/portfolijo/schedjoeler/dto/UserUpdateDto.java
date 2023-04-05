package com.portfolijo.schedjoeler.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Represents an update to an existing application user
 */
@Getter
@Builder
public class UserUpdateDto extends UserDtoBase {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
