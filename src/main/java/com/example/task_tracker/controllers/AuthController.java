package com.example.task_tracker.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task_tracker.domain.dto.LoginDto;
import com.example.task_tracker.domain.dto.RegisterDto;
import com.example.task_tracker.domain.entities.Auth;
import com.example.task_tracker.mappers.AuthMapper;
import com.example.task_tracker.services.AuthService;

import jakarta.validation.Valid;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    private final AuthMapper authMapper;
    private final AuthService authService;

    public AuthController(AuthMapper authMapper, AuthService authService) {
        this.authMapper = authMapper;
        this.authService = authService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterDto registerDto) {

        return authService.register(authMapper.fromRegisterDto(registerDto));
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginDto loginDto) {

        return authService.login(authMapper.fromLoginDto(loginDto));
    }

}
