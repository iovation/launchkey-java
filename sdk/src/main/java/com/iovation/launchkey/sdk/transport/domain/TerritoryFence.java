package com.iovation.launchkey.sdk.transport.domain;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "type", "country", "administrative_area", "postal_code"})
public class TerritoryFence {

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

    public TerritoryFence(String fenceName, String country, String administrativeArea, String postalCode) {
        this.fenceName = fenceName;
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
    }
}
