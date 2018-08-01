package com.example.script.cdi;

import com.example.script.conversion.CustomerRecordToCustomerBidirectionalConverter;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.inject.Singleton;

public class ConversionModule extends AbstractModule {

    @Provides @Singleton
    MapperFacade provideMapper() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(new CustomerRecordToCustomerBidirectionalConverter());
        return mapperFactory.getMapperFacade();
    }
}
