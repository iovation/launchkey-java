package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyResponseType {
    @JsonProperty("type")
    private final String policyType;

    public PolicyResponseType(@JsonProperty("type") String policyType) {
        this.policyType = policyType;
    }

    public String getPolicyType() {
        return policyType;
    }
}
