package com.example.domain.customer.service;

import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 *
 */
interface CustomerService {
    /**
     *
     * @param customers
     * @param location
     * @param radius
     * @return
     */
    List<Customer> findCustomersWithinRange(@Valid @NotNull final List<Customer> customers,
                                            @Valid @NotNull final Coordinate location,
                                            @PositiveOrZero final double radius);
}
