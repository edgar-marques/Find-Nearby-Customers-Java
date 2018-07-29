package com.example.script.cdi;

import com.example.domain.geocoord.service.CoordinateService;
import com.example.domain.geocoord.service.DefaultCoordinateService;
import com.google.inject.AbstractModule;

public class CoordinateServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CoordinateService.class).to(DefaultCoordinateService.class);
    }
}
