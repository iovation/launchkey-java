package com.iovation.launchkey.sdk.integration.managers.kobiton;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KobitonDevice {
    private final int id;
    private final boolean isBooked;
    private final boolean isOnline;
    private final String modelName;
    private final String deviceName;
    private final String platformName;
    private final String platformVersion;
    private final String deviceImageUrl;
    private final boolean isFavorite;
    private final boolean isCloud;
    private final boolean isMyOrg;
    private final boolean isMyOwn;
    private final String udid;

    public int getId() {
        return id;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public String getModelName() {
        return modelName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getDeviceImageUrl() {
        return deviceImageUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isCloud() {
        return isCloud;
    }

    public boolean isMyOrg() {
        return isMyOrg;
    }

    public boolean isMyOwn() {
        return isMyOwn;
    }

    public String getUdid() {
        return udid;
    }

    public KobitonDevice(@JsonProperty(value = "id") int id,
                         @JsonProperty(value = "isBooked") boolean isBooked,
                         @JsonProperty(value = "isOnline") boolean isOnline,
                         @JsonProperty(value = "modelName") String modelName,
                         @JsonProperty(value = "deviceName") String deviceName,
                         @JsonProperty(value = "platformName") String platformName,
                         @JsonProperty(value = "platformVersion") String platformVersion,
                         @JsonProperty(value = "deviceImageUrl") String deviceImageUrl,
                         @JsonProperty(value = "isFavorite") boolean isFavorite,
                         @JsonProperty(value = "isCloud") boolean isCloud,
                         @JsonProperty(value = "isMyOrg") boolean isMyOrg,
                         @JsonProperty(value = "isMyOwn") boolean isMyOwn,
                         @JsonProperty(value = "udid") String udid) {

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
