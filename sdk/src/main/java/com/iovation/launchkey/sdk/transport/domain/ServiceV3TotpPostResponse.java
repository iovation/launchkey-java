package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3TotpPostResponse {
    private final boolean valid;

    @JsonCreator
    public ServiceV3TotpPostResponse(@JsonProperty("valid") boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceV3TotpPostResponse that = (ServiceV3TotpPostResponse) o;
        return valid == that.valid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valid);
    }

    @Override
    public String toString() {
        return "ServiceV3TotpPostResponse{" +
                "valid=" + valid +
                '}';
    }
}
