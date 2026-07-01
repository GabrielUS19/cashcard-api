package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dto.requests.UserCreateRequest;
import com.gabriel.cashcard_api.dto.responses.UserResponse;
import com.gabriel.cashcard_api.exceptions.UserAlreadyExistException;
import com.gabriel.cashcard_api.exceptions.UserNotFoundException;
import com.gabriel.cashcard_api.models.UserModel;
import com.gabriel.cashcard_api.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public UserResponse createUser(UserCreateRequest user) {
        if (!userRepository.findByEmail(user.email()).isEmpty()) {
            throw new UserAlreadyExistException("Email already in use");
        }

        var userEntity = new UserModel();

        userEntity.setName(user.name());
        userEntity.setEmail(user.email());
        userEntity.setPassword(encoder.encode(user.password()));

        var savedUser = userRepository.save(userEntity);

        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public UserResponse findById(UUID userId) {
        return userRepository
                .findById(userId)
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail()))
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }
}
