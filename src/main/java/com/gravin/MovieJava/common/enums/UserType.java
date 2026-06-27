package com.gravin.MovieJava.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gravin.MovieJava.users.domain.User;

import java.util.Arrays;

public enum UserType {
    ADMIN(1, "Admin"),

    MEMBER(2, "Member"),

    GUEST(3, "Guest");

    private final int value;

    private final String label;

    UserType(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @JsonCreator
    public static UserType fromValue(String value) {
        return Arrays.stream(UserType.values())
                .filter(type -> type.getValue() == Integer.parseInt(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User type is not valid"));
    }
}
