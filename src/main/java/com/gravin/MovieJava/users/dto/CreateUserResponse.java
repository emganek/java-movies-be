package com.gravin.MovieJava.users.dto;

import com.gravin.MovieJava.common.enums.UserType;

public record CreateUserResponse(
        String username,
        String fullName,
        String email,
        String phoneNumber,
        UserType userType
) {
}
