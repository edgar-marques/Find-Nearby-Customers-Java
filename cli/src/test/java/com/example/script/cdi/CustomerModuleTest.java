package com.example.script.cdi;

import com.example.domain.customer.service.CustomerService;
import com.example.domain.customer.service.DefaultCustomerService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CustomerModuleTest {

    private Injector injector = Guice.createInjector(
            new CoordinateModule(),
            new CustomerModule()
    );

    @Test
    public void customerService_shouldBeBoundToExpectedImplementation() {
        // when
        CustomerService customerService = injector.getInstance(CustomerService.class);

        // then
        assertThat(customerService, is(instanceOf(DefaultCustomerService.class)));
    }

    @Test
    public void customerService_shouldHaveSingletonScope() {
        // when
        CustomerService customerService = injector.getInstance(CustomerService.class);
        CustomerService anotherCustomerService = injector.getInstance(CustomerService.class);

        // then
        assertThat(customerService, is(sameInstance(anotherCustomerService)));
    }
}
