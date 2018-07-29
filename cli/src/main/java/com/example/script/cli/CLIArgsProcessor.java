package com.example.script.cli;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 */
public interface CLIArgsProcessor {
    /**
     *
     * @param args
     */
    void process(@Valid @NotNull CLIArgs args);
}
