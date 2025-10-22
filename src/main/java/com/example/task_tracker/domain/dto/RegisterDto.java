package com.example.task_tracker.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDto(


                @NotBlank(message = "username can't be empty") String userName,

                @NotBlank(message = "password can't be empty") String passWord) {

}
