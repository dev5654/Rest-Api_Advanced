package com.epam.esm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderPostRequest {
    @NotNull(message = "user id cannot be null for order")
    private Long userId;
    @NotNull(message = "certificate id cannot be null for order")
    private Long certificateId;
}
