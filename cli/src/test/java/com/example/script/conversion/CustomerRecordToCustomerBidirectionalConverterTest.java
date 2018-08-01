package com.example.script.conversion;

import com.example.domain.customer.dto.CustomerRecord;
import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.TypeFactory;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class CustomerRecordToCustomerBidirectionalConverterTest {

    private CustomerRecordToCustomerBidirectionalConverter converter = new CustomerRecordToCustomerBidirectionalConverter();

    @Test
    public void convertTo_shouldReturnCustomerWithExpectedPropertyValues() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));

        // when
        Customer customer = converter.convertTo(customerRecord, TypeFactory.valueOf(Customer.class), new MappingContext(null));

        // then
        assertThat(customer, is(notNullValue()));
        assertThat(customer, allOf(
                hasProperty("userId", equalTo(customerRecord.getUserId())),
                hasProperty("name", equalTo(customerRecord.getName())),
                hasProperty("location", allOf(
                    hasProperty("latitude", equalTo(customerRecord.getLatitude())),
                    hasProperty("longitude", equalTo(customerRecord.getLongitude()))
                )
        )));
    }

    @Test
    public void convertFrom_shouldReturnCustomerRecordWithExpectedPropertyValues() {
        // given
        Customer customer = new Customer(1L, "John Smith", Coordinate.of(new BigDecimal("0.0"), new BigDecimal("0.0")));

        // when
        CustomerRecord customerRecord = converter.convertFrom(customer, TypeFactory.valueOf(CustomerRecord.class), new MappingContext(null));

        // then
        assertThat(customerRecord, is(notNullValue()));
        assertThat(customerRecord, allOf(
                hasProperty("userId", equalTo(customer.getUserId())),
                hasProperty("name", equalTo(customer.getName())),
                hasProperty("latitude", equalTo(customer.getLocation().getLatitude())),
                hasProperty("longitude", equalTo(customer.getLocation().getLongitude()))
        ));
    }
}
