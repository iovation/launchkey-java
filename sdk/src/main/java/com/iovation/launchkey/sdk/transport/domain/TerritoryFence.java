package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", "name", "country", "administrative_area", "postal_code"})
public class TerritoryFence extends Fence {

    @JsonProperty("country")
    private final String country;

    @JsonProperty("administrative_area")
    private final String administrativeArea;

    @JsonProperty("postal_code")
    private final String postalCode;

    @JsonCreator
    public TerritoryFence(@JsonProperty("name") String fenceName,
                          @JsonProperty("country") String country,
                          @JsonProperty("administrative_area") String administrativeArea,
                          @JsonProperty("postal_code") String postalCode) {
        super(fenceName,"TERRITORY");
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
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

}
