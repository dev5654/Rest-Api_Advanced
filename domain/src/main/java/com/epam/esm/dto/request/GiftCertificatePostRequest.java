package com.epam.esm.dto.request;


import com.epam.esm.entity.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GiftCertificatePostRequest {
    @NotBlank(message = "name can't be null or empty")
    private String name;
    private String description;
    @DecimalMin(message = "gift certificate price cannot be negative", value = "0")
    private BigDecimal price;
    @Positive(message = "duration should be positive")
    private Integer duration;
    private List<TagEntity> tagEntities;

}
