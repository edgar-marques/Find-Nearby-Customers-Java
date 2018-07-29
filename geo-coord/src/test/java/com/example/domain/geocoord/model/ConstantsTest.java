package com.example.domain.geocoord.model;

import com.example.domain.geocoord.model.Constants;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.example.domain.geocoord.model.Constants.MEAN_EARTH_RADIUS;
import static com.example.domain.geocoord.TestUtils.ERROR;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ConstantsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void privateConstructor_shouldThrowInvocationTargetException_dueToUnsupportedOperationException() throws Exception {
        // given
        Constructor<Constants> noArgsConstructor = Constants.class.getDeclaredConstructor();
        noArgsConstructor.setAccessible(true);  // change visibility of private no-args constructor

        // then
        thrown.expect(InvocationTargetException.class);
        thrown.expectCause(instanceOf(UnsupportedOperationException.class));

        // when
        noArgsConstructor.newInstance();
    }

    @Test
    public void meanEarthRadius_shouldBeCloseToExpectedValue() {
        assertThat(MEAN_EARTH_RADIUS, is(closeTo(6_371_000.0, ERROR)));
    }
}
