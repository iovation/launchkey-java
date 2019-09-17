package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.*;

@JsonPropertyOrder({"type"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyType {

    @JsonProperty("type")
    private final String policyType;

    @JsonCreator
    public PolicyType(@JsonProperty("type") String policyTypeString) {
        this.policyType = policyTypeString;
    }

    public PolicyTypeEnum getPolicyTypeEnum() {
        return PolicyTypeEnum.valueOf(policyType);
    }
}
