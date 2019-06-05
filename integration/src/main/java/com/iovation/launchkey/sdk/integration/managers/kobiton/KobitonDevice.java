package com.iovation.launchkey.sdk.integration.managers.kobiton;

public abstract class KobitonDevice {
    public final int id;
    public final boolean isBooked;
    public final boolean isOnline;
    public final String modelName;
    public final String deviceName;
    public final String platformName;
    public final String platformVersion;
    public final String deviceImageUrl;
    public final boolean isFavorite;
    public final boolean isCloud;
    public final boolean isMyOrg;
    public final boolean isMyOwn;
    public final String udid;

    public KobitonDevice(
            int id,
            boolean isBooked,
            boolean isOnline,
            String  modelName,
            String  deviceName,
            String  platformName,
            String  platformVersion,
            String  deviceImageUrl,
            boolean isFavorite,
            boolean isCloud,
            boolean isMyOrg,
            boolean isMyOwn,
            String  udid
    ) {

        this.id = id;
        this.isBooked = isBooked;
        this.isOnline = isOnline;
        this.modelName = modelName;
        this.deviceName = deviceName;
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.deviceImageUrl = deviceImageUrl;
        this.isFavorite = isFavorite;
        this.isCloud = isCloud;
        this.isMyOrg = isMyOrg;
        this.isMyOwn = isMyOwn;
        this.udid = udid;
    }
}
