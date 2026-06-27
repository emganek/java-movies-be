package com.gravin.MovieJava.users.dto;

import com.gravin.MovieJava.common.enums.UserType;

public record GetUseTypesResponse(
        int value,
        String label
) {
    public static GetUseTypesResponse from (UserType userType) {
        return new GetUseTypesResponse(userType.getValue(), userType.getLabel());
    }
}
