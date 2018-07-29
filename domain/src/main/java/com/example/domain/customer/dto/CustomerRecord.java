package com.example.domain.customer.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 */
@Data
class CustomerRecord {

    /**
     *
     */
    @NotNull(message = "user ID must not be null")
    private Long userId;

    /**
     *
     */
    @NotBlank(message = "name must not be blank")
    private String name;

    /**
     *
     */
    @NotNull(message = "latitude must not be null")
    @DecimalMax(value = "90.0", message = "latitude must be between -90.0 and 90.0")
    @DecimalMin(value = "-90.0", message = "latitude must be between -90.0 and 90.0")
    BigDecimal latitude;

    /**
     *
     */
    @NotNull(message = "longitude must not be null")
    @DecimalMax(value = "180.0", message = "longitude must be between -180.0 and 180.0")
    @DecimalMin(value = "-180.0", message = "longitude must be between -180.0 and 180.0")
    BigDecimal longitude;

    /**
     *
     * @param userId
     * @param name
     * @param latitude
     * @param longitude
     */
    @JsonCreator
    public CustomerRecord(
            @JsonProperty("user_id") Long userId,
            @JsonProperty("name") String name,
            @JsonProperty("latitude") BigDecimal latitude,
            @JsonProperty("longitude") BigDecimal longitude) {
        this.userId = userId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
