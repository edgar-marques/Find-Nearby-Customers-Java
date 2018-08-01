package com.example.script.conversion;

import com.example.domain.customer.dto.CustomerRecord;
import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class CustomerRecordToCustomerBidirectionalConverter extends BidirectionalConverter<CustomerRecord, Customer> {
    @Override
    public Customer convertTo(CustomerRecord customerRecord, Type<Customer> type, MappingContext mappingContext) {
        return new Customer(
                customerRecord.getUserId(),
                customerRecord.getName(),
                Coordinate.of(customerRecord.getLatitude(), customerRecord.getLongitude()));
    }

    @Override
    public CustomerRecord convertFrom(Customer customer, Type<CustomerRecord> type, MappingContext mappingContext) {
        return new CustomerRecord(
                customer.getUserId(),
                customer.getName(),
                customer.getLocation().getLatitude(),
                customer.getLocation().getLongitude());
    }
}
