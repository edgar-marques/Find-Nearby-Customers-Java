package com.example.domain.geocoord.service;

import com.example.domain.geocoord.model.Coordinate;
import com.example.domain.geocoord.service.DefaultCoordinateService;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Set;

import static com.example.domain.geocoord.TestUtils.ERROR;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.StrictMath.PI;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultCoordinateServiceTest {

    private static ExecutableValidator validator;

    @BeforeClass
    public static void setUpValidator() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .forExecutables();
    }

    private final DefaultCoordinateService coordService = new DefaultCoordinateService();

    @Test
    public void degreesToRadians_shouldReturnExpectedValues() {
        assertThat(coordService.degreesToRadians(0.0), is(closeTo(0.0, ERROR)));
        assertThat(coordService.degreesToRadians(1.0), is(closeTo(PI / 180, ERROR)));
        assertThat(coordService.degreesToRadians(180.0), is(closeTo(PI, ERROR)));
        assertThat(coordService.degreesToRadians(360.0), is(closeTo(2 * PI, ERROR)));
        assertThat(coordService.degreesToRadians(-1.0), is(closeTo(-PI / 180, ERROR)));
        assertThat(coordService.degreesToRadians(-180.0), is(closeTo(-PI, ERROR)));
        assertThat(coordService.degreesToRadians(-360.0), is(closeTo(-2 * PI, ERROR)));
        assertThat(coordService.degreesToRadians(POSITIVE_INFINITY), is(equalTo(POSITIVE_INFINITY)));
        assertThat(coordService.degreesToRadians(NEGATIVE_INFINITY), is(equalTo(NEGATIVE_INFINITY)));
        assertThat(coordService.degreesToRadians(NaN), is(equalTo(NaN)));
    }

    @Test
    public void centralAngle_shouldRequireValidCoordinates() throws Exception {
        // given
        Method centralAngle = coordService.getClass().getDeclaredMethod("centralAngle", Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* invalid coord a */ Coordinate.of("90.1", "0.0"),
                /* invalid coord b */ Coordinate.of("0.0", "180.1"),
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, centralAngle, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("centralAngle.a.latitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("90.1"))),
                        hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))
                )));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("centralAngle.b.longitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("180.1"))),
                        hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))
                )));
    }

    @Test
    public void centralAngle_shouldRequireNonNullCoordinates() throws Exception {
        // given
        Method centralAngle = coordService.getClass().getDeclaredMethod("centralAngle", Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* coord a */ null,
                /* coord b */ null,
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, centralAngle, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("centralAngle.a")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("centralAngle.b")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
    }

    @Test
    public void arcLength_shouldRequireValidCoordinates() throws Exception {
        // given
        Method arcLength = coordService.getClass().getDeclaredMethod("arcLength", double.class, Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* valid radius r  */ 1.0,
                /* invalid coord a */ Coordinate.of("90.1", "0.0"),
                /* invalid coord b */ Coordinate.of("0.0", "180.1"),
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, arcLength, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("arcLength.a.latitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("90.1"))),
                        hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))
                )));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("arcLength.b.longitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("180.1"))),
                        hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))
                )));
    }

    @Test
    public void arcLength_shouldRequireNonNullCoordinates() throws Exception {
        // given
        Method arcLength = coordService.getClass().getDeclaredMethod("arcLength", double.class, Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* radius r  */ 1.0,
                /* coord a */ null,
                /* coord b */ null,
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, arcLength, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("arcLength.a")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("arcLength.b")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
    }

    @Test
    public void arcLength_shouldRequireValidRadius() throws Exception {
        // given
        Method arcLength = coordService.getClass().getDeclaredMethod("arcLength", double.class, Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* valid radius r   */ -1.0,
                /* valid coord a    */ Coordinate.of("90.0", "0.0"),
                /* valid coord b    */ Coordinate.of("0.0", "180.0"),
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, arcLength, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("arcLength.radius")),
                        hasProperty("invalidValue", equalTo(-1.0)),
                        hasProperty("message", equalTo("must be greater than or equal to 0"))
                )));
    }

    @Test
    public void arcLength_shouldAcceptRadiusOfZero() throws Exception {
        // given
        Method arcLength = coordService.getClass().getDeclaredMethod("arcLength", double.class, Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* valid radius r   */ 0.0,
                /* valid coord a    */ Coordinate.of("90.0", "0.0"),
                /* valid coord b    */ Coordinate.of("0.0", "180.0"),
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, arcLength, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, is(empty()));
    }

    @Test
    public void arcLength_shouldAcceptPositiveRadius() throws Exception {
        // given
        Method arcLength = coordService.getClass().getDeclaredMethod("arcLength", double.class, Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* valid radius r   */ 0.1,
                /* valid coord a    */ Coordinate.of("90.0", "0.0"),
                /* valid coord b    */ Coordinate.of("0.0", "180.0"),
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, arcLength, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, is(empty()));
    }

    @Test
    public void greatCircleDistanceOnEarthBetween_shouldRequireValidCoordinates() throws Exception {
        // given
        Method greatCircleDistanceOnEarthBetween = coordService.getClass().getDeclaredMethod("greatCircleDistanceOnEarthBetween", Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* invalid coord a */ Coordinate.of("90.1", "0.0"),
                /* invalid coord b */ Coordinate.of("0.0", "180.1"),
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, greatCircleDistanceOnEarthBetween, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("greatCircleDistanceOnEarthBetween.a.latitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("90.1"))),
                        hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))
                )));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("greatCircleDistanceOnEarthBetween.b.longitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("180.1"))),
                        hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))
                )));
    }

    @Test
    public void greatCircleDistanceOnEarthBetween_shouldRequireNonNullCoordinates() throws Exception {
        // given
        Method greatCircleDistanceOnEarthBetween = coordService.getClass().getDeclaredMethod("greatCircleDistanceOnEarthBetween", Coordinate.class, Coordinate.class);
        Object[] invalidArgs = new Object[]{
                /* coord a */ null,
                /* coord b */ null,
        };

        // when
        Set<ConstraintViolation<DefaultCoordinateService>> violations = validator.validateParameters(coordService, greatCircleDistanceOnEarthBetween, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("greatCircleDistanceOnEarthBetween.a")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("greatCircleDistanceOnEarthBetween.b")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
    }
}
