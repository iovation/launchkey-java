package com.iovation.launchkey.sdk.domain.service;

import java.util.Objects;

public class DenialReason {
    private final String id;
    private final String reason;
    private final boolean fraud;

    /**
     * Denial reason to be presented to users during authentication when they deny the request. The ID
     * will be added to the {@link AuthorizationResponse} to determine which item was selected.
     * @param id Universally unique identifier for this reason. This must be unique in an authorization request
     * and should be unique globally for proper tracking of user selections.
     * @param reason Textual description of the reason that will be presented in a list for the user to select.
     * @param fraud Is the reason considered fraud
     */
    public DenialReason(String id, String reason, boolean fraud) {
        this.id = id;
        this.reason = reason;
        this.fraud = fraud;
    }

    /**
     * Get the reason identifier
     * @return Reason identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Get the reason text
     * @return Reason text
     */
    public String getReason() {
        return reason;
    }

    /**
     * Is this reason considered fraudulent
     * @return Fraudulent?
     */
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
