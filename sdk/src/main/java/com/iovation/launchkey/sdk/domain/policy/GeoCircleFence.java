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
    public String getName() {
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
        if (o == null || getClass() != o.getClass()) return false;
        GeoCircleFence that = (GeoCircleFence) o;
        return Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.radius, radius) == 0 &&
                Objects.equals(fenceName, that.fenceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fenceName, latitude, longitude, radius);
    }

    @Override
    public String toString() {
        return "GeoCircleFence{" +
                "fenceName='" + fenceName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                '}';
    }
}
