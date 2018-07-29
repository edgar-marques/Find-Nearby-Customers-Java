package com.example.domain.geocoord.service;

import com.example.domain.geocoord.model.Coordinate;
import com.example.domain.geocoord.service.DefaultCoordinateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.example.domain.geocoord.model.Constants.MEAN_EARTH_RADIUS;
import static com.example.domain.geocoord.TestUtils.ERROR;
import static java.lang.StrictMath.PI;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class DefaultCoordinateServiceParameterizedTest {

    private DefaultCoordinateService coordService = new DefaultCoordinateService();

    @Parameterized.Parameters(name = "{index}: central angle({0}, {1})={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // given the same coordinate then the central angle should be 0
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "180.0"),
                        /* coord b */           Coordinate.of("0.0", "180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-180.0"),
                        /* coord b */           Coordinate.of("0.0", "-180.0"),
                        /* central angle */     0.0,
                },

                // given coordinates on the same meridian but 90 degrees apart then the central angle should be PI / 2
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("90.0", "0.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "180.0"),
                        /* coord b */           Coordinate.of("0.0", "180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "180.0"),
                        /* coord b */           Coordinate.of("90.0", "180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "180.0"),
                        /* coord b */           Coordinate.of("0.0", "180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "180.0"),
                        /* coord b */           Coordinate.of("-90.0", "180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "-180.0"),
                        /* coord b */           Coordinate.of("0.0", "-180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-180.0"),
                        /* coord b */           Coordinate.of("90.0", "-180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "-180.0"),
                        /* coord b */           Coordinate.of("0.0", "-180.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-180.0"),
                        /* coord b */           Coordinate.of("-90.0", "-180.0"),
                        /* central angle */     PI / 2,
                },

                // given coordinates on the same meridian but 180 degrees apart then the central angle should be PI
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "180.0"),
                        /* coord b */           Coordinate.of("-90.0", "180.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "-180.0"),
                        /* coord b */           Coordinate.of("-90.0", "-180.0"),
                        /* central angle */     PI,
                },

                // given coordinates on the equator but 90 degrees apart then the central angle should be PI / 2
                {
                        /* coord a */           Coordinate.of("0.0", "90.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "90.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-90.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "-90.0"),
                        /* central angle */     PI / 2,
                },

                // given coordinates on the equator but 180 degrees apart then the central angle should be PI
                {
                        /* coord a */           Coordinate.of("0.0", "180.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "180.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-180.0"),
                        /* coord b */           Coordinate.of("0.0", "0.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "0.0"),
                        /* coord b */           Coordinate.of("0.0", "-180.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "90.0"),
                        /* coord b */           Coordinate.of("0.0", "-90.0"),
                        /* central angle */     PI,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-90.0"),
                        /* coord b */           Coordinate.of("0.0", "90.0"),
                        /* central angle */     PI,
                },

                // given coordinates on the equator but more than 180 degrees apart then the central angle should be less than PI
                // (as the effective central angle is equal to (longA - longB - PI) if (longA - longB) > PI)
                {
                        /* coord a */           Coordinate.of("0.0", "135.0"),
                        /* coord b */           Coordinate.of("0.0", "-135.0"),
                        /* central angle */     PI / 2,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-135.0"),
                        /* coord b */           Coordinate.of("0.0", "135.0"),
                        /* central angle */     PI / 2,
                },

                // given coordinates on the equator but 360 degrees apart then the central angle should be 0
                // (as they effectively point to the same location)
                {
                        /* coord a */           Coordinate.of("0.0", "180.0"),
                        /* coord b */           Coordinate.of("0.0", "-180.0"),
                        /* central angle */     0,
                },
                {
                        /* coord a */           Coordinate.of("0.0", "-180.0"),
                        /* coord b */           Coordinate.of("0.0", "180.0"),
                        /* central angle */     0,
                },

                // given coordinates on the north pole then the central angle should be 0 no matter the longitude
                {
                        /* coord a */           Coordinate.of("90.0", "90.0"),
                        /* coord b */           Coordinate.of("90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("90.0", "90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "-90.0"),
                        /* coord b */           Coordinate.of("90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("90.0", "-90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "90.0"),
                        /* coord b */           Coordinate.of("90.0", "-90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "-90.0"),
                        /* coord b */           Coordinate.of("90.0", "90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "180.0"),
                        /* coord b */           Coordinate.of("90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("90.0", "180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "-180.0"),
                        /* coord b */           Coordinate.of("90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "0.0"),
                        /* coord b */           Coordinate.of("90.0", "-180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "180.0"),
                        /* coord b */           Coordinate.of("90.0", "-180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("90.0", "-180.0"),
                        /* coord b */           Coordinate.of("90.0", "180.0"),
                        /* central angle */     0.0,
                },

                // given coordinates on the south pole then the central angle should be 0 no matter the longitude
                {
                        /* coord a */           Coordinate.of("-90.0", "90.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "-90.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "-90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "90.0"),
                        /* coord b */           Coordinate.of("-90.0", "-90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "-90.0"),
                        /* coord b */           Coordinate.of("-90.0", "90.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "180.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "-180.0"),
                        /* coord b */           Coordinate.of("-90.0", "0.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "0.0"),
                        /* coord b */           Coordinate.of("-90.0", "-180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "180.0"),
                        /* coord b */           Coordinate.of("-90.0", "-180.0"),
                        /* central angle */     0.0,
                },
                {
                        /* coord a */           Coordinate.of("-90.0", "-180.0"),
                        /* coord b */           Coordinate.of("-90.0", "180.0"),
                        /* central angle */     0.0,
                },
        });
    }

    @Parameterized.Parameter // first data value (0) is default
    public Coordinate a;

    @Parameterized.Parameter(1)
    public Coordinate b;

    @Parameterized.Parameter(2)
    public double centralAngle;

    @Test
    public void centralAngle_shouldReturnExpectedValue() {
        assertThat(coordService.centralAngle(a, b), is(closeTo(centralAngle, ERROR)));
    }

    @Test
    public void arcLength_shouldReturnExpectedValue() {
        assertThat(coordService.arcLength(2.0, a, b), is(closeTo(2.0 * centralAngle, ERROR)));
    }

    @Test
    public void greatCircleDistanceOnEarthBetween_shouldReturnExpectedValue() {
        assertThat(coordService.greatCircleDistanceOnEarthBetween(a, b), is(closeTo(MEAN_EARTH_RADIUS * centralAngle, ERROR)));
    }
}
