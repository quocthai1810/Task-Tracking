package com.example.task_tracker.domain.dto;

public record ErrorReponse(int status,
    String message,
    String details) {
    
}
