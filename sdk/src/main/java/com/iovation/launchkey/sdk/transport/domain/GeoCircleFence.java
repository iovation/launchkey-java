package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "name", "latitude", "longitude", "radius"})
public class GeoCircleFence extends Fence {

    @JsonProperty("latitude")
    private final double latitude;

    @JsonProperty("longitude")
    private final double longitude;

    @JsonProperty("radius")
    private final double radius;

    @JsonCreator
    public GeoCircleFence(@JsonProperty("name") String fenceName,
                          @JsonProperty("latitude") double latitude,
                          @JsonProperty("longitude") double longitude,
                          @JsonProperty("radius") double radius) {
        super(fenceName,"GEO_CIRCLE");
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRadius() {
        return radius;
    }

}
