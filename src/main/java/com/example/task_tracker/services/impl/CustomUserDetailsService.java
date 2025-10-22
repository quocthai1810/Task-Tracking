package com.example.task_tracker.services.impl;

import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.task_tracker.repositories.AuthRepository; // thay bằng repo thật của bạn
import com.example.task_tracker.security.CustomUserDetails;
import com.example.task_tracker.domain.entities.Auth; // hoặc User entity của bạn

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    public CustomUserDetailsService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String idAuth) throws UsernameNotFoundException {
        Auth auth = authRepository.findById(UUID.fromString(idAuth))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(auth);
    }
}
