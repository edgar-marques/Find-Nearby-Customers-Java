package com.example.findnearbycustomers;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.File;

import static com.example.findnearbycustomers.OutputMatcher.matchesOutput;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class CLITest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void cli_shouldPrintHelpScreen_whenNoArgsAreGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option                            Description",
                "------                            -----------",
                "-?, -h                            show help",
                "--input-file <File>               customer file",
                "--lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                    location",
                "--long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                    location",
                "-v, --verbose                     verbose mode"
        )));

        // when
        CLI.main(new String[] {});
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOptionIsGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option                            Description",
                "------                            -----------",
                "-?, -h                            show help",
                "--input-file <File>               customer file",
                "--lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                    location",
                "--long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                    location",
                "-v, --verbose                     verbose mode"
        )));

        // when
        CLI.main(new String[] {"-?"});
    }

    @Test
    public void cli_shouldPrintHelpScreen_whenHelpOptionAliasIsGiven() throws Exception {
        // then
        exit.expectSystemExit();
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Option                            Description",
                "------                            -----------",
                "-?, -h                            show help",
                "--input-file <File>               customer file",
                "--lat, --latitude <BigDecimal>    latitude coordinate (in degrees) of target",
                "                                    location",
                "--long, --longitude <BigDecimal>  longitude coordinate (in degrees) of target",
                "                                    location",
                "-v, --verbose                     verbose mode"
        )));

        // when
        CLI.main(new String[] {"-h"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenUnrecognizedArgumentIsGiven() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Unrecognized options/arguments: [unknown]"
        )));

        // when
        CLI.main(new String[] {"unknown"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenUnrecognizedOptionIsGiven() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Unrecognized options/arguments: [--unknown]"
        )));

        // when
        CLI.main(new String[] {"--unknown"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenNonExistentFileIsGivenViaInputFileOption() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Specified input file does not exist: nonexistent_file"
        )));

        // when
        CLI.main(new String[] {"--input-file", "nonexistent_file"});
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
        CLI.main(new String[] {"--input-file", tempDirectory.getPath()});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLatitudeOption() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option lat/latitude"
        )));

        // when
        CLI.main(new String[] {"--latitude", "NaN"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLatitudeOptionAlias() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option lat/latitude"
        )));

        // when
        CLI.main(new String[] {"--lat", "NaN"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLongitudeOption() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option long/longitude"
        )));

        // when
        CLI.main(new String[] {"--longitude", "NaN"});
    }

    @Test
    public void cli_shouldPrintErrorMessage_whenInvalidDecimalValueIsGivenViaLongitudeOptionAlias() throws Exception {
        // then
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> assertThat(systemOutRule.getLog(), matchesOutput(
                "Cannot parse argument 'NaN' of option long/longitude"
        )));

        // when
        CLI.main(new String[] {"--long", "NaN"});
    }

    @Test
    public void cli_shouldPrintInfoMessage_whenVerboseModeIsEnabledViaLongOption() throws Exception {
        // when
        CLI.main(new String[] {"--verbose"});

        // then
        assertThat(systemOutRule.getLog(), containsString("Verbose mode enabled"));
    }

    @Test
    public void cli_shouldPrintInfoMessage_whenVerboseModeIsEnabledViaShortOption() throws Exception {
        // when
        CLI.main(new String[] {"-v"});

        // then
        assertThat(systemOutRule.getLog(), containsString("Verbose mode enabled"));
    }
}
