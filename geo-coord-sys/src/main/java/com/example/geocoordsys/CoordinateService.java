package com.example.geocoordsys;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

public interface CoordinateService {
    double degreesToRadians(double degrees);
    double centralAngle(@Valid Coordinate a, @Valid Coordinate b);
    double arcLength(@PositiveOrZero double radius, @Valid Coordinate a, @Valid Coordinate b);
    double greatCircleDistanceOnEarthBetween(@Valid Coordinate a, @Valid Coordinate b);
}
