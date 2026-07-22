package com.gabriel.cashcard_api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final HandlerExceptionResolver resolver;
    private final CustomUserDetailsService userDetailsService;

    public SecurityFilter(
            TokenService tokenService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
            CustomUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.resolver = resolver;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = this.recoverToken(request);

            if (token != null) {
                var email = tokenService.validateToken(token);

                var user = userDetailsService.loadUserByUsername(email);

                var authenticate = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticate);
            }

            filterChain.doFilter(request, response);
        } catch (BadCredentialsException ex) {
            resolver.resolveException(request, response, null, ex);

        } catch (Exception ex) {
            resolver.resolveException(request, response, null, ex);

        }
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) return null;

        return authHeader.replace("Bearer ", "");
    }
}
