package com.epam.esm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserPostRequest {
    private String name;
    @Positive(message = "age must be positive")
    @Min(message = "user under 15 years old cannot use our system", value = 15L)
    private Integer age;
    @NotBlank(message = "username cannot be null or empty")
    @Size(message = "username should contain 5 to 30 characters", min = 5, max = 30)
    @Column(unique = true)
    private String username;
    @NotBlank(message = "password cannot be null or empty")
    @Size(message = "password should contain 8 to 30 characters", min = 8, max = 30)
    private String password;
}
