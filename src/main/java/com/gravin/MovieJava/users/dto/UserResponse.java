package com.gravin.MovieJava.users.dto;

import com.gravin.MovieJava.common.enums.UserType;

public record UserResponse(
        String username,
        String password,
        String fullName,
        String email,
        String phoneNumber,
        UserType userType
){}
