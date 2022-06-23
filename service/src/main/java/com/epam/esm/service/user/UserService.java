package com.epam.esm.service.user;

import com.epam.esm.dto.reponse.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.service.base.BaseService;

import java.util.List;

public interface UserService extends BaseService<UserPostRequest, UserGetResponse> {
    List<UserGetResponse> getAll(int limit, int offset);
}
