package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "name", "latitude", "longitude", "radius"})
public class GeoCircleFence implements Fence {

    @JsonProperty("name")
    private final String fenceName;

    @JsonProperty("latitude")
    private final double latitude;

    @JsonProperty("longitude")
    private final double longitude;

    @JsonProperty("radius")
    private final double radius;

    @JsonProperty("type")
    private final String type = "GEO_CIRCLE";

    @JsonCreator
    public GeoCircleFence(@JsonProperty("name") String fenceName,
                          @JsonProperty("latitude") double latitude,
                          @JsonProperty("longitude") double longitude,
                          @JsonProperty("radius") double radius) {
        this.fenceName = fenceName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public String getFenceName() {
        return fenceName;
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

    public String getType() {
        return type;
    }
}
