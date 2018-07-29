package com.example.script.cli;

import java.util.Optional;

/**
 *
 */
public interface CLIArgsParser {
    /**
     *
     * @param args
     * @return
     */
    Optional<CLIArgs> parse(String[] args);

    /**
     *
     * @return
     */
    String usageInfo();
}
