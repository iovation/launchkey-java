package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerSentEventDeviceLinkCompletion implements ServerSentEvent {
    private final UUID deviceId;
    private final String publicKeyId;
    private final String publicKey;

    @JsonCreator
    public ServerSentEventDeviceLinkCompletion(
            @JsonProperty("device_id") UUID deviceId,
            @JsonProperty("device_public_key_id") String publicKeyId,
            @JsonProperty("device_public_key") String publicKey) {
        this.deviceId = deviceId;
        this.publicKeyId = publicKeyId;
        this.publicKey = publicKey;
    }

    public UUID getDeviceId() {
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
        if (!(o instanceof ServerSentEventDeviceLinkCompletion)) return false;
        ServerSentEventDeviceLinkCompletion that = (ServerSentEventDeviceLinkCompletion) o;
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
        return "ServerSentEventDeviceLinkCompletion{" +
                "deviceId='" + deviceId + '\'' +
                ", publicKeyId='" + publicKeyId + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
