package com.example.script.cdi;

import com.example.script.io.DefaultFileParser;
import com.example.script.io.FileParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class IOModuleTest {

    private Injector injector = Guice.createInjector(new IOModule());

    @Test
    public void fileParser_shouldBeBoundToExpectedImplementation() {
        // when
        FileParser fileParser = injector.getInstance(FileParser.class);

        // then
        assertThat(fileParser, is(instanceOf(DefaultFileParser.class)));
    }

    @Test
    public void fileParser_shouldHaveSingletonScope() {
        // when
        FileParser fileParser = injector.getInstance(FileParser.class);
        FileParser anotherFileParser = injector.getInstance(FileParser.class);

        // then
        assertThat(fileParser, is(sameInstance(anotherFileParser)));
    }

    @Test
    public void module_shouldHaveAProviderForObjectMapper() {
        // when
        Provider<ObjectMapper> provider = injector.getProvider(ObjectMapper.class);

        // then
        assertThat(provider, is(notNullValue()));
    }

    @Test
    public void objectMapper_shouldBeConfiguredCorrectly() {
        // given
        ObjectMapper objectMapper = injector.getInstance(ObjectMapper.class);

        // when
        DeserializationConfig config = objectMapper.getDeserializationConfig();
        boolean failOnUnknownProperties = config.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // then
        assertThat(failOnUnknownProperties, is(false));
    }

    @Test
    public void objectMapper_shouldHaveSingletonScope() {
        // when
        ObjectMapper objectMapper = injector.getInstance(ObjectMapper.class);
        ObjectMapper anotherObjectMapper = injector.getInstance(ObjectMapper.class);

        // then
        assertThat(objectMapper, is(sameInstance(anotherObjectMapper)));
    }
}
