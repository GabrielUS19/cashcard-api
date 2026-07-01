package com.gabriel.cashcard_api.controllers;

import com.gabriel.cashcard_api.dto.UserCreateDTO;
import com.gabriel.cashcard_api.dto.UserResponseDTO;
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
    private ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userDTO) {
        var user = userService.createUser(userDTO);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.id())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    @GetMapping("/{id}")
    private ResponseEntity<UserResponseDTO> findById(@PathVariable UUID id) {
        var user = userService.findById(id);

        return ResponseEntity.ok(user);
    }
}
