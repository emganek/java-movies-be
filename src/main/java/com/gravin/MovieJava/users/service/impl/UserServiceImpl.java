package com.gravin.MovieJava.users.service.impl;

import com.gravin.MovieJava.common.enums.ErrorCode;
import com.gravin.MovieJava.common.enums.UserType;
import com.gravin.MovieJava.common.exception.AppException;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.CreateUserRequest;
import com.gravin.MovieJava.users.dto.GetUsersRequest;
import com.gravin.MovieJava.users.dto.UpdateUserRequest;
import com.gravin.MovieJava.users.mapper.UserMapper;
import com.gravin.MovieJava.users.repository.UserRepository;
import com.gravin.MovieJava.users.repository.impl.UserCustomRepositoryImpl;
import com.gravin.MovieJava.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final UserCustomRepositoryImpl userCustomRepo;

    private final UserMapper userMapper;

    public List<UserType> getUserTypes() {
        return List.of(UserType.values());
    }

    @Override
    public User createUser(CreateUserRequest req) {
        if (userRepo.existsByUsername(req.username())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toEntity(req);

        return userRepo.save(user);
    }

    @Override
    public User updateUser(String username, UpdateUserRequest req) {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        user.setPassword(req.password());
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPhoneNumber(req.phoneNumber());
        user.setUserType(req.userType());

        return userRepo.save(user);
    }

    @Override
    public PaginationData<User> getUsers(GetUsersRequest req) {
        return userCustomRepo.getUsers(req);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        userRepo.delete(user);
    }
}
