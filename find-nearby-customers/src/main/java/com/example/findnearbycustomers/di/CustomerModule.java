package com.example.findnearbycustomers.di;

import com.example.findnearbycustomers.customer.service.CustomerService;
import com.example.findnearbycustomers.customer.service.DefaultCustomerService;
import com.google.inject.AbstractModule;

public class CustomerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CustomerService.class).to(DefaultCustomerService.class);
    }
}