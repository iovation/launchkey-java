package com.iovation.launchkey.sdk.domain.directory;

import java.util.Objects;
import java.util.UUID;

public class DeviceLinkCompletionResponse {
    private final UUID deviceId;
    private final String devicePublicKey;
    private final String devicePublicKeyId;

    public DeviceLinkCompletionResponse(UUID deviceId, String devicePublicKey, String devicePublicKeyId) {
        this.deviceId = deviceId;
        this.devicePublicKey = devicePublicKey;
        this.devicePublicKeyId = devicePublicKeyId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public String getDevicePublicKey() {
        return devicePublicKey;
    }

    public String getDevicePublicKeyId() {
        return devicePublicKeyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceLinkCompletionResponse)) return false;
        DeviceLinkCompletionResponse that = (DeviceLinkCompletionResponse) o;
        return Objects.equals(getDeviceId(), that.getDeviceId()) &&
                Objects.equals(getDevicePublicKey(), that.getDevicePublicKey()) &&
                Objects.equals(getDevicePublicKeyId(), that.getDevicePublicKeyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeviceId(), getDevicePublicKey(), getDevicePublicKeyId());
    }

    @Override
    public String toString() {
        return "DeviceLinkCompletionResponse{" +
                "deviceId=" + deviceId +
                ", devicePublicKey='" + devicePublicKey + '\'' +
                ", devicePublicKeyId='" + devicePublicKeyId + '\'' +
                '}';
    }
}
