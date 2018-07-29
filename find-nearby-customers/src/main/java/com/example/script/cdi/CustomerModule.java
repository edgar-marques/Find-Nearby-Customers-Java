package com.example.script.cdi;

import com.example.domain.customer.service.CustomerService;
import com.example.domain.customer.service.DefaultCustomerService;
import com.google.inject.AbstractModule;

public class CustomerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CustomerService.class).to(DefaultCustomerService.class);
    }
}