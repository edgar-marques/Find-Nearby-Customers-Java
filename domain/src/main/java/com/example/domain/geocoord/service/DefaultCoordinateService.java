package com.example.domain.geocoord.service;

import com.example.domain.geocoord.model.Coordinate;
import lombok.extern.log4j.Log4j2;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static com.example.domain.geocoord.model.Constants.MEAN_EARTH_RADIUS;
import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.acos;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

@Log4j2
@ThreadSafe
@Singleton
public class DefaultCoordinateService implements CoordinateService {

    /**
     *
     * @param degrees
     * @return
     */
    @Override
    public double degreesToRadians(double degrees) {
        return degrees * PI / 180.0;
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    @Override
    public double centralAngle(@Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b) {
        double latA = degreesToRadians(a.getLatitude().doubleValue());
        double longA = degreesToRadians(a.getLongitude().doubleValue());

        double latB = degreesToRadians(b.getLatitude().doubleValue());
        double longB = degreesToRadians(b.getLongitude().doubleValue());

        double deltaLong = abs(longA - longB);

        return acos(sin(latA) * sin(latB) + cos(latA) * cos(latB) * cos(deltaLong));
    }

    /**
     *
     * @param radius
     * @param a
     * @param b
     * @return
     * @see <a href="https://en.wikipedia.org/wiki/Great-circle_distance">Great-circle distance</a>
     */
    @Override
    public double arcLength(@PositiveOrZero double radius, @Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b) {
        return radius * centralAngle(a, b);
    }

    /**
     *
     * @param a
     * @param b
     * @return
     * @see <a href="https://en.wikipedia.org/wiki/Great-circle_distance">Great-circle distance</a>
     */
    @Override
    public double greatCircleDistanceOnEarthBetween(@Valid @NotNull Coordinate a, @Valid @NotNull Coordinate b) {
        return arcLength(MEAN_EARTH_RADIUS, a, b);
    }
}
