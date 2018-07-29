package com.example.domain.customer.model;

import com.example.domain.geocoord.model.Coordinate;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 */
@Data
@NoArgsConstructor
public class Customer {

    /**
     *
     */
    @NotNull(message = "user ID must not be null")
    private Long userId;

    /**
     *
     */
    @NotBlank(message = "name must not be blank")
    private String name;

    /**
     *
     */
    @NotNull(message = "location must not be null")
    @Valid
    private Coordinate location;

    /**
     *
     * @param userId
     * @param name
     * @param location
     */
    public Customer(Long userId, String name, Coordinate location) {
        this.userId = userId;
        this.name = name;
        this.location = location;
    }
}
