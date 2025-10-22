package com.example.task_tracker.domain.dto;

import java.util.UUID;

import jakarta.validation.constraints.Null;

public record LoginDto(
                String userName,
                String passWord) {

}
