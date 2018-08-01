package com.example.script.functional;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;

import static com.example.script.functional.Functions.safeCall;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class FunctionsTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Before
    public void setUp() {
        // set log level to DEBUG
        Configurator.setLevel("com.example", Level.DEBUG);
    }

    @Test
    public void safeCall_shouldReturnExpectedLambdaReturnValue() {
        // given
        Object someObject = "some object";
        Object defaultValue = "default value";

        FunctionWithException<Object, Object, Exception> identity = o -> o;

        // when
        Object returnValue = safeCall(identity, defaultValue).apply(someObject);

        // then
        assertThat(returnValue, is(sameInstance(someObject)));
        assertThat(systemOutRule.getLog(), not(containsString("skipping item")));
    }

    @Test
    public void safeCall_shouldReturnDefaultValue_whenLambdaThrowsConstraintViolationException() {
        // given
        Object someObject = "some object";
        Object defaultValue = "default value";

        FunctionWithException<Object, Object, Exception> identity = o -> {
            throw new ConstraintViolationException(new HashSet<>());
        };

        // when
        Object returnValue = safeCall(identity, defaultValue).apply(someObject);

        // then
        assertThat(returnValue, is(sameInstance(defaultValue)));
        assertThat(systemOutRule.getLog(), containsString("invalid item 'some object'"));
        assertThat(systemOutRule.getLog(), containsString("skipping item"));
    }

    @Test
    public void safeCall_shouldReturnDefaultValue_whenLambdaThrowsSomeOtherException() {
        // given
        Object someObject = "some object";
        Object defaultValue = "default value";

        FunctionWithException<Object, Object, Exception> identity = o -> {
            throw new IllegalArgumentException();
        };

        // when
        Object returnValue = safeCall(identity, defaultValue).apply(someObject);

        // then
        assertThat(returnValue, is(sameInstance(defaultValue)));
        assertThat(systemOutRule.getLog(), containsString("an error was encountered while processing item 'some object'"));
        assertThat(systemOutRule.getLog(), containsString("skipping item"));
    }

    @After
    public void tearDown() {
        // reset log level to default
        Configurator.setLevel("com.example", Level.INFO);
    }
}
