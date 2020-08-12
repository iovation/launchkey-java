package com.iovation.launchkey.sdk.transport.domain;

import java.util.Objects;

public class ServiceV3TotpPostRequest {
    private final String identifier;
    private final String otp;

    public ServiceV3TotpPostRequest(String identifier, String otp) {
        this.identifier = identifier;
        this.otp = otp;
    }

    public String getIdentifier(){
        return identifier;
    }

    public String getOtp(){
        return otp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceV3TotpPostRequest that = (ServiceV3TotpPostRequest) o;
        return Objects.equals(identifier, that.identifier) &&
                Objects.equals(otp, that.otp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, otp);
    }

    @Override
    public String toString() {
        return "ServiceV3TotpPostRequest{" +
                "identifier='" + identifier + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
