package com.gabriel.cashcard_api.controllers;

import com.gabriel.cashcard_api.dto.requests.UserCreateRequest;
import com.gabriel.cashcard_api.dto.responses.UserResponse;
import com.gabriel.cashcard_api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    private ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest userDTO) {
        var user = userService.createUser(userDTO);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.id())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        var user = userService.findById(id);

        return ResponseEntity.ok(user);
    }
}
