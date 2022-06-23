package com.epam.esm.controller;

import com.epam.esm.dto.BaseResponse;
import com.epam.esm.dto.reponse.UserGetResponse;
import com.epam.esm.dto.request.UserPostRequest;
import com.epam.esm.exception.InvalidInputException;
import com.epam.esm.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<UserGetResponse>> create(
            @Valid @RequestBody UserPostRequest userPostRequest,
            BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
            throw new InvalidInputException(bindingResult);
        UserGetResponse createdUser = userService.create(userPostRequest);
        createdUser.add(linkTo(methodOn(OrderController.class)
                .getUserOrders(createdUser.getId(), 100, 0)).withRel("user orders"));
        return ResponseEntity.status(201).body(new BaseResponse<>(200, "user created", createdUser));
    }

    @GetMapping("/get")
    public ResponseEntity<BaseResponse<UserGetResponse>> get(
            @RequestParam Long id)
    {
        UserGetResponse response = userService.get(id);
        response.add(linkTo(methodOn(OrderController.class)
                .getUserOrders(response.getId(), 100, 0)).withRel("user orders"));
        return ResponseEntity.ok().body(new BaseResponse<>(200, "user details", response));
    }

    @GetMapping("/get_all")
    public ResponseEntity<BaseResponse<List<UserGetResponse>>> getAll(
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "0") int offset)
    {
        List<UserGetResponse> userList = userService.getAll(limit, offset);
        userList.forEach(user -> user.add(linkTo(methodOn(OrderController.class)
                .getUserOrders(user.getId(), 100, 0)).withRel("user orders")));
        return ResponseEntity.ok(new BaseResponse<>(200, "users list", userList));
    }

}