package com.example.script.cdi;

import com.example.domain.geocoord.service.CoordinateService;
import com.example.domain.geocoord.service.DefaultCoordinateService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CoordinateModuleTest {

    private Injector injector = Guice.createInjector(new CoordinateModule());

    @Test
    public void coordinateService_shouldBeBoundToExpectedImplementation() {
        // when
        CoordinateService coordinateService = injector.getInstance(CoordinateService.class);

        // then
        assertThat(coordinateService, is(instanceOf(DefaultCoordinateService.class)));
    }

    @Test
    public void coordinateService_shouldHaveSingletonScope() {
        // when
        CoordinateService coordinateService = injector.getInstance(CoordinateService.class);
        CoordinateService anotherCoordinateService = injector.getInstance(CoordinateService.class);

        // then
        assertThat(coordinateService, is(sameInstance(anotherCoordinateService)));
    }
}
