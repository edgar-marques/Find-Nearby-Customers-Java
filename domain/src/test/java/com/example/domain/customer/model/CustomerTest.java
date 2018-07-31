package com.example.domain.customer.model;

import com.example.domain.geocoord.model.Coordinate;
import org.apache.commons.beanutils.ConstructorUtils;
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

public class CustomerTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    public void customer_shouldProvideNoArgsConstructor() throws Exception {
        ConstructorUtils.invokeConstructor(Customer.class, new Object[]{});
    }

    @Test
    public void customer_shouldProvideAllArgsConstructor() throws Exception {
        ConstructorUtils.invokeConstructor(Customer.class, new Object[] { 1L, "name", Coordinate.of("0.0", "0.0") });
    }

    @Test
    public void customer_shouldBeInvalid_whenUserIdIsNull() {
        // given
        Customer customer = new Customer(null, "John Smith", Coordinate.of(BigDecimal.ZERO, BigDecimal.ZERO));

        // when
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("user ID must not be null"))));
    }

    @Test
    public void customer_shouldBeInvalid_whenNameIsNull() {
        // given
        Customer customer = new Customer(1L, null, Coordinate.of(BigDecimal.ZERO, BigDecimal.ZERO));

        // when
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("name must not be blank"))));
    }

    @Test
    public void customer_shouldBeInvalid_whenNameIsEmpty() {
        // given
        Customer customer = new Customer(1L, "", Coordinate.of(BigDecimal.ZERO, BigDecimal.ZERO));

        // when
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("name must not be blank"))));
    }

    @Test
    public void customer_shouldBeInvalid_whenNameIsBlank() {
        // given
        Customer customer = new Customer(1L, " \t\r\n", Coordinate.of(BigDecimal.ZERO, BigDecimal.ZERO));

        // when
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("name must not be blank"))));
    }

    @Test
    public void customer_shouldBeInvalid_whenLocationIsNull() {
        // given
        Customer customer = new Customer(1L, "John Smith", null);

        // when
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("location must not be null"))));
    }

    @Test
    public void customer_shouldBeInvalid_whenLocationIsInvalid() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("-90.1"), new BigDecimal("0.0")));

        // when
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))));
    }

    @Test
    public void customer_shouldBeEqual_toItself() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, is(equalTo(customer)));
    }

    @Test
    public void customer_shouldBeEqual_toAnotherCustomerWithSamePropertyValues() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, is(equalTo(anotherCustomer)));
        assertThat(customer, is(not(sameInstance(anotherCustomer))));
    }

    @Test
    public void customer_shouldNotBeEqual_toAnotherCustomerWithDifferentUserId() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(2L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, is(not(equalTo(anotherCustomer))));
    }

    @Test
    public void customer_shouldNotBeEqual_toAnotherCustomerWithDifferentName() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(1L, "Jane Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, is(not(equalTo(anotherCustomer))));
    }

    @Test
    public void customer_shouldNotBeEqual_toAnotherCustomerWithDifferentLocation() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("1.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, is(not(equalTo(anotherCustomer))));
    }

    @Test
    public void customer_shouldNotBeEqual_toNullValue() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, is(not(equalTo(null))));
    }

    @Test
    public void customer_shouldNotBeEqual_toObjectOfDifferentType() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Object anotherCustomer = "some object of different type";

        // then
        assertThat(customer, is(not(equalTo(anotherCustomer))));
        assertThat(anotherCustomer, is(not(instanceOf(Customer.class))));
    }

    @Test
    public void customer_shouldHaveSameHashCode_asAnotherCustomerWithSamePropertyValues() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer.hashCode(), is(equalTo(anotherCustomer.hashCode())));
        assertThat(customer, is(not(sameInstance(anotherCustomer))));
    }

    @Test
    public void customer_shouldNotHaveSameHashCode_asAnotherCustomerWithDifferentUserId() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(2L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer.hashCode(), is(not(equalTo(anotherCustomer.hashCode()))));
    }

    @Test
    public void customer_shouldNotHaveSameHashCode_asAnotherCustomerWithDifferentName() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(1L, "Jane Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer.hashCode(), is(not(equalTo(anotherCustomer.hashCode()))));
    }

    @Test
    public void customer_shouldNotHaveSameHashCode_asAnotherCustomerWithDifferentLocation() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));
        Customer anotherCustomer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("1.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer.hashCode(), is(not(equalTo(anotherCustomer.hashCode()))));
    }

    @Test
    public void customer_shouldPrintOutMeaningfulInfo() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // then
        assertThat(customer, hasToString("Customer(userId=1, name=John Smith, location=Coordinate(latitude=0.0, longitude=0.0))"));
    }

    @Test
    public void customer_userId_shouldBeAReadableAndWritableProperty() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // when
        boolean isReadable = PropertyUtils.isReadable(customer, "userId");
        boolean isWriteable = PropertyUtils.isWriteable(customer, "userId");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }

    @Test
    public void customer_name_shouldBeAReadableAndWritableProperty() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // when
        boolean isReadable = PropertyUtils.isReadable(customer, "name");
        boolean isWriteable = PropertyUtils.isWriteable(customer, "name");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }

    @Test
    public void customer_location_shouldBeAReadableAndWritableProperty() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // when
        boolean isReadable = PropertyUtils.isReadable(customer, "location");
        boolean isWriteable = PropertyUtils.isWriteable(customer, "location");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }
}
