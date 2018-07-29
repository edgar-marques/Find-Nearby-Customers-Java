package com.example.findnearbycustomers.cli;

import com.example.findnearbycustomers.io.validation.RegularFile;
import com.example.geocoord.Coordinate;
import lombok.Value;

import javax.annotation.concurrent.Immutable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.File;
import java.math.BigDecimal;

@Value
@Immutable
public class CLIArgs {
    @RegularFile
    @NotNull(message = "file must not be null")
    File inputFile;

    @Valid
    @NotNull(message = "target location must not be null")
    Coordinate targetLocation;

    @PositiveOrZero(message = "radius must be greater than or equal to 0")
    @NotNull(message = "radius must not be null")
    BigDecimal radius;

    boolean verbose;
}
