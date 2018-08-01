package com.example.script.cdi;

import com.example.domain.customer.dto.CustomerRecord;
import com.example.domain.customer.model.Customer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class ConversionModuleTest {

    private Injector injector = Guice.createInjector(new ConversionModule());

    @Test
    public void module_shouldHaveAProviderForMapperFacade() {
        // when
        Provider<MapperFacade> provider = injector.getProvider(MapperFacade.class);

        // then
        assertThat(provider, is(notNullValue()));
    }

    @Test
    public void mapperFacade_shouldBeConfiguredCorrectly() {
        // given
        CustomerRecord customerRecord = new CustomerRecord(1L, "John Smith", new BigDecimal("0.0"), new BigDecimal("0.0"));
        MapperFacade mapperFacade = injector.getInstance(MapperFacade.class);

        // when
        Customer customer = mapperFacade.map(customerRecord, Customer.class);

        // then
        assertThat(customer, is(notNullValue()));
    }

    @Test
    public void mapperFacade_shouldHaveSingletonScope() {
        // when
        MapperFacade mapperFacade = injector.getInstance(MapperFacade.class);
        MapperFacade anotherMapperFacade = injector.getInstance(MapperFacade.class);

        // then
        assertThat(mapperFacade, is(sameInstance(anotherMapperFacade)));
    }
}
