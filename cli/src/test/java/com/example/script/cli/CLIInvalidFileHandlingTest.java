package com.example.script.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
        InputStream resource = this.getClass().getResourceAsStream(TEST_DATA_FILE);
        inputFilePath = Files.createTempFile(null, null);
        Files.copy(resource, inputFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(inputFilePath);
    }

    @Ignore
    @Test
    public void cli_shouldOutputListOfCustomersWithinRangeOfGivenPositionSortedByUserIdAsc() {
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
                "Customer(userId=6, name=Theresa Enright, location=Coordinate(latitude=53.1229599, longitude=-6.2705202))",
                "Customer(userId=8, name=Eoin Ahearn, location=Coordinate(latitude=54.0894797, longitude=-6.18671))",
                "Customer(userId=11, name=Richard Finnegan, location=Coordinate(latitude=53.008769, longitude=-6.1056711))",
                "Customer(userId=12, name=Christina McArdle, location=Coordinate(latitude=52.986375, longitude=-6.043701))",
                "Customer(userId=13, name=Olive Ahearn, location=Coordinate(latitude=53, longitude=-7))",
                "Customer(userId=15, name=Michael Ahearn, location=Coordinate(latitude=52.966, longitude=-6.463))",
                "Customer(userId=17, name=Patricia Cahill, location=Coordinate(latitude=54.180238, longitude=-5.920898))",
                "Customer(userId=23, name=Eoin Gallagher, location=Coordinate(latitude=54.080556, longitude=-6.361944))",
                "Customer(userId=24, name=Rose Enright, location=Coordinate(latitude=54.133333, longitude=-6.433333))",
                "Customer(userId=26, name=Stephen McArdle, location=Coordinate(latitude=53.038056, longitude=-7.653889))",
                "Customer(userId=29, name=Oliver Ahearn, location=Coordinate(latitude=53.74452, longitude=-7.11167))",
                "Customer(userId=30, name=Nick Enright, location=Coordinate(latitude=53.761389, longitude=-7.2875))",
                "Customer(userId=31, name=Alan Behan, location=Coordinate(latitude=53.1489345, longitude=-6.8422408))",
                "Customer(userId=39, name=Lisa Ahearn, location=Coordinate(latitude=53.0033946, longitude=-6.3877505))"
        ));
    }
}
