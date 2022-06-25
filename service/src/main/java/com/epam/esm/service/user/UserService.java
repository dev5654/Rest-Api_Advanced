package com.epam.esm.service.user;

import com.epam.esm.dto.reponse.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;

import java.util.List;

public interface UserService /*extends BaseService<UserPostRequest, UserGetResponse>*/ {
    UserGetResponse create(UserPostRequest p);

    UserGetResponse get(Long id);

    /*int delete(Long id);*/

    List<UserGetResponse> getAll(int limit, int offset);
}
