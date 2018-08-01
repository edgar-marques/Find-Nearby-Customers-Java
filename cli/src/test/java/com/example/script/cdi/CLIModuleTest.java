package com.example.script.cdi;

import com.example.script.cli.CLIArgsParser;
import com.example.script.cli.CLIArgsProcessor;
import com.example.script.cli.DefaultCLIArgsParser;
import com.example.script.cli.DefaultCLIArgsProcessor;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;
import ru.vyarus.guice.validator.ImplicitValidationModule;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

public class CLIModuleTest {

    private Injector injector = Guice.createInjector(
            new CLIModule(),
            new ConversionModule(),
            new CoordinateModule(),
            new CustomerModule(),
            new IOModule(),
            new ImplicitValidationModule()
    );

    @Test
    public void cliArgsParser_shouldBeBoundToExpectedImplementation() {
        // when
        CLIArgsParser cliArgsParser = injector.getInstance(CLIArgsParser.class);

        // then
        assertThat(cliArgsParser, is(instanceOf(DefaultCLIArgsParser.class)));
    }

    @Test
    public void cliArgsParser_shouldHaveSingletonScope() {
        // when
        CLIArgsParser cliArgsParser = injector.getInstance(CLIArgsParser.class);
        CLIArgsParser anotherCliArgsParser = injector.getInstance(CLIArgsParser.class);

        // then
        assertThat(cliArgsParser, is(sameInstance(anotherCliArgsParser)));
    }

    @Test
    public void cliArgsProcessor_shouldBeBoundToExpectedImplementation() {
        // when
        CLIArgsProcessor cliArgsProcessor = injector.getInstance(CLIArgsProcessor.class);

        // then
        assertThat(cliArgsProcessor, is(instanceOf(DefaultCLIArgsProcessor.class)));
    }

    @Test
    public void cliArgsProcessor_shouldHaveSingletonScope() {
        // when
        CLIArgsProcessor cliArgsProcessor = injector.getInstance(CLIArgsProcessor.class);
        CLIArgsProcessor anotherCliArgsProcessor = injector.getInstance(CLIArgsProcessor.class);

        // then
        assertThat(cliArgsProcessor, is(sameInstance(anotherCliArgsProcessor)));
    }
}
