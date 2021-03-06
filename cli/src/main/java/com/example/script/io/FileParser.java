package com.example.script.io;

import com.example.script.validation.RegularFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

/**
 *
 */
public interface FileParser {
    /**
     *
     * @param file
     * @param valueType
     * @param <T>
     * @return
     */
    <T> List<T> parse(@RegularFile @NotNull File file, @NotNull Class<T> valueType);
}
