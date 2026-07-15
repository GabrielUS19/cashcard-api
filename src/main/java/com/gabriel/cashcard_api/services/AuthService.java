package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.config.TokenConfig;
import com.gabriel.cashcard_api.dto.requests.LoginRequest;
import com.gabriel.cashcard_api.dto.responses.LoginResponse;
import com.gabriel.cashcard_api.entities.User;
import com.gabriel.cashcard_api.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public LoginResponse login(LoginRequest request) {
        var userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        var authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String accessToken = tokenConfig.generateToken(user);

        return new LoginResponse(accessToken);
    }
}
