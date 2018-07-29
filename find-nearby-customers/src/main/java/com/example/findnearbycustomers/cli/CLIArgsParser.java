package com.example.findnearbycustomers.cli;

import java.util.Optional;

public interface CLIArgsParser {
    Optional<CLIArgs> parse(String[] args);
    String usageInfo();
}
