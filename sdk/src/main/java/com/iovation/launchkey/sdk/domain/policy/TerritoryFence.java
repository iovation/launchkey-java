package com.iovation.launchkey.sdk.domain.policy;

import java.util.Objects;

public class TerritoryFence implements Fence {

    private final String fenceName;

    private final String country;

    private final String administrativeArea;

    private final String postalCode;

    public TerritoryFence(String fenceName, String country, String administrativeArea, String postalCode) {
        this.fenceName = fenceName;
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
    }

    @Override
    public String getName() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerritoryFence that = (TerritoryFence) o;
        return Objects.equals(fenceName, that.fenceName) &&
                Objects.equals(country, that.country) &&
                Objects.equals(administrativeArea, that.administrativeArea) &&
                Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fenceName, country, administrativeArea, postalCode);
    }

    @Override
    public String toString() {
        return "TerritoryFence{" +
                "fenceName='" + fenceName + '\'' +
                ", country='" + country + '\'' +
                ", administrativeArea='" + administrativeArea + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
