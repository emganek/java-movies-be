package com.gravin.MovieJava.common.converter;

import com.gravin.MovieJava.common.enums.UserType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserType userType) {
        return userType == null ? null : userType.getValue();
    }

    @Override
    public UserType convertToEntityAttribute(Integer integer) {
        return Arrays.stream(UserType.class.getEnumConstants())
                .filter(type -> type.getValue() == integer)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User type is invalid"));
    }
}
