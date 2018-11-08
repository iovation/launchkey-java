package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3AuthsGetResponseDeviceJWE {
    private final String type;
    private final String reason;
    private final String denialReason;
    private final UUID authorizationRequestId;
    private final String deviceId;
    private final String[] servicePins;

    public ServiceV3AuthsGetResponseDeviceJWE(
                @JsonProperty(value = "type") String type,
                @JsonProperty(value = "reason") String reason,
                @JsonProperty(value = "denial_reason") String denialReason,
                @JsonProperty(value = "auth_request") UUID authorizationRequestId,
                @JsonProperty(value = "device_id") String deviceId,
                @JsonProperty(value = "service_pins") String[] servicePins) {
            this.type = type;
            this.reason = reason;
            this.denialReason = denialReason;
            this.authorizationRequestId = authorizationRequestId;
            this.deviceId = deviceId;
            this.servicePins = servicePins;
        }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getDenialReason() {
        return denialReason;
    }

    public UUID getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String[] getServicePins() {
        return servicePins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsGetResponseDeviceJWE)) return false;
        ServiceV3AuthsGetResponseDeviceJWE that = (ServiceV3AuthsGetResponseDeviceJWE) o;
        return Objects.equals(getType(), that.getType()) &&
                Objects.equals(getReason(), that.getReason()) &&
                Objects.equals(getDenialReason(), that.getDenialReason()) &&
                Objects.equals(getAuthorizationRequestId(), that.getAuthorizationRequestId()) &&
                Objects.equals(getDeviceId(), that.getDeviceId()) &&
                Arrays.equals(getServicePins(), that.getServicePins());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getType(), getReason(), getDenialReason(), getAuthorizationRequestId(), getDeviceId());
        result = 31 * result + Arrays.hashCode(getServicePins());
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsGetResponseDeviceJWE{" +
                "type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", denialReason='" + denialReason + '\'' +
                ", authorizationRequestId=" + authorizationRequestId +
                ", deviceId='" + deviceId + '\'' +
                ", servicePins=" + Arrays.toString(servicePins) +
                '}';
    }
}
