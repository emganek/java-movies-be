package com.gravin.MovieJava.users.dto;

import com.gravin.MovieJava.common.request.FilterBaseRequest;
import lombok.Getter;

@Getter
public class GetUsersRequest extends FilterBaseRequest {
        private String fullName;
}

