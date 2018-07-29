package com.example.domain.geocoord.service;

import com.example.domain.geocoord.model.Coordinate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public interface CoordinateService {
    double degreesToRadians(double degrees);
    double centralAngle(@Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b);
    double arcLength(@PositiveOrZero double radius, @Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b);
    double greatCircleDistanceOnEarthBetween(@Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b);
}
