package com.example.task_tracker.jwt;

import java.util.List;
import java.util.Map;

public interface JwtService {
    String generateToken(String idAuth, List<String> role);

    String extractId(String token);

    boolean isTokenExpired(String token);
}
