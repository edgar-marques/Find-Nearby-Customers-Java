package com.example.domain.geocoord.model;

import lombok.Value;

import javax.annotation.concurrent.Immutable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
@Immutable
public class Coordinate {

    @NotNull(message = "latitude must not be null")
    @DecimalMax(value = "90.0", message = "latitude must be between -90.0 and 90.0")
    @DecimalMin(value = "-90.0", message = "latitude must be between -90.0 and 90.0")
    BigDecimal latitude;

    @NotNull(message = "longitude must not be null")
    @DecimalMax(value = "180.0", message = "longitude must be between -180.0 and 180.0")
    @DecimalMin(value = "-180.0", message = "longitude must be between -180.0 and 180.0")
    BigDecimal longitude;

    private Coordinate(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinate of(BigDecimal latitude, BigDecimal longitude) {
        return new Coordinate(latitude, longitude);
    }

    public static Coordinate of(String latitude, String longitude) {
        return Coordinate.of(new BigDecimal(latitude), new BigDecimal(longitude));
    }
}
