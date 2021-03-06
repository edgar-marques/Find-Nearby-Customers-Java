package com.example.script.cdi;

import com.example.script.io.DefaultFileParser;
import com.example.script.io.FileParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

public class IOModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileParser.class).to(DefaultFileParser.class);
    }

    @Provides @Singleton
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
