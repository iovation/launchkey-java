package com.iovation.launchkey.sdk.transport.domain;

import java.util.Objects;

public class DirectoryV3TotpDeleteRequest {
    private final String identifier;

    public DirectoryV3TotpDeleteRequest(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DirectoryV3TotpDeleteRequest that = (DirectoryV3TotpDeleteRequest) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "DirectoryV3TotpDeleteRequest{" +
                "identifier='" + identifier + '\'' +
                '}';
    }
}
