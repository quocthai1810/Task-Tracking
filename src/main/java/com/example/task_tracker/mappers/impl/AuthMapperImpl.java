package com.example.task_tracker.mappers.impl;

import org.springframework.stereotype.Component;

import com.example.task_tracker.domain.dto.LoginDto;
import com.example.task_tracker.domain.dto.RegisterDto;
import com.example.task_tracker.domain.entities.Auth;
import com.example.task_tracker.mappers.AuthMapper;

@Component
public class AuthMapperImpl implements AuthMapper {

    @Override
    public Auth fromRegisterDto(RegisterDto registerDto) {
        return new Auth(null, registerDto.userName(), registerDto.passWord(), null);
    }

    @Override
    public Auth fromLoginDto(LoginDto loginDto) {
        return new Auth(null, loginDto.userName(), loginDto.passWord(), null);
    }

}
