package com.example.domain.customer.dto;

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

public class CustomerRecordTest {

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
    public void customerRecord_shouldBeInvalid_whenUserIdIsNull() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(null, "John Smith", BigDecimal.ZERO, BigDecimal.ZERO);

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("user ID must not be null"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenNameIsNull() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, null, BigDecimal.ZERO, BigDecimal.ZERO);

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("name must not be blank"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenNameIsEmpty() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "", BigDecimal.ZERO, BigDecimal.ZERO);

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("name must not be blank"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenNameIsBlank() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, " \t\r\n", BigDecimal.ZERO, BigDecimal.ZERO);

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("name must not be blank"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenLatitudeIsNull() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", null, BigDecimal.ZERO);

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must not be null"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenLatitudeIsLessThanMinus90Degrees() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("-90.1"), new BigDecimal("0.0"));

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenLatitudeIsGreaterThan90Degrees() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("90.1"), new BigDecimal("0.0"));

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("latitude must be between -90.0 and 90.0"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenLongitudeIsNull() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", BigDecimal.ZERO, null);

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("longitude must not be null"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenLongitudeIsLessThanMinus180Degrees() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("-180.1"));

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))));
    }

    @Test
    public void customerRecord_shouldBeInvalid_whenLongitudeIsGreaterThan180Degrees() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("180.1"));

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, hasItem(
                hasProperty("message", equalTo("longitude must be between -180.0 and 180.0"))));
    }

    @Test
    public void customerRecord_shouldBeValid_whenLatitudeAndLongitudeAreWithinTheirValidRanges() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // when
        Set<ConstraintViolation<CustomerRecord>> violations = validator.validate(customerRecord);

        // then
        assertThat(violations, is(notNullValue()));
        assertThat(violations, is(empty()));
    }

    @Test
    public void customerRecord_shouldBeEqual_toItself() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, is(equalTo(customerRecord)));
    }

    @Test
    public void customerRecord_shouldBeEqual_toAnotherCustomerRecordWithSamePropertyValues() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, is(equalTo(anotherCustomerRecord)));
        assertThat(customerRecord, is(not(sameInstance(anotherCustomerRecord))));
    }

    @Test
    public void customerRecord_shouldNotBeEqual_toAnotherCustomerRecordWithDifferentUserId() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(2L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, is(not(equalTo(anotherCustomerRecord))));
    }

    @Test
    public void customerRecord_shouldNotBeEqual_toAnotherCustomerRecordWithDifferentName() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "Jane Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, is(not(equalTo(anotherCustomerRecord))));
    }

    @Test
    public void customerRecord_shouldNotBeEqual_toAnotherCustomerRecordWithDifferentLatitude() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("1.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, is(not(equalTo(anotherCustomerRecord))));
    }

    @Test
    public void customerRecord_shouldNotBeEqual_toAnotherCustomerRecordWithDifferentLongitude() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("1.0"));

        // then
        assertThat(customerRecord, is(not(equalTo(anotherCustomerRecord))));
    }

    @Test
    public void customerRecord_shouldNotBeEqual_toNullValue() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, is(not(equalTo(null))));
    }

    @Test
    public void customerRecord_shouldNotBeEqual_toObjectOfDifferentType() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        Object anotherCustomerRecord = "some object of different type";

        // then
        assertThat(customerRecord, is(not(equalTo(anotherCustomerRecord))));
        assertThat(anotherCustomerRecord, is(not(instanceOf(CustomerRecord.class))));
    }

    @Test
    public void customerRecord_shouldHaveSameHashCode_asAnotherCustomerRecordWithSamePropertyValues() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord.hashCode(), is(equalTo(anotherCustomerRecord.hashCode())));
        assertThat(customerRecord, is(not(sameInstance(anotherCustomerRecord))));
    }

    @Test
    public void customerRecord_shouldNotHaveSameHashCode_asAnotherCustomerRecordWithDifferentUserId() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(2L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord.hashCode(), is(not(equalTo(anotherCustomerRecord.hashCode()))));
    }

    @Test
    public void customerRecord_shouldNotHaveSameHashCode_asAnotherCustomerRecordWithDifferentName() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "Jane Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord.hashCode(), is(not(equalTo(anotherCustomerRecord.hashCode()))));
    }

    @Test
    public void customerRecord_shouldNotHaveSameHashCode_asAnotherCustomerRecordWithDifferentLatitude() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("1.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord.hashCode(), is(not(equalTo(anotherCustomerRecord.hashCode()))));
    }

    @Test
    public void customerRecord_shouldNotHaveSameHashCode_asAnotherCustomerRecordWithDifferentLongitude() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        CustomerRecord anotherCustomerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("1.0"));

        // then
        assertThat(customerRecord.hashCode(), is(not(equalTo(anotherCustomerRecord.hashCode()))));
    }

    @Test
    public void customerRecord_shouldPrintOutMeaningfulInfo() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // then
        assertThat(customerRecord, hasToString("CustomerRecord(userId=1, name=John Smith, latitude=0.0, longitude=0.0)"));
    }

    @Test
    public void customerRecord_userId_shouldBeAReadableAndWritableProperty() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // when
        boolean isReadable = PropertyUtils.isReadable(customerRecord, "userId");
        boolean isWriteable = PropertyUtils.isWriteable(customerRecord, "userId");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }

    @Test
    public void customerRecord_name_shouldBeAReadableAndWritableProperty() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // when
        boolean isReadable = PropertyUtils.isReadable(customerRecord, "name");
        boolean isWriteable = PropertyUtils.isWriteable(customerRecord, "name");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }

    @Test
    public void customerRecord_latitude_shouldBeAReadableAndWritableProperty() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // when
        boolean isReadable = PropertyUtils.isReadable(customerRecord, "latitude");
        boolean isWriteable = PropertyUtils.isWriteable(customerRecord, "latitude");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }

    @Test
    public void customerRecord_longitude_shouldBeAReadableAndWritableProperty() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // when
        boolean isReadable = PropertyUtils.isReadable(customerRecord, "longitude");
        boolean isWriteable = PropertyUtils.isWriteable(customerRecord, "longitude");

        // then
        assertThat(isReadable, is(true));
        assertThat(isWriteable, is(true));
    }
}
