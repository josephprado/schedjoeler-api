package com.portfolijo.schedjoeler.controller;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.UserCreateDto;
import com.portfolijo.schedjoeler.dto.UserDto;
import com.portfolijo.schedjoeler.dto.UserDtoBase;
import com.portfolijo.schedjoeler.dto.UserUpdateDto;
import com.portfolijo.schedjoeler.service.UserService;
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

    @GetMapping("/{uuid}")
    public ResponseEntity<Response<UserDto>> getOne(@PathVariable(name = "uuid") UUID uuid) {
        UserDto data = convert(SVC.findOne(uuid));
        return responseCodeOk(List.of(data));
    }

    @GetMapping("")
    public ResponseEntity<Response<UserDto>> getAll() {
        List<UserDto> data = SVC.findAll().stream().map(this::convert).toList();
        return responseCodeOk(data);
    }

    @PostMapping("")
    public ResponseEntity<Response<UserDto>> saveOne(@RequestBody UserCreateDto dto) {
        return null;
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Response<UserDto>> updateOne(@PathVariable(name = "uuid") UUID uuid,
                                                       @RequestBody UserUpdateDto dto) {
        return null;
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response<UserDto>> deleteOne(@PathVariable(name = "uuid") UUID uuid) throws Exception {
        SVC.deleteOne(uuid);
        return responseCodeNoContent();
    }

    public UserDto convert(User user) {
        return UserDto.builder().build();
    }
}
