package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponsePolicy {
    private final String requirement;
    private final int amount;
    private final List<String> types;
    private final List<Fence> fences;

    @JsonCreator
    public AuthResponsePolicy(
            @JsonProperty("requirement") String requirement, @JsonProperty("amount") int amount,
            @JsonProperty("types") List<String> types, @JsonProperty("geofences") List<Fence> fences
    ) {
        this.requirement = requirement;
        this.amount = amount;
        this.types = types;
        this.fences = fences;
    }

    public String getRequirement() {
        return requirement;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getTypes() {
        return types;
    }

    public List<Fence> getFences() {
        return fences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthResponsePolicy that = (AuthResponsePolicy) o;
        return amount == that.amount &&
                Objects.equals(requirement, that.requirement) &&
                Objects.equals(types, that.types) &&
                Objects.equals(fences, that.fences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirement, amount, types, fences);
    }

    @Override
    public String toString() {
        return "AuthResponsePolicy{" +
                "requirement='" + requirement + '\'' +
                ", amount=" + amount +
                ", types=" + types +
                ", fences=" + fences +
                '}';
    }
}
