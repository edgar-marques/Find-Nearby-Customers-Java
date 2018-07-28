package com.example.findnearbycustomers;

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

import static com.example.findnearbycustomers.OutputMatcher.matchesOutput;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class CLITest {

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
    public void cli_shouldPrintHelpScreen_whenNoArgsAreGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-v, --verbose                       verbose mode"
        )));

        // when
        CLI.main(new String[] {});
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOptionIsGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-v, --verbose                       verbose mode"
        )));

        // when
        CLI.main(new String[] {"-?"});
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOptionAliasIsGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-v, --verbose                       verbose mode"
        )));

        // when
        CLI.main(new String[] {"-h"});
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOption2ndAliasIsGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option (* = required)               Description",
                "---------------------               -----------",
                "-?, -h, --help                      show help",
                "* --input-file <File>               customer file",
                "* --lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                      location",
                "* --long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                      location",
                "-v, --verbose                       verbose mode"
        )));

        // when
        CLI.main(new String[] {"--help"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenUnrecognizedArgumentIsGiven() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenUnrecognizedOptionIsGiven() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenInputFileOptionArgIsMissing() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenNonExistentFileIsGivenViaInputFileOption() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Specified input file does not exist: nonexistent_file.txt"
        )));

        // when
        CLI.main(new String[] {
                "--input-file", "nonexistent_file.txt",
                "--latitude", "0.0",
                "--longitude", "0.0",
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidFileIsGivenViaInputFileOption() throws Exception {
        // given
        final File tempDirectory = FileUtils.getTempDirectory();

        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Specified input file is not a valid file: " + tempDirectory.getPath()
        )));

        // when
        CLI.main(new String[] {
                "--input-file", tempDirectory.getPath(),
                "--latitude", "0.0",
                "--longitude", "0.0",
        });
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenLatitudeOptionArgIsMissing() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLatitudeOption() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLatitudeOptionAlias() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenLongitudeOptionArgIsMissing() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLongitudeOption() throws Exception {
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
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLongitudeOptionAlias() throws Exception {
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
    public void cli_shouldPrintInfoMessage_whenVerboseModeIsEnabledViaLongOption() throws Exception {
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
    public void cli_shouldPrintInfoMessage_whenVerboseModeIsEnabledViaShortOption() throws Exception {
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

    @Test
    public void cli_shouldOutputListOfCustomersWithinRangeOfGivenPositionSortedByUserIdAsc() throws Exception {
        // when
        CLI.main(new String[] {
                "--input-file", inputFilePath.toString(),
                "--lat", "53.339428",
                "--long", "-6.257664"
        });

        // then
        assertThat(systemOutRule.getLog(), matchesOutput(
                "CustomerRecord(userId=4, name=Ian Kehoe, location=Coordinate(latitude=53.2451022, longitude=-6.238335))",
                "CustomerRecord(userId=5, name=Nora Dempsey, location=Coordinate(latitude=53.1302756, longitude=-6.2397222))",
                "CustomerRecord(userId=6, name=Theresa Enright, location=Coordinate(latitude=53.1229599, longitude=-6.2705202))",
                "CustomerRecord(userId=8, name=Eoin Ahearn, location=Coordinate(latitude=54.0894797, longitude=-6.18671))",
                "CustomerRecord(userId=11, name=Richard Finnegan, location=Coordinate(latitude=53.008769, longitude=-6.1056711))",
                "CustomerRecord(userId=12, name=Christina McArdle, location=Coordinate(latitude=52.986375, longitude=-6.043701))",
                "CustomerRecord(userId=13, name=Olive Ahearn, location=Coordinate(latitude=53, longitude=-7))",
                "CustomerRecord(userId=15, name=Michael Ahearn, location=Coordinate(latitude=52.966, longitude=-6.463))",
                "CustomerRecord(userId=17, name=Patricia Cahill, location=Coordinate(latitude=54.180238, longitude=-5.920898))",
                "CustomerRecord(userId=23, name=Eoin Gallagher, location=Coordinate(latitude=54.080556, longitude=-6.361944))",
                "CustomerRecord(userId=24, name=Rose Enright, location=Coordinate(latitude=54.133333, longitude=-6.433333))",
                "CustomerRecord(userId=26, name=Stephen McArdle, location=Coordinate(latitude=53.038056, longitude=-7.653889))",
                "CustomerRecord(userId=29, name=Oliver Ahearn, location=Coordinate(latitude=53.74452, longitude=-7.11167))",
                "CustomerRecord(userId=30, name=Nick Enright, location=Coordinate(latitude=53.761389, longitude=-7.2875))",
                "CustomerRecord(userId=31, name=Alan Behan, location=Coordinate(latitude=53.1489345, longitude=-6.8422408))",
                "CustomerRecord(userId=39, name=Lisa Ahearn, location=Coordinate(latitude=53.0033946, longitude=-6.3877505))"
        ));
    }

    //TODO: validate location coordinates
    //TODO: add option argument for max distance
}
