package com.gabriel.cashcard_api.services;

import com.gabriel.cashcard_api.dto.requests.LoginRequest;
import com.gabriel.cashcard_api.dto.responses.LoginResponse;
import com.gabriel.cashcard_api.infra.security.CustomUserDetails;
import com.gabriel.cashcard_api.infra.security.TokenService;
import com.gabriel.cashcard_api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest request) {
        var userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());

        var authentication = authenticationManager.authenticate(userAndPass);

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = tokenService.generateToken(user);

        return new LoginResponse(accessToken);
    }
}
