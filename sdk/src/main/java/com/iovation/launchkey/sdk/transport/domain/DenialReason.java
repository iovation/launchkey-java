package com.iovation.launchkey.sdk.transport.domain;

import java.util.Objects;

public class DenialReason {
    private final String id;
    private final String reason;
    private final boolean fraud;

    public DenialReason(String id, String reason, boolean fraud) {
        this.id = id;
        this.reason = reason;
        this.fraud = fraud;
    }

    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }

    public boolean isFraud() {
        return fraud;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DenialReason)) return false;
        DenialReason that = (DenialReason) o;
        return isFraud() == that.isFraud() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getReason(), that.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getReason(), isFraud());
    }

    @Override
    public String toString() {
        return "DenialReason{" +
                "id='" + id + '\'' +
                ", reason='" + reason + '\'' +
                ", fraud=" + fraud +
                '}';
    }
}
