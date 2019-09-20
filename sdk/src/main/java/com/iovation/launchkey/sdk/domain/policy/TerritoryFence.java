package com.iovation.launchkey.sdk.domain.policy;

import java.util.Objects;

public class TerritoryFence implements Fence {

    private final String fenceName;

    private final String country;

    private final String administrativeArea;

    private final String postalCode;

    public TerritoryFence(String country) {
        this.country = country;
        this.administrativeArea = null;
        this.postalCode = null;
        this.fenceName = null;
    }

    public TerritoryFence(String country, String administrativeArea, String postalCode, String fenceName) {
        this.country = country;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
        this.fenceName = fenceName;
    }

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TerritoryFence)) return false;
        TerritoryFence that = (TerritoryFence) o;
        return Objects.equals(getFenceName(), that.getFenceName()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getAdministrativeArea(), that.getAdministrativeArea()) &&
                Objects.equals(getPostalCode(), that.getPostalCode());
    }
}
