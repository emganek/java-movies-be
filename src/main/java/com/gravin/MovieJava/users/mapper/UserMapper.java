package com.gravin.MovieJava.users.mapper;

import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.CreateUserRequest;
import com.gravin.MovieJava.users.dto.CreateUserResponse;
import com.gravin.MovieJava.users.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(CreateUserRequest req);

    CreateUserResponse toCreateUserResponse(User user);

    UserResponse toUserResponse(User user);
}
