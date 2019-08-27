package com.iovation.launchkey.sdk.domain.policy;

public class GeoCircleFence implements Fence {

    private String fenceName;

    private double latitude;

    private double longitude;

    private double radius;

    @Override
    public String getFenceName() {
        return fenceName;
    }

    @Override
    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
