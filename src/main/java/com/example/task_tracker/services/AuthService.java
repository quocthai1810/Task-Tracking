package com.example.task_tracker.services;

import java.util.Map;

import com.example.task_tracker.domain.entities.Auth;

public interface AuthService {
    Map<String,String> register(Auth auth);
    Map<String,String> login(Auth auth);
}
