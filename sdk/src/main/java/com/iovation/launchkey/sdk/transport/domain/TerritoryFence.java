package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", "name", "country", "administrative_area", "postal_code"})
public class TerritoryFence implements Fence {

    @JsonProperty("name")
    private final String fenceName;

    @JsonProperty("country")
    private final String country;

    @JsonProperty("administrative_area")
    private final String administrativeArea;

    @JsonProperty("postal_code")
    private final String postalCode;

    @JsonProperty("type")
    private final String type = "TERRITORY";

    @JsonCreator
    public TerritoryFence(@JsonProperty("name") String fenceName,
                          @JsonProperty("country") String country,
                          @JsonProperty("administrative_area") String administrativeArea,
                          @JsonProperty("postal_code") String postalCode) {
        this.fenceName = fenceName;
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
    }

    public String getFenceName() {
        return fenceName;
    }

    public String getCountry() {
        return country;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getType() {
        return type;
    }
}
