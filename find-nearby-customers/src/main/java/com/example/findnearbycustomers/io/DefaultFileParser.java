package com.example.findnearbycustomers.io;

import com.example.findnearbycustomers.io.validation.RegularFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @see <a href="https://www.oreilly.com/ideas/handling-checked-exceptions-in-java-streams">Handling checked exceptions in Java streams</a>
 */
@Log4j2
@Singleton
public class DefaultFileParser implements FileParser {

    private ObjectMapper objectMapper;

    @Inject
    public DefaultFileParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> List<T> parse(@RegularFile @NotNull File file, @NotNull Class<T> valueType) {
        try {
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            return lines.stream()
                    .map(wrapper(l -> objectMapper.readValue(l, valueType)))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    interface FunctionWithException<T, R, E extends Throwable> {
        R apply(T t) throws E;
    }

    private <T, R, E extends Exception>
    Function<T, R> wrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        };
    }
}
