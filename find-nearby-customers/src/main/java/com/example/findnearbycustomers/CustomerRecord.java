package com.example.findnearbycustomers;

import com.example.geocoordsys.Coordinate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CustomerRecord {

    private long userId;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotNull(message = "location must not be null")
    @Valid
    private Coordinate location;

    @JsonCreator
    public CustomerRecord(
            @JsonProperty("user_id") long userId,
            @JsonProperty("name") String name,
            @JsonProperty("latitude") String latitude,
            @JsonProperty("longitude") String longitude) {
        this.userId = userId;
        this.name = name;
        this.location = Coordinate.of(latitude, longitude);
    }
}
