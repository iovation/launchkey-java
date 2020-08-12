package com.iovation.launchkey.sdk.transport.domain;

import java.util.Objects;

public class DirectoryV3TotpPostRequest {
    private final String identifer;

    public DirectoryV3TotpPostRequest(String identifer) {
        this.identifer = identifer;
    }

    public String getIdentifier() {
        return identifer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryV3TotpPostRequest that = (DirectoryV3TotpPostRequest) o;
        return Objects.equals(identifer, that.identifer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifer);
    }

    @Override
    public String toString() {
        return "DirectoryV3TotpPostRequest{" +
                "identifer='" + identifer + '\'' +
                '}';
    }
}
