package com.portfolijo.schedjoeler.converter;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.UserCreateDto;
import com.portfolijo.schedjoeler.dto.UserDto;
import com.portfolijo.schedjoeler.dto.UserUpdateDto;
import com.portfolijo.schedjoeler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Converts {@link User} objects to/from DTO representations
 */
@RequiredArgsConstructor
@Component
public class UserConverter {
    private final UserService SVC;

    /**
     * Creates a user DTO based on the given user
     *
     * @param user A user
     * @return A user DTO
     */
    public UserDto toDto(User user) {
        return UserDto
                .builder()
                .uuid(user.getUuid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    /**
     * Creates a user based on the given user create DTO
     *
     * @param dto A user create DTO
     * @return A user
     */
    public User toUser(UserCreateDto dto) {
        return User
                .builder()
                .uuid(UUID.randomUUID())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }

    /**
     * Creates an updated version of the identified user based on the update user DTO
     *
     * @param uuid A user uuid
     * @param dto A user DTO
     * @return A user
     */
    public User toUser(UUID uuid, UserUpdateDto dto) {
        User updated = SVC.findOne(uuid);

        String firstName = dto.getFirstName();
        if (firstName != null)
            updated.setFirstName(firstName);

        String lastName = dto.getLastName();
        if (lastName != null)
            updated.setLastName(lastName);

        String email = dto.getEmail();
        if (email != null)
            updated.setEmail(email);

        String phone = dto.getPhone();
        if (phone != null)
            updated.setPhone(phone);

        return updated;
    }
}
