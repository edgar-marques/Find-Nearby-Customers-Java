package com.example.domain.customer.service;

import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;
import com.example.domain.geocoord.service.CoordinateService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Set;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class DefaultCustomerServiceTest {

    private static ExecutableValidator validator;

    @BeforeClass
    public static void setUpValidator() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator()
                .forExecutables();
    }

    private CoordinateService coordService;

    private DefaultCustomerService customerService;

    @Before
    public void setUp() {
        // init mocks
        coordService = mock(CoordinateService.class);

        // init component under test
        customerService = new DefaultCustomerService(coordService);
    }

    @Test
    public void isCustomerWithinRange_shouldReturnTrue_whenCustomerLocationIsWithinSpecifiedRadiusOfGivenLocation() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of("0.0", "0.0"));
        Coordinate location = Coordinate.of("0.0", "0.0"); // actual location doesn't matter since the mock is instrumented
        double radius = 100_000; // 100 km

        given(coordService.greatCircleDistanceOnEarthBetween(any(Coordinate.class), any(Coordinate.class))).willReturn(radius);

        // when
        boolean withinRange = customerService.isCustomerWithinRange(customer, location, radius);

        // then
        assertThat(withinRange, is(true));
    }

    @Test
    public void isCustomerWithinRange_shouldReturnFalse_whenCustomerLocationIsOutsideSpecifiedRadiusOfGivenLocation() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of("0.0", "0.0"));
        Coordinate location = Coordinate.of("0.0", "0.0"); // actual location doesn't matter since the mock is instrumented
        double radius = 100_000; // 100 km

        given(coordService.greatCircleDistanceOnEarthBetween(any(Coordinate.class), any(Coordinate.class))).willReturn(radius + 1);

        // when
        boolean withinRange = customerService.isCustomerWithinRange(customer, location, radius);

        // then
        assertThat(withinRange, is(false));
    }

    @Test
    public void isCustomerWithinRange_shouldRequireNonNullCustomer() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] invalidArgs = new Object[]{
                /* invalid customer */ null,
                /* valid location   */ Coordinate.of("0.0", "0.0"),
                /* valid radius r   */ 1.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("isCustomerWithinRange.customer")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
    }

    @Test
    public void isCustomerWithinRange_shouldRequireValidCustomer() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] invalidArgs = new Object[]{
                /* invalid customer */ new Customer(null, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0"))),
                /* valid location   */ Coordinate.of("0.0", "0.0"),
                /* valid radius r   */ 1.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("isCustomerWithinRange.customer.userId")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("user ID must not be null"))
                )));
    }

    @Test
    public void isCustomerWithinRange_shouldRequireNonNullLocation() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] invalidArgs = new Object[]{
                /* valid customer   */ new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0"))),
                /* invalid location */ null,
                /* valid radius r   */ 1.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("isCustomerWithinRange.location")),
                        hasProperty("invalidValue", nullValue()),
                        hasProperty("message", equalTo("must not be null"))
                )));
    }

    @Test
    public void isCustomerWithinRange_shouldRequireValidLocation() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] invalidArgs = new Object[]{
                /* valid customer   */ new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0"))),
                /* invalid location */ Coordinate.of("90.1", "0.0"),
                /* valid radius r   */ 1.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("isCustomerWithinRange.location.latitude")),
                        hasProperty("invalidValue", equalTo(new BigDecimal("90.1"))),
                        hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))
                )));
    }

    @Test
    public void isCustomerWithinRange_shouldRequireValidRadius() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] invalidArgs = new Object[]{
                /* valid customer   */ new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0"))),
                /* valid location   */ Coordinate.of("0.0", "0.0"),
                /* invalid radius r */ -1.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, invalidArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                allOf(
                        hasProperty("propertyPath", hasToString("isCustomerWithinRange.radius")),
                        hasProperty("invalidValue", equalTo(-1.0)),
                        hasProperty("message", equalTo("must be greater than or equal to 0"))
                )));
    }

    @Test
    public void isCustomerWithinRange_shouldAcceptRadiusOfZero() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] validArgs = new Object[]{
                /* valid customer   */ new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0"))),
                /* valid location   */ Coordinate.of("0.0", "0.0"),
                /* valid radius r   */ 0.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, validArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, is(empty()));
    }

    @Test
    public void isCustomerWithinRange_shouldAcceptPositiveRadius() throws Exception {
        // given
        Method isCustomerWithinRange = customerService.getClass().getDeclaredMethod("isCustomerWithinRange", Customer.class, Coordinate.class, double.class);
        Object[] validArgs = new Object[]{
                /* valid customer   */ new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0"))),
                /* valid location   */ Coordinate.of("0.0", "0.0"),
                /* valid radius r   */ 1.0
        };

        // when
        Set<ConstraintViolation<DefaultCustomerService>> violations = validator.validateParameters(customerService, isCustomerWithinRange, validArgs);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, is(empty()));
    }
}
