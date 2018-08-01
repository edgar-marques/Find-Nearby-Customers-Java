package com.example.script.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DefaultFileParserTest {

    private static final String EOL = System.lineSeparator();
    private static final Joiner JOINER = Joiner.on(EOL);

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private ObjectMapper objectMapper;

    private DefaultFileParser fileParser;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        fileParser = new DefaultFileParser(objectMapper);
    }

    private File writeListToTempFile(List<?> items) throws IOException {
        byte[] bytes = JOINER.join(
                items.stream()
                        .map(Object::toString)
                        .collect(Collectors.toList())
        ).getBytes(Charsets.UTF_8);

        Path path = Files.createTempFile("test", null);
        Files.write(path, bytes);

        return path.toFile();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void parse_shouldFilterOutItemsThatAreInvalidOrThrowErrors() throws Exception {
        // given
        List<Integer> data = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        File file = writeListToTempFile(data);

        given(objectMapper.readValue(anyString(), any(Class.class))).willAnswer((Answer<Integer>) invocationOnMock -> {
            int val = Integer.valueOf((String) invocationOnMock.getArguments()[0]);
            boolean isEven = (val % 2) == 0;
            if (isEven) {
                throw new IllegalArgumentException(String.format("value '%d' is even", val));
            }
            return val;
        });

        // when
        List<Integer> parsedData = fileParser.parse(file, Integer.class);

        // then
        assertThat(parsedData, is(equalTo(Lists.newArrayList(1, 3, 5, 7, 9))));

        // clean up
        file.delete();
    }

    @Test
    public void parse_shouldRethrowErrorWrappedInARuntimeException() {
        // given
        File nonExistingFile = new File("does not exist");

        // then
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("does not exist (The system cannot find the file specified)");

        thrown.expectCause(is(instanceOf(FileNotFoundException.class)));
        thrown.expectCause(hasProperty("message", equalTo("does not exist (The system cannot find the file specified)")));

        // when
        fileParser.parse(nonExistingFile, Integer.class);
    }
}
