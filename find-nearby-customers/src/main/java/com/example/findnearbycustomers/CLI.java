package com.example.findnearbycustomers;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.math.BigDecimal;

import static java.util.Arrays.asList;

@Log4j2
public class CLI {

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser() {
            {
                acceptsAll(asList("h", "?"), "show help").forHelp();
                acceptsAll(asList("v", "verbose"), "verbose mode");
                accepts("input-file", "customer file").withRequiredArg().ofType(File.class);
                acceptsAll(asList("lat", "latitude"), "latitude coordinate (in degrees) of target location").withRequiredArg().ofType(BigDecimal.class);
                acceptsAll(asList("long", "longitude"), "longitude coordinate (in degrees) of target location").withRequiredArg().ofType(BigDecimal.class);
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

            if (options.has("input-file")) {
                File inputFile = (File) options.valueOf("input-file");
                if (!inputFile.exists()) {
                    log.error("Specified input file does not exist: {}", inputFile.getPath());
                    System.exit(1);
                }
                if (!inputFile.isFile()) {
                    log.error("Specified input file is not a valid file: {}", inputFile.getPath());
                    System.exit(1);
                }
            }

            if (options.has("latitude") || options.has("lat")) {
                BigDecimal latitude = (BigDecimal) options.valueOf("latitude");
            }

            if (options.has("longitude") || options.has("long")) {
                BigDecimal longitude = (BigDecimal) options.valueOf("longitude");
            }

            if (options.has("verbose") || options.has("v")) {
                boolean verbose = true;
                log.info("Verbose mode enabled");
            }

        } catch (OptionException ex) {
            log.error(ex.getMessage());
            System.exit(1);
        }
    }
}
