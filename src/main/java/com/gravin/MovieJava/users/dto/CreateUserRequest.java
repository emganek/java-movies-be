package com.gravin.MovieJava.users.dto;

import com.gravin.MovieJava.common.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotEmpty(message = "Username is required")
        String username,

        @NotEmpty(message = "Password is required")
        String password,

        @NotEmpty(message = "Full name is required")
        String fullName,

        @Email(message = "Invalid email")
        String email,

        @Pattern(regexp = "^\\\\+?[0-9]{7,15}$", message = "Invalid phone format")
        String phoneNumber,

        @NotNull
        UserType userType
) {}
