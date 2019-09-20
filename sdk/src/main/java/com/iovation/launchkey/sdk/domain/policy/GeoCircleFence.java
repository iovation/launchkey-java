package com.iovation.launchkey.sdk.domain.policy;

import java.util.Objects;

public class GeoCircleFence implements Fence {

    private final String fenceName;

    private final double latitude;

    private final double longitude;

    private final double radius;

    public GeoCircleFence(String fenceName, double latitude, double longitude, double radius) {
        this.fenceName = fenceName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    public GeoCircleFence(double latitude, double longitude, double radius) {
        this.fenceName = null;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
    }

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeoCircleFence)) return false;
        GeoCircleFence that = (GeoCircleFence) o;
        return Objects.equals(getFenceName(), that.getFenceName()) &&
                Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLongitude(), that.getLongitude()) &&
                Objects.equals(getRadius(), that.getRadius());
    }
}
