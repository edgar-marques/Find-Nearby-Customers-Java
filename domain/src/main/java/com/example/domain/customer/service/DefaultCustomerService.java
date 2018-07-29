package com.example.domain.customer.service;

import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;
import com.example.domain.geocoord.service.CoordinateService;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Log4j2
@Singleton
public class DefaultCustomerService implements CustomerService {

    private final CoordinateService coordService;

    /**
     *
     * @param coordService
     */
    @Inject
    public DefaultCustomerService(CoordinateService coordService) {
        this.coordService = coordService;
    }

    /**
     *
     * @param customers
     * @param location
     * @param radius
     * @return
     */
    @Override
    public List<Customer> findCustomersWithinRange(@Valid @NotNull final List<Customer> customers,
                                                   @Valid @NotNull final Coordinate location,
                                                   @PositiveOrZero final double radius) {
        return customers.stream()
                .filter(r -> {
                    double distance = coordService.greatCircleDistanceOnEarthBetween(
                            r.getLocation(),
                            location);
                    return distance <= radius;
                })
                .collect(Collectors.toList());
    }
}
