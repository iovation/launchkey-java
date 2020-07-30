package com.iovation.launchkey.sdk.domain;

import java.util.Objects;

public class DirectoryUserTotp {
    private final String secret;
    private final String algorithm;
    private final int digits;
    private final int period;

    public DirectoryUserTotp(String secret, String algorithm, int period, int digits) {

        this.secret = secret;
        this.algorithm = algorithm;
        this.digits = digits;
        this.period = period;
    }

    public String getSecret() {
        return secret;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public int getDigits() {
        return digits;
    }

    public int getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryUserTotp that = (DirectoryUserTotp) o;
        return digits == that.digits &&
                period == that.period &&
                Objects.equals(secret, that.secret) &&
                Objects.equals(algorithm, that.algorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(secret, algorithm, digits, period);
    }

    @Override
    public String toString() {
        return "DirectoryUserTotp{" +
                "secret='" + secret + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", digits=" + digits +
                ", period=" + period +
                '}';
    }
}
