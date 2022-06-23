package com.epam.esm.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserGetResponse extends RepresentationModel<UserGetResponse> {
    private Long id;
    private String name;
    private int age;
    private String username;
    private String password;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
}
