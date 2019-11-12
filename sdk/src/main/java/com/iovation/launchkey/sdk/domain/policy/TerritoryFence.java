package com.iovation.launchkey.sdk.domain.policy;

import java.util.Objects;

public class TerritoryFence implements Fence {

    private final String name;

    private final String country;

    private final String administrativeArea;

    private final String postalCode;

    public TerritoryFence(String name, String country, String administrativeArea, String postalCode) {
        this.name = name;
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
    }

    @Override
    public String getName() {
        return name;
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
        return Objects.equals(name, that.name) &&
                Objects.equals(country, that.country) &&
                Objects.equals(administrativeArea, that.administrativeArea) &&
                Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, country, administrativeArea, postalCode);
    }

    @Override
    public String toString() {
        return "TerritoryFence{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", administrativeArea='" + administrativeArea + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
