package com.example.findnearbycustomers;

import com.example.geocoordsys.Coordinate;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Customer {

    @NotBlank(message = "userId must not be blank")
    private String userId;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "location must not be null")
    @Valid
    private Coordinate location;
}
