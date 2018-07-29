package com.example.script.cdi;

import com.example.script.cli.CLIArgsParser;
import com.example.script.cli.CLIArgsProcessor;
import com.example.script.cli.DefaultCLIArgsParser;
import com.example.script.cli.DefaultCLIArgsProcessor;
import com.google.inject.AbstractModule;

public class CLIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CLIArgsParser.class).to(DefaultCLIArgsParser.class);
        bind(CLIArgsProcessor.class).to(DefaultCLIArgsProcessor.class);
    }
}
