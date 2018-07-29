package com.example.findnearbycustomers.io;

import com.example.findnearbycustomers.io.validation.RegularFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;

public interface FileParser {
    <T> List<T> parse(@RegularFile @NotNull File file, @NotNull Class<T> valueType);
}
