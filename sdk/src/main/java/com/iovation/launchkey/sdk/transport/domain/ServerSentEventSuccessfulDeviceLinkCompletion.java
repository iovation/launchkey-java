package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerSentEventSuccessfulDeviceLinkCompletion implements ServerSentEvent {
    private final String deviceId;
    private final String publicKeyId;
    private final String publicKey;

    @JsonCreator
    public ServerSentEventSuccessfulDeviceLinkCompletion(
            @JsonProperty("device_id") String deviceId,
            @JsonProperty("public_key_id") String publicKeyId,
            @JsonProperty("public_key") String publicKey) {
        this.deviceId = deviceId;
        this.publicKeyId = publicKeyId;
        this.publicKey = publicKey;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getPublicKeyId() {
        return publicKeyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSentEventSuccessfulDeviceLinkCompletion)) return false;
        ServerSentEventSuccessfulDeviceLinkCompletion that = (ServerSentEventSuccessfulDeviceLinkCompletion) o;
        return Objects.equals(getDeviceId(), that.getDeviceId()) &&
                Objects.equals(getPublicKeyId(), that.getPublicKeyId()) &&
                Objects.equals(getPublicKey(), that.getPublicKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeviceId(), getPublicKeyId(), getPublicKey());
    }

    @Override
    public String toString() {
        return "ServerSentEventSuccessfulDeviceLinkCompletion{" +
                "deviceId='" + deviceId + '\'' +
                ", publicKeyId='" + publicKeyId + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
