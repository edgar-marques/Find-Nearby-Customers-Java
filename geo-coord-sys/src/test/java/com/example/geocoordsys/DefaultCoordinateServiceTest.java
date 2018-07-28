package com.example.geocoordsys;

import org.junit.Test;

import static com.example.geocoordsys.TestUtils.ERROR;
import static java.lang.StrictMath.PI;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DefaultCoordinateServiceTest {

    private DefaultCoordinateService coordService = new DefaultCoordinateService();

    @Test
    public void degreesToRadians_shouldReturnExpectedValues() {
        assertThat(coordService.degreesToRadians(0.0), is(closeTo(0.0, ERROR)));
        assertThat(coordService.degreesToRadians(1.0), is(closeTo(PI / 180, ERROR)));
        assertThat(coordService.degreesToRadians(180.0), is(closeTo(PI, ERROR)));
        assertThat(coordService.degreesToRadians(360.0), is(closeTo(2 * PI, ERROR)));
        assertThat(coordService.degreesToRadians(-1.0), is(closeTo(-PI / 180, ERROR)));
        assertThat(coordService.degreesToRadians(-180.0), is(closeTo(-PI, ERROR)));
        assertThat(coordService.degreesToRadians(-360.0), is(closeTo(-2 * PI, ERROR)));
    }
}
