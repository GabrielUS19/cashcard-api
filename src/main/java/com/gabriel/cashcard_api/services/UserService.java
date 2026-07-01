package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dto.UserCreateDTO;
import com.gabriel.cashcard_api.dto.UserResponseDTO;
import com.gabriel.cashcard_api.exceptions.UserAlreadyExistException;
import com.gabriel.cashcard_api.exceptions.UserNotFoundException;
import com.gabriel.cashcard_api.models.UserModel;
import com.gabriel.cashcard_api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserCreateDTO user) {
        if (!userRepository.findByEmail(user.email()).isEmpty()) {
            throw new UserAlreadyExistException("Email already in use");
        }

        var userEntity = new UserModel();

        userEntity.setName(user.name());
        userEntity.setEmail(user.email());
        userEntity.setPassword(user.password());

        var savedUser = userRepository.save(userEntity);

        return new UserResponseDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public UserResponseDTO findById(UUID userId) {
        return userRepository
                .findById(userId)
                .map(u -> new UserResponseDTO(u.getId(), u.getName(), u.getEmail()))
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }
}
