package com.gravin.MovieJava.users.service;


import com.gravin.MovieJava.common.enums.UserType;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.CreateUserRequest;
import com.gravin.MovieJava.users.dto.GetUsersRequest;
import com.gravin.MovieJava.users.dto.UpdateUserRequest;

import java.util.List;

public interface UserService {
    List<UserType> getUserTypes();

    User createUser(CreateUserRequest req);

    User updateUser(String username, UpdateUserRequest req);

    PaginationData<User> getUsers(GetUsersRequest req);

    User getUser(String username);

    void deleteUser(String username);
}
