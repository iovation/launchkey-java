package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MethodAmountPolicy.class, name = "METHOD_AMOUNT"),
        @JsonSubTypes.Type(value = FactorsPolicy.class, name = "FACTORS"),
        @JsonSubTypes.Type(value = ConditionalGeoFencePolicy.class, name = "COND_GEO")
})
public class Policy implements PolicyAdapter {

    @JsonProperty("type")
    private final String policyType;

    @JsonProperty("fences")
    private final List<Fence> fences;

    @JsonProperty("deny_rooted_jailbroken")
    private final Boolean denyRootedJailbroken;

    @JsonProperty("deny_emulator_simulator")
    private final Boolean denyEmulatorSimulator;

    @JsonCreator
    public Policy(@JsonProperty("type") String policyTypeString,
                  @JsonProperty("deny_rooted_jailbroken") Boolean denyRootedJailbroken,
                  @JsonProperty("deny_emulator_simulator") Boolean denyEmulatorSimulator,
                  @JsonProperty("fences") List<Fence> fences
                  ) {
        this.policyType = policyTypeString;
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
    }

    public PolicyTypeEnum getPolicyTypeEnum() {
        if (policyType == null) {
            return PolicyTypeEnum.LEGACY;
        }
        else {
            return PolicyTypeEnum.valueOf(policyType);
        }
    }

    public String getPolicyType() {
        return policyType;
    }

    public List<Fence> getFences() {
        return fences;
    }

    public Boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    public Boolean getDenyEmulatorSimulator() {
        return denyEmulatorSimulator;
    }
}
