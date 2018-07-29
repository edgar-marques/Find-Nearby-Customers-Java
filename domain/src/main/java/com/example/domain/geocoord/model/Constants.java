package com.example.domain.geocoord.model;

public final class Constants {

    private Constants() {
        // restrict instantiation
        throw new UnsupportedOperationException();
    }

    /**
     * @see <a href="https://en.wikipedia.org/wiki/Great-circle_distance#Radius_for_spherical_Earth">Radius for spherical Earth</a>
     */
    public static final double MEAN_EARTH_RADIUS = 6_371_000.0; // approx. mean Earth radius in meters
}
