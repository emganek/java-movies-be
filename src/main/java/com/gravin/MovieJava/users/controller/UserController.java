package com.gravin.MovieJava.users.controller;

import com.gravin.MovieJava.common.response.ApiResponse;
import com.gravin.MovieJava.common.response.PaginationData;
import com.gravin.MovieJava.users.domain.User;
import com.gravin.MovieJava.users.dto.*;
import com.gravin.MovieJava.users.mapper.UserMapper;
import com.gravin.MovieJava.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateUserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest req
    ) {
        User user = userService.createUser(req);

        return ResponseEntity.ok(ApiResponse.ok(userMapper.toCreateUserResponse(user)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<PaginationData<UserResponse>>> getUsers(
            @Valid @RequestBody GetUsersRequest req
    ) {
        PaginationData<User> paginationData = userService.getUsers(req);

        List<UserResponse> usersResponse = paginationData.content().stream()
                .map(userMapper::toUserResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(
                PaginationData.of(
                        usersResponse,
                        paginationData.page(),
                        paginationData.size(),
                        paginationData.totalElements()
                )
        ));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable String username) {
        UserResponse user = userMapper.toUserResponse(userService.getUser(username));

        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PutMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String username,
            @Valid @RequestBody UpdateUserRequest req
    ) {
        UserResponse user = userMapper.toUserResponse(userService.updateUser(username, req));

        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponse>> deleteUser(
            @PathVariable String username
    ) {
        userService.deleteUser(username);

        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/types")
    public ResponseEntity<ApiResponse<List<GetUseTypesResponse>>> getUserTypes() {
        List<GetUseTypesResponse> response = userService.getUserTypes()
                .stream()
                .map(GetUseTypesResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
