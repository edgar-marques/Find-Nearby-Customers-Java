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

import static com.example.script.functional.Predicates.safeTest;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class PredicatesTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Before
    public void setUp() {
        // set log level to DEBUG
        Configurator.setLevel("com.example", Level.DEBUG);
    }

    @Test
    public void safeTest_shouldReturnExpectedLambdaReturnValue() {
        // given
        Object someObject = "some object";

        PredicateWithException<Object, Exception> alwaysTrue = o -> true;

        // when
        boolean outcome = safeTest(alwaysTrue, false).test(someObject);

        // then
        assertThat(outcome, is(true));
        assertThat(systemOutRule.getLog(), not(containsString("skipping item")));
    }

    @Test
    public void safeTest_shouldReturnDefaultValue_whenLambdaThrowsConstraintViolationException() {
        // given
        Object someObject = "some object";

        PredicateWithException<Object, Exception> alwaysFails = o -> {
            throw new ConstraintViolationException(new HashSet<>());
        };

        // when
        boolean outcome = safeTest(alwaysFails, false).test(someObject);

        // then
        assertThat(outcome, is(false));
        assertThat(systemOutRule.getLog(), containsString("invalid item 'some object'"));
        assertThat(systemOutRule.getLog(), containsString("skipping item"));
    }

    @Test
    public void safeTest_shouldReturnDefaultValue_whenLambdaThrowsSomeOtherException() {
        // given
        Object someObject = "some object";

        PredicateWithException<Object, Exception> alwaysFails = o -> {
            throw new IllegalArgumentException();
        };

        // when
        boolean outcome = safeTest(alwaysFails, false).test(someObject);

        // then
        assertThat(outcome, is(false));
        assertThat(systemOutRule.getLog(), containsString("an error was encountered while testing item 'some object'"));
        assertThat(systemOutRule.getLog(), containsString("skipping item"));
    }

    @After
    public void tearDown() {
        // reset log level to default
        Configurator.setLevel("com.example", Level.INFO);
    }
}
