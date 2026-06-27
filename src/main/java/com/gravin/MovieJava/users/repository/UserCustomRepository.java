package com.gravin.MovieJava.users.repository;

import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.GetUsersRequest;


public interface UserCustomRepository {
    PaginationData<User> getUsers(GetUsersRequest req);

}
