package com.example.script.io;

import com.example.script.validation.RegularFile;
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
import java.util.stream.Collectors;

import static com.example.script.functional.Functions.safeCall;

/**
 *
 */
@Log4j2
@Singleton
public class DefaultFileParser implements FileParser {

    private final ObjectMapper objectMapper;

    /**
     *
     * @param objectMapper
     */
    @Inject
    public DefaultFileParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param file
     * @param valueType
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> parse(@RegularFile @NotNull File file, @NotNull Class<T> valueType) {
        try {
            List<String> lines = Files.readLines(file, Charsets.UTF_8);
            return lines.stream()
                    .map(safeCall(l -> objectMapper.readValue(l, valueType), null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
