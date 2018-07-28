package com.example.findnearbycustomers;

import com.example.geocoordsys.Coordinate;
import com.example.geocoordsys.CoordinateService;
import com.example.geocoordsys.DefaultCoordinateService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Log4j2
public class CLI {

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser() {
            {
                acceptsAll(asList("h", "help", "?"), "show help").forHelp();
                acceptsAll(asList("v", "verbose"), "verbose mode");

                accepts("input-file", "customer file")
                        .withRequiredArg().ofType(File.class)
                        .required();

                acceptsAll(asList("lat", "latitude"), "latitude coordinate (in degrees) of target location")
                        .withRequiredArg().ofType(BigDecimal.class)
                        .required();

                acceptsAll(asList("long", "longitude"), "longitude coordinate (in degrees) of target location")
                        .withRequiredArg().ofType(BigDecimal.class)
                        .required();


                allowsUnrecognizedOptions();
            }
        };

        if (args.length == 0) {
            parser.printHelpOn(System.out);
            System.exit(0);
        }

        try {
            OptionSet options = parser.parse(args);

            if (!options.nonOptionArguments().isEmpty()) {
                log.error("Unrecognized options/arguments: {}", options.nonOptionArguments());
                System.exit(1);
            }

            if (options.has("?") || options.has("h")) {
                parser.printHelpOn(System.out);
                System.exit(0);
            }

            File inputFile = (File) options.valueOf("input-file");
            if (!inputFile.exists()) {
                log.error("Specified input file does not exist: {}", inputFile.getPath());
                System.exit(1);
            }
            if (!inputFile.isFile()) {
                log.error("Specified input file is not a valid file: {}", inputFile.getPath());
                System.exit(1);
            }

            BigDecimal latitude = (BigDecimal) options.valueOf("latitude");
            BigDecimal longitude = (BigDecimal) options.valueOf("longitude");

            boolean verbose = options.has("verbose") || options.has("v");
            if (verbose) {
                log.info("Verbose mode enabled");
            }

            List<String> lines = Files.readLines(inputFile, Charsets.UTF_8);

            final Coordinate targetCoordinate = Coordinate.of(latitude, longitude);

            final ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            List<CustomerRecord> records = lines.stream()
                    .map(r -> {
                        try {
                            return mapper.readValue(r, CustomerRecord.class);
                        } catch (IOException ex) {
                            log.error(ex.getMessage());
                            return null;
                        }
                    })
                    .filter(r -> r != null)
                    .collect(Collectors.toList());

            final CoordinateService coordService = new DefaultCoordinateService();
            final double maxDistance = 100_000.0;   // 100km

            List<CustomerRecord> filteredAndSortedRecords = records.stream()
                    .filter(r -> {
                        double distance = coordService.greatCircleDistanceOnEarthBetween(
                                r.getLocation(),
                                targetCoordinate);
                        return distance <= maxDistance;
                    })
                    .sorted(Comparator.comparing(CustomerRecord::getUserId))
                    .collect(Collectors.toList());

            filteredAndSortedRecords.stream()
                    .forEach(r -> log.info(r.toString()));

        } catch (OptionException ex) {
            log.error(ex.getMessage());
            System.exit(1);
        }
    }
}
