package com.portfolijo.schedjoeler.controller;

import com.portfolijo.schedjoeler.converter.UserConverter;
import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.UserCreateDto;
import com.portfolijo.schedjoeler.dto.UserDto;
import com.portfolijo.schedjoeler.dto.UserDtoBase;
import com.portfolijo.schedjoeler.dto.UserUpdateDto;
import com.portfolijo.schedjoeler.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Handles requests for {@link User} resources
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController extends Controller<UserDtoBase> {
    private final UserService SVC;
    private final UserConverter CON;

    /**
     * Gets the identified user
     *
     * @param uuid A user uuid
     * @return A response entity containing a user
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<Response<UserDto>> getOne(@PathVariable(name = "uuid") UUID uuid) {
        User user = SVC.findOne(uuid);
        UserDto data = CON.toDto(user);
        return responseCodeOk(List.of(data));
    }

    /**
     * Gets all users
     *
     * @return A response entity containing a list of users
     */
    @GetMapping("")
    public ResponseEntity<Response<UserDto>> getAll() {
        List<UserDto> data = SVC
                .findAll()
                .stream()
                .map(CON::toDto)
                .toList();
        return responseCodeOk(data);
    }

    /**
     * Saves the given user
     *
     * @param dto A user create DTO
     * @return A response entity containing the saved user
     */
    @PostMapping("")
    public ResponseEntity<Response<UserDto>> saveOne(@Valid @RequestBody UserCreateDto dto) {
        User user = CON.toUser(dto);
        user = SVC.saveOne(user);
        UserDto data = CON.toDto(user);
        return responseCodeCreated(List.of(data), "/"+data.getUuid());
    }

    /**
     * Updates the identified user
     *
     * @param uuid A user uuid
     * @param dto A user update DTO
     * @return A response entity containing the updated user
     */
    @PatchMapping("/{uuid}")
    public ResponseEntity<Response<UserDto>> updateOne(@PathVariable(name = "uuid") UUID uuid,
                                                       @Valid @RequestBody UserUpdateDto dto) {
        User user = CON.toUser(uuid, dto);
        user = SVC.saveOne(user);
        UserDto data = CON.toDto(user);
        return responseCodeOk(List.of(data));
    }

    /**
     * Deletes the identified user
     *
     * @param uuid A user uuid
     * @return A response entity containing the result of the deletion
     * @throws Exception If the deletion was unsuccessful
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response<UserDto>> deleteOne(@PathVariable(name = "uuid") UUID uuid) throws Exception {
        SVC.deleteOne(uuid);
        return responseCodeNoContent();
    }
}
