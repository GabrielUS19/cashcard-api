package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dto.requests.UserCreateRequest;
import com.gabriel.cashcard_api.exceptions.UserAlreadyExistException;
import com.gabriel.cashcard_api.exceptions.UserNotFoundException;
import com.gabriel.cashcard_api.models.UserModel;
import com.gabriel.cashcard_api.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return user when valid credentials")
    void shouldReturnUserWhenValidCredential() {
        var inputUser = new UserCreateRequest("Gabriel", "email_real@hotmail.com", "Senhaforte@123", "Senhaforte@123");

        var generatedId = UUID.randomUUID();
        var savedUser = new UserModel(generatedId, "Gabriel", "email_real@hotmail.com", "Senhaforte@123", new HashSet<>());

        when(userRepository.save(any(UserModel.class))).thenReturn(savedUser);

        var result = userService.createUser(inputUser);

        assertThat(result).isNotNull();

        assertThat(result.id()).isEqualTo(generatedId);

        verify(userRepository).findByEmail(inputUser.email());
        verify(userRepository).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistException when email already in use")
    void shouldThrowExceptionWhenEmailAlreadyInUse() {
        var inputUser = new UserCreateRequest("Gabriel", "email_real@hotmail.com", "Senhaforte@123", "Senhaforte@123");

        var generatedId = UUID.randomUUID();
        var savedUser = new UserModel(generatedId, "Gabriel", "email_real@hotmail.com", "Senhaforte@123", new HashSet<>());

        when(userRepository.findByEmail("email_real@hotmail.com")).thenReturn(Optional.of(savedUser));

        assertThatThrownBy(() -> userService.createUser(inputUser))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessageContaining("Email already in use");

        verify(userRepository).findByEmail(inputUser.email());
        verify(userRepository, never()).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Should return User when valid ID")
    void shouldReturnUserWhenValidId() {
        var generatedId = UUID.randomUUID();

        var savedUser = new UserModel(generatedId, "Gabriel", "email_real@hotmail.com", "Senhaforte@123", new HashSet<>());

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(savedUser));

        var result = userService.findById(generatedId);

        assertThat(result).isNotNull();

        assertThat(result.id()).isEqualTo(generatedId);

        verify(userRepository).findById(generatedId);
    }

    @Test
    @DisplayName("Should Throw UserNotFoundException when user with this ID not found")
    void shouldThrowUserNotFoundExceptionWhenInvalidId() {
        var generatedId = UUID.randomUUID();

        when(userRepository.findById(generatedId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(generatedId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + generatedId);

        verify(userRepository).findById(generatedId);
    }
}