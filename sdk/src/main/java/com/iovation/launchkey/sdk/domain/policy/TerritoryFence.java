package com.iovation.launchkey.sdk.domain.policy;

public class TerritoryFence implements Fence {

    private String fenceName;

    private String country;

    private String administrativeArea;

    private String postalCode;

    @Override
    public String getFenceName() {
        return fenceName;
    }

    @Override
    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
