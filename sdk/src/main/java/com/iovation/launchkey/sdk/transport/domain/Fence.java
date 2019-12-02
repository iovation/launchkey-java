package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"type", "name", "latitude", "longitude", "radius", "country", "administrative_area", "postal_code"})
public class Fence {

    public static final String TYPE_TERRITORY = "TERRITORY";
    public static final String TYPE_GEO_CIRCLE = "GEO_CIRCLE";
    public static final String FACTOR_INHERENCE = "INHERENCE";
    public static final String FACTOR_KNOWLEDGE = "KNOWLEDGE";
    public static final String FACTOR_POSSESSION = "POSSESSION";

    @JsonProperty("type")
    private final String fenceType;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("latitude")
    private final Double latitude;

    @JsonProperty("longitude")
    private final Double longitude;

    @JsonProperty("radius")
    private final Double radius;

    @JsonProperty("country")
    private final String country;

    @JsonProperty("administrative_area")
    private final String administrativeArea;

    @JsonProperty("postal_code")
    private final String postalCode;

    @JsonCreator
    public Fence(@JsonProperty("name") String name, @JsonProperty("type") String fenceType,
                 @JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude,
                 @JsonProperty("radius") Double radius, @JsonProperty("country") String country,
                 @JsonProperty("administrative_area") String administrativeArea,
                 @JsonProperty("postal_code") String postalCode) {
        this.name = name;
        this.fenceType = fenceType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return fenceType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getRadius() {
        return radius;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fence fence = (Fence) o;
        return Objects.equals(fenceType, fence.fenceType) &&
                Objects.equals(name, fence.name) &&
                Objects.equals(latitude, fence.latitude) &&
                Objects.equals(longitude, fence.longitude) &&
                Objects.equals(radius, fence.radius) &&
                Objects.equals(country, fence.country) &&
                Objects.equals(administrativeArea, fence.administrativeArea) &&
                Objects.equals(postalCode, fence.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fenceType, name, latitude, longitude, radius, country, administrativeArea, postalCode);
    }

    @Override
    public String toString() {
        return "Fence{" +
                "fenceType='" + fenceType + '\'' +
                ", fenceName='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", country='" + country + '\'' +
                ", administrativeArea='" + administrativeArea + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}