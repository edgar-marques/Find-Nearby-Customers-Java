package com.example.findnearbycustomers.di;

import com.example.findnearbycustomers.cli.CLIArgsParser;
import com.example.findnearbycustomers.cli.CLIArgsProcessor;
import com.example.findnearbycustomers.cli.DefaultCLIArgsParser;
import com.example.findnearbycustomers.cli.DefaultCLIArgsProcessor;
import com.google.inject.AbstractModule;

public class CLIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CLIArgsParser.class).to(DefaultCLIArgsParser.class);
        bind(CLIArgsProcessor.class).to(DefaultCLIArgsProcessor.class);
    }
}
