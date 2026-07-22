package com.gabriel.cashcard_api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(CustomUserDetails userDetails) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            var roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return JWT.create()
                    .withIssuer("cashcard-api")
                    .withSubject(userDetails.getUsername())
                    .withClaim("roles", roles)
                    .withExpiresAt(Instant.now().plusSeconds(15*60))
                    .sign(algorithm);

        } catch (JWTCreationException ex) {
            throw new RuntimeException("Error while generating token", ex);
        }
    }

    public String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("cashcard-api")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (TokenExpiredException ex) {
            throw new BadCredentialsException("JWT Token Expired");

        } catch (JWTVerificationException ex) {
            throw new BadCredentialsException("Invalid JWT Token");
        }
    }
}
