package com.example.findnearbycustomers;

import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CustomerRecord {

    @NotBlank(message = "userId must not be blank")
    private String userId;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "latitude must not be null")
    @DecimalMax("90.0")
    @DecimalMin("-90.0")
    BigDecimal latitude;

    @NotNull(message = "longitude must not be null")
    @DecimalMax("180.0")
    @DecimalMin("-180.0")
    BigDecimal longitude;
}
