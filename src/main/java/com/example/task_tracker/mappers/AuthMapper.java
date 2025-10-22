package com.example.task_tracker.mappers;

import com.example.task_tracker.domain.dto.LoginDto;
import com.example.task_tracker.domain.dto.RegisterDto;
import com.example.task_tracker.domain.entities.Auth;

public interface AuthMapper {
    Auth fromRegisterDto(RegisterDto registerDto);


    Auth fromLoginDto(LoginDto loginDto);

    
}
