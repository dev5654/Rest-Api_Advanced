package com.epam.esm.service.utils;

import com.epam.esm.dto.reponse.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.entity.UserEntity;

public class UserServiceTestUtils {

    public static UserPostRequest getUserPostRequest() {
        UserPostRequest userPostRequest = new UserPostRequest();
        userPostRequest.setAge(20);
        userPostRequest.setUsername("username");
        userPostRequest.setPassword("password");
        return userPostRequest;
    }

    public static UserEntity getUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(20);
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        return userEntity;
    }

    public static UserGetResponse getUserResponse() {
        UserGetResponse userGetResponse = new UserGetResponse();
        userGetResponse.setAge(20);
        userGetResponse.setUsername("username");
        userGetResponse.setPassword("password");
        return userGetResponse;
    }
}
