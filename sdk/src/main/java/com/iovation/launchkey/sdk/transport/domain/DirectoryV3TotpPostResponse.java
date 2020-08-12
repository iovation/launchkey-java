package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryV3TotpPostResponse {
    private final String secret;
    private final String algorithm;
    private final int period;
    private final int digits;

    @JsonCreator
    public DirectoryV3TotpPostResponse(@JsonProperty("secret") String secret,
                                       @JsonProperty("algorithm") String algorithm,
                                       @JsonProperty("period") int period,
                                       @JsonProperty("digits") int digits) {
        this.secret = secret;
        this.algorithm = algorithm;
        this.period = period;
        this.digits = digits;
    }

    public String getSecret() {
        return secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getPeriod() {
        return period;
    }

    public int getDigits() {
        return digits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryV3TotpPostResponse that = (DirectoryV3TotpPostResponse) o;
        return period == that.period &&
                digits == that.digits &&
                Objects.equals(secret, that.secret) &&
                Objects.equals(algorithm, that.algorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secret, algorithm, period, digits);
    }

    @Override
    public String toString() {
        return "DirectoryV3TotpPostResponse{" +
                "secret='" + secret + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", period=" + period +
                ", digits=" + digits +
                '}';
    }
}
