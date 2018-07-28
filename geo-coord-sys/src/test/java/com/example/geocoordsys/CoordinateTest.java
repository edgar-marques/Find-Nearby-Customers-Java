package com.example.geocoordsys;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CoordinateTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void factoryMethod_shouldThrowNullPointerException_whenLatitudeIsNull() {
        thrown.expect(NullPointerException.class);

        Coordinate.of(null, "0.0");
    }

    @Test
    public void factoryMethod_shouldThrowNumberFormatException_whenLatitudeIsNotANumber() {
        thrown.expect(NumberFormatException.class);

        Coordinate.of("NaN", "0.0");
    }

    @Test
    public void factoryMethod_shouldThrowNullPointerException_whenLongitudeIsNull() {
        thrown.expect(NullPointerException.class);

        Coordinate.of("0.0", null);
    }

    @Test
    public void factoryMethod_shouldThrowNumberFormatException_whenLongitudeIsNotANumber() {
        thrown.expect(NumberFormatException.class);

        Coordinate.of("0.0", "NaN");
    }

    @Test
    public void coordinate_shouldBeInvalid_whenLatitudeIsNull() {
        // given
        Coordinate coord = Coordinate.of(null, BigDecimal.ZERO);

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must not be null"))));
    }

    @Test
    public void coordinate_shouldBeInvalid_whenLatitudeIsLessThanMinus90Degrees() {
        // given
        Coordinate coord = Coordinate.of("-90.1", "0.0");

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))));
    }

    @Test
    public void coordinate_shouldBeInvalid_whenLatitudeIsGreaterThan90Degrees() {
        // given
        Coordinate coord = Coordinate.of("90.1", "0.0");

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))));
    }

    @Test
    public void coordinate_shouldBeInvalid_whenLongitudeIsNull() {
        // given
        Coordinate coord = Coordinate.of(BigDecimal.ZERO, null);

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("longitude must not be null"))));
    }

    @Test
    public void coordinate_shouldBeInvalid_whenLongitudeIsLessThanMinus180Degrees() {
        // given
        Coordinate coord = Coordinate.of("0.0", "-180.1");

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))));
    }

    @Test
    public void coordinate_shouldBeInvalid_whenLongitudeIsGreaterThan180Degrees() {
        // given
        Coordinate coord = Coordinate.of("0.0", "180.1");

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))));
    }

    @Test
    public void coordinate_shouldBeValid_whenLatitudeAndLongitudeAreWithinTheirValidRanges() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");

        // when
        Set<ConstraintViolation<Coordinate>> violations = validator.validate(coord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, is(empty()));
    }

    @Test
    public void coordinate_shouldBeEqual_toItself() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");

        // then
        assertThat(coord, is(equalTo(coord)));
    }

    @Test
    public void coordinate_shouldBeEqual_toAnotherCoordinateWithSameLatitudeAndLongitude() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Coordinate anotherCoord = Coordinate.of("0.0", "0.0");

        // then
        assertThat(coord, is(equalTo(anotherCoord)));
        assertThat(coord, is(not(sameInstance(anotherCoord))));
    }

    @Test
    public void coordinate_shouldNotBeEqual_toAnotherCoordinateWithSameLatitudeButDifferentLongitude() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Coordinate anotherCoord = Coordinate.of("0.0", "90.0");

        // then
        assertThat(coord, is(not(equalTo(anotherCoord))));
    }

    @Test
    public void coordinate_shouldNotBeEqual_toAnotherCoordinateWithDifferentLatitudeButSameLongitude() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Coordinate anotherCoord = Coordinate.of("90.0", "0.0");

        // then
        assertThat(coord, is(not(equalTo(anotherCoord))));
    }

    @Test
    public void coordinate_shouldNotBeEqual_toNullValue() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");

        // then
        assertThat(coord, is(not(equalTo(null))));
    }

    @Test
    public void coordinate_shouldNotBeEqual_toObjectOfDifferentType() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Object anotherCoord = "some object of different type";

        // then
        assertThat(coord, is(not(equalTo(anotherCoord))));
        assertThat(anotherCoord, is(not(instanceOf(Coordinate.class))));
    }

    @Test
    public void coordinate_shouldHaveSameHashCode_asAnotherCoordinateWithSameLatitudeAndLongitude() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Coordinate anotherCoord = Coordinate.of("0.0", "0.0");

        // then
        assertThat(coord.hashCode(), is(equalTo(anotherCoord.hashCode())));
        assertThat(coord, is(not(sameInstance(anotherCoord))));
    }

    @Test
    public void coordinate_shouldNotHaveSameHashCode_asAnotherCoordinateWithSameLatitudeButDifferentLongitude() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Coordinate anotherCoord = Coordinate.of("0.0", "90.0");

        // then
        assertThat(coord.hashCode(), is(not(equalTo(anotherCoord.hashCode()))));
    }

    @Test
    public void coordinate_shouldNotHaveSameHashCode_asAnotherCoordinateWithDifferentLatitudeButSameLongitude() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");
        Coordinate anotherCoord = Coordinate.of("90.0", "0.0");

        // then
        assertThat(coord.hashCode(), is(not(equalTo(anotherCoord.hashCode()))));
    }

    @Test
    public void coordinate_shouldPrintOutMeaningfulInfo() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");

        // then
        assertThat(coord, hasToString("Coordinate(latitude=0.0, longitude=0.0)"));
    }

    @Test
    public void coordinate_latitude_shouldBeAReadOnlyProperty() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");

        // when
        boolean isReadable = PropertyUtils.isReadable(coord, "latitude");
        boolean isWriteable = PropertyUtils.isWriteable(coord, "latitude");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(false));
    }

    @Test
    public void coordinate_longitude_shouldBeAReadOnlyProperty() {
        // given
        Coordinate coord = Coordinate.of("0.0", "0.0");

        // when
        boolean isReadable = PropertyUtils.isReadable(coord, "longitude");
        boolean isWriteable = PropertyUtils.isWriteable(coord, "longitude");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(false));
    }
}
