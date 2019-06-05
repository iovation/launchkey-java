package com.iovation.launchkey.sdk.integration.managers.kobiton;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KobitonCloudDevice extends KobitonDevice {
    public KobitonCloudDevice(@JsonProperty(value = "id") int id,
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
                              @JsonProperty(value = "isMyOwn") boolean isMyOwn) {
        super(id,
                isBooked,
                isOnline,
                modelName,
                deviceName,
                platformName,
                platformVersion,
                deviceImageUrl,
                isFavorite,
                isCloud,
                isMyOrg,
                isMyOwn,
                null);
    }
}
