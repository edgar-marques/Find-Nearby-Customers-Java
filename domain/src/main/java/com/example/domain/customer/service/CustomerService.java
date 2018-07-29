package com.example.domain.customer.service;

import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface CustomerService {
    List<Customer> findCustomersWithinRange(@Valid @NotNull final List<Customer> customers,
                                            @Valid @NotNull final Coordinate location,
                                            @PositiveOrZero final double radius);
}
