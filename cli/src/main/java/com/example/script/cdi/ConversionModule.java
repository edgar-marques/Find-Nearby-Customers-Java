package com.example.script.cdi;

import com.example.domain.customer.dto.CustomerRecord;
import com.example.domain.customer.model.Customer;
import com.example.domain.geocoord.model.Coordinate;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import javax.inject.Singleton;

public class ConversionModule extends AbstractModule {

    @Provides @Singleton
    MapperFacade provideMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(new BidirectionalConverter<CustomerRecord, Customer>() {
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
        });
        return mapperFactory.getMapperFacade();
    }
}
