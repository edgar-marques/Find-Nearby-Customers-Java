package com.example.findnearbycustomers.cli;

import com.example.findnearbycustomers.di.CLIModule;
import com.example.findnearbycustomers.di.ConversionModule;
import com.example.findnearbycustomers.di.CoordinateServiceModule;
import com.example.findnearbycustomers.di.CustomerModule;
import com.example.findnearbycustomers.di.IOModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.log4j.Log4j2;
import ru.vyarus.guice.validator.ImplicitValidationModule;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

@Log4j2
public class CLI {

    private CLIArgsParser parser;
    private CLIArgsProcessor processor;

    public CLI(CLIArgsParser parser, CLIArgsProcessor processor) {
        this.parser = parser;
        this.processor = processor;
    }

    public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(
                    new CLIModule(),
                    new ConversionModule(),
                    new CoordinateServiceModule(),
                    new CustomerModule(),
                    new IOModule(),
                    new ImplicitValidationModule()
            );

            CLIArgsParser parser = injector.getInstance(CLIArgsParser.class);
            CLIArgsProcessor processor = injector.getInstance(CLIArgsProcessor.class);

            new CLI(parser, processor).handle(args);
        } catch (Exception e) {
            logErrorAndExit(e);
        }
    }

    static void logErrorAndExit(Exception e) {
        if (e instanceof ConstraintViolationException) {
            ((ConstraintViolationException) e).getConstraintViolations().stream()
                    .forEach(v -> log.error(v.getMessage()));
        } else {
            log.error(e.getMessage());
        }
        System.exit(1);
    }

    void handle(String[] args) {
        Optional<CLIArgs> cliArgs = parser.parse(args);

        if (!cliArgs.isPresent()) {
            displayHelp();
            return;
        }

        processor.process(cliArgs.get());
    }

    void displayHelp() {
        log.info(parser.usageInfo());
    }
}
