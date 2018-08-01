package com.example.script;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.stripEnd;

public class OutputMatcher extends TypeSafeMatcher<String> {

    private static final String EOL = System.lineSeparator();
    private static final Joiner JOINER = Joiner.on("");
    private static final Splitter SPLITTER = Splitter.on(EOL);
    private static final String STRIP_CHARS = null; // strip whitespace chars

    private final String expectedOutput;

    private OutputMatcher(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }

    public static OutputMatcher matchesOutput(String... lines) {
        String expectedOutput = JOINER.join(Arrays.stream(lines)
                .map(s -> stripEnd(s, STRIP_CHARS) + EOL)
                .toArray());
        expectedOutput = stripEnd(expectedOutput, STRIP_CHARS);
        return new OutputMatcher(expectedOutput);
    }

    @Override
    protected boolean matchesSafely(String actualOutput) {
        String strippedOutput = JOINER.join(SPLITTER.splitToList(actualOutput)
                .stream()
                .map(s -> stripEnd(s, STRIP_CHARS) + EOL)
                .toArray());
        strippedOutput = stripEnd(strippedOutput, STRIP_CHARS);
        return expectedOutput.equals(strippedOutput);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expectedOutput);
    }
}
