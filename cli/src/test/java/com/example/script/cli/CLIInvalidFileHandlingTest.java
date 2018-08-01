package com.example.script.cli;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static com.example.script.OutputMatcher.matchesOutput;
import static org.junit.Assert.assertThat;

public class CLIInvalidFileHandlingTest {

    private static final String TEST_DATA_FILE = "/invalid_customers.txt";

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private Path inputFilePath;

    @Before
    public void setUp() throws IOException {
        // reset log level to default
        Configurator.setLevel("com.example", Level.INFO);

        // copy test data from classpath resource file into a temporary file
        InputStream resource = this.getClass().getResourceAsStream(TEST_DATA_FILE);
        inputFilePath = Files.createTempFile(null, null);
        Files.copy(resource, inputFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(inputFilePath);
    }

    @Test
    public void cli_shouldFilterOutInvalidData() {
        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "53.339428",
                "--long", "-6.257664"
        });

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "Customer(userId=4, name=Ian Kehoe, location=Coordinate(latitude=53.2451022, longitude=-6.238335))",
                "Customer(userId=5, name=Nora Dempsey, location=Coordinate(latitude=53.1302756, longitude=-6.2397222))",
                "Customer(userId=12, name=Christina McArdle, location=Coordinate(latitude=52.986375, longitude=-6.043701))"
        ));
    }

    @Test
    public void cli_shouldPrintOutInfoAboutInvalidDataWhenVerboseModeIsEnabled() {
        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "53.339428",
                "--long", "-6.257664",
                "-v"
        });

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "Verbose mode enabled",
                "an error was encountered while processing item ''",
                "No content to map due to end-of-input\n at [Source: (String)\"\"; line: 1, column: 0]",
                "skipping item",
                "invalid item 'Customer(userId=null, name=null, location=Coordinate(latitude=null, longitude=null))'",
                "latitude must not be null",
                "longitude must not be null",
                "name must not be blank",
                "user ID must not be null",
                "skipping item",
                "invalid item 'Customer(userId=null, name=null, location=Coordinate(latitude=null, longitude=null))'",
                "latitude must not be null",
                "longitude must not be null",
                "name must not be blank",
                "user ID must not be null",
                "skipping item",
                "invalid item 'Customer(userId=2, name= , location=Coordinate(latitude=90.1, longitude=180.1))'",
                "latitude must be between -90.0 and 90.0",
                "longitude must be between -180.0 and 180.0",
                "name must not be blank",
                "skipping item",
                "Customer(userId=4, name=Ian Kehoe, location=Coordinate(latitude=53.2451022, longitude=-6.238335))",
                "Customer(userId=5, name=Nora Dempsey, location=Coordinate(latitude=53.1302756, longitude=-6.2397222))",
                "Customer(userId=12, name=Christina McArdle, location=Coordinate(latitude=52.986375, longitude=-6.043701))"
        ));
    }
}
