package com.example.script.cli;

import com.example.domain.geocoord.model.Coordinate;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.extern.log4j.Log4j2;

import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Arrays.asList;

/**
 *
 */
@Log4j2
@Singleton
public class DefaultCLIArgsParser implements CLIArgsParser {

    private final OptionParser parser;

    /**
     *
     */
    public DefaultCLIArgsParser() {
        this.parser = new OptionParser() {
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

                acceptsAll(asList("r", "radius"), "radius of proximity (in km)")
                        .withRequiredArg().ofType(BigDecimal.class)
                        .defaultsTo(new BigDecimal("100.0"));

                allowsUnrecognizedOptions();
            }
        };
    }

    /**
     *
     * @param args
     * @return
     */
    @Override
    public Optional<CLIArgs> parse(String[] args) {
        if (args.length == 0) {
            return Optional.empty();
        }

        OptionSet options = parser.parse(args);

        if (options.has("?") || options.has("h")) {
            return Optional.empty();
        }

        if (!options.nonOptionArguments().isEmpty()) {
            throw new IllegalArgumentException("Unrecognized options/arguments: "
                    + options.nonOptionArguments());
        }

        File inputFile = (File) options.valueOf("input-file");
        BigDecimal latitude = (BigDecimal) options.valueOf("latitude");
        BigDecimal longitude = (BigDecimal) options.valueOf("longitude");
        BigDecimal radius = (BigDecimal) options.valueOf("radius");
        boolean verbose = options.has("verbose") || options.has("v");

        return Optional.of(new CLIArgs(inputFile, Coordinate.of(latitude, longitude), radius, verbose));
    }

    /**
     *
     * @return
     */
    @Override
    public String usageInfo() {
        OutputStream out = new ByteArrayOutputStream();
        try {
            parser.printHelpOn(out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return out.toString();
    }
}
