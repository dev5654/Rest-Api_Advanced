package com.epam.esm.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagPostRequest {
    @NotBlank(message = "tag name cannot be empty or null")
    private String name;
}
