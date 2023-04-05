package com.portfolijo.schedjoeler.controller;

import com.portfolijo.schedjoeler.domain.User;
import com.portfolijo.schedjoeler.dto.UserCreateDto;
import com.portfolijo.schedjoeler.dto.UserUpdateDto;
import com.portfolijo.schedjoeler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Handles requests for {@link User} resources
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController extends Controller {
    private final UserService SVC;

    @GetMapping("/{uuid}")
    public ResponseEntity<Response> getOne(@PathVariable(name = "uuid") UUID uuid) {
        return null;
    }

    @GetMapping("")
    public ResponseEntity<Response> getAll() {
        return null;
    }

    @PostMapping("")
    public ResponseEntity<Response> saveOne(@RequestBody UserCreateDto dto) {
        return null;
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Response> updateOne(@PathVariable(name = "uuid") UUID uuid,
                                              @RequestBody UserUpdateDto dto) {
        return null;
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Response> deleteOne(@PathVariable(name = "uuid") UUID uuid) {
        return null;
    }
}
