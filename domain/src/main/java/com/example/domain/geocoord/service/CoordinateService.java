package com.example.domain.geocoord.service;

import com.example.domain.geocoord.model.Coordinate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 *
 */
public interface CoordinateService {
    /**
     *
     * @param degrees
     * @return
     */
    double degreesToRadians(double degrees);

    /**
     *
     * @param a
     * @param b
     * @return
     */
    double centralAngle(@Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b);

    /**
     *
     * @param radius
     * @param a
     * @param b
     * @return
     */
    double arcLength(@PositiveOrZero double radius, @Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b);

    /**
     *
     * @param a
     * @param b
     * @return
     */
    double greatCircleDistanceOnEarthBetween(@Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b);
}
