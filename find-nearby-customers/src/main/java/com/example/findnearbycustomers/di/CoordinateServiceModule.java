package com.example.findnearbycustomers.di;

import com.example.geocoord.CoordinateService;
import com.example.geocoord.DefaultCoordinateService;
import com.google.inject.AbstractModule;

public class CoordinateServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CoordinateService.class).to(DefaultCoordinateService.class);
    }
}
