package com.gravin.MovieJava.users.repository.impl;

import com.gravin.MovieJava.common.repository.BaseCustomRepository;
import com.gravin.MovieJava.common.repository.QueryBuilder;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.GetUsersRequest;
import com.gravin.MovieJava.users.mapper.UserMapper;
import com.gravin.MovieJava.users.repository.UserCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl extends BaseCustomRepository implements UserCustomRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    private final UserMapper userMapper;

    public PaginationData<User> getUsers(GetUsersRequest req) {
        QueryBuilder queryBuilder = new QueryBuilder("SELECT u FROM User u WHERE 1=1");
        queryBuilder.addLike("u.fullName", "fullName", "%" + req.getFullName() + "%");

        return paginate(
                queryBuilder.getJpql().toString(),
                User.class,
                queryBuilder.getParams(),
                req.getPage(),
                req.getSize()
        );
    }
}
