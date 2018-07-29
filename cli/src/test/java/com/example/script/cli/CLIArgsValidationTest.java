package com.example.script.cli;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static com.example.script.OutputMatcher.matchesOutput;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class CLIArgsValidationTest {

    private static final String TEST_DATA_FILE = "/customers.txt";

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    private Path inputFilePath;

    @Before
    public void setUp() throws IOException {
        InputStream resource = this.getClass().getResourceAsStream(TEST_DATA_FILE);
        inputFilePath = Files.createTempFile(null, null);
        Files.copy(resource, inputFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(inputFilePath);
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenNoArgsAreGiven() {
        // when
        CLI.main(new String[] {});

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-r, --radius <BigDecimal>           radius of proximity (in km) (default: 100.0)",
                "-v, --verbose                       verbose mode"
        ));
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOptionIsGiven() {
        // when
        CLI.main(new String[] {"-?"});

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-r, --radius <BigDecimal>           radius of proximity (in km) (default: 100.0)",
                "-v, --verbose                       verbose mode"
        ));
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOptionAliasIsGiven() {
        // when
        CLI.main(new String[] {"-h"});

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-r, --radius <BigDecimal>           radius of proximity (in km) (default: 100.0)",
                "-v, --verbose                       verbose mode"
        ));
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOption2ndAliasIsGiven() {
        // when
        CLI.main(new String[] {"--help"});

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-r, --radius <BigDecimal>           radius of proximity (in km) (default: 100.0)",
                "-v, --verbose                       verbose mode"
        ));
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenUnrecognizedArgumentIsGiven() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Unrecognized options/arguments: [unknown_argument]"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude", "0.0",
                "unknown_argument"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenUnrecognizedOptionIsGiven() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Unrecognized options/arguments: [--unknown_option]"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude", "0.0",
                "--unknown_option"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInputFileOptionArgIsMissing() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option input-file requires an argument"
        )));

        // when
        CLI.main(new String[] {
                "--latitude", "0.0",
                "--longitude", "0.0",
                "--input-file"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenNonExistentFileIsGivenViaInputFileOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "file either does not exist or is not a regular file: nonexistent_file.txt"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", "nonexistent_file.txt",
                "--latitude", "0.0",
                "--longitude", "0.0",
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidFileIsGivenViaInputFileOption() {
        // given
        final File tempDirectory = FileUtils.getTempDirectory();

        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "file either does not exist or is not a regular file: " + tempDirectory.getPath()
        )));

        // when
        CLI.main(new String[] {
                "--input-file", tempDirectory.getPath(),
                "--latitude", "0.0",
                "--longitude", "0.0",
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenLatitudeOptionArgIsMissing() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option lat/latitude requires an argument"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--longitude", "0.0",
                "--latitude"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLatitudeOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option lat/latitude"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "NaN",
                "--longitude", "0.0"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLatitudeOptionAlias() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option lat/latitude"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "NaN",
                "--long", "0.0"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenValueOutOfRangeIsGivenViaLatitudeOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "latitude must be between -90.0 and 90.0"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "90.1",
                "--longitude", "0.0"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenLongitudeOptionArgIsMissing() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option long/longitude requires an argument"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLongitudeOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option long/longitude"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude", "NaN"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLongitudeOptionAlias() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option long/longitude"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "0.0",
                "--long", "NaN"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenValueOutOfRangeIsGivenViaLongitudeOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "longitude must be between -180.0 and 180.0"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude", "180.1"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaRadiusOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option r/radius"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude", "0.0",
                "--radius", "NaN"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaRadiusOptionAlias() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option r/radius"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "0.0",
                "--long", "0.0",
                "-r", "NaN"
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenNegativeValueIsGivenViaRadiusOption() {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "radius must be greater than or equal to 0"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--latitude", "0.0",
                "--longitude", "0.0",
                "--radius", "-1.0"
        });
    }

    @Test
    public void cli_shouldPrintInfoMessage_whenVerboseModeIsEnabledViaLongOption() {
        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "0.0",
                "--long", "0.0",
                "--verbose"
        });

        // then
        assertThat(systemOutRule.getLog(), containsString("Verbose mode enabled"));
    }

    @Test
    public void cli_shouldPrintInfoMessage_whenVerboseModeIsEnabledViaShortOption() {
        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "0.0",
                "--long", "0.0",
                "-v"
        });

        // then
        assertThat(systemOutRule.getLog(), containsString("Verbose mode enabled"));
    }
}
