package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.iovation.launchkey.sdk.domain.policy.Fence;

import java.util.List;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="class")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "inside", "outside"})
public class Policy implements PolicyAdapter {

    @JsonProperty("type")
    private final String policyType;

    @JsonProperty("deny_rooted_jailbroken")
    private final Boolean denyRootedJailbroken;

    @JsonProperty("deny_emulator_simulator")
    private final Boolean denyEmulatorSimulator;

    @JsonProperty("fences")
    private final List<Fence> fences;

    @JsonProperty("inside")
    private final Policy inPolicy;

    @JsonProperty("outside")
    private final Policy outPolicy;

    @JsonProperty("factors")
    private final List<String> factors;

    public Policy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, Policy inPolicy, Policy outPolicy, String policyType, List<String> factors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
        this.policyType = policyType;
        this.factors = factors;
    }

    public String getPolicyType() {
        return policyType;
    }

    public Boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    public Boolean getDenyEmulatorSimulator() {
        return denyEmulatorSimulator;
    }

    public List<Fence> getFences() {
        return fences;
    }

    public Policy getInPolicy() {
        return inPolicy;
    }

    public Policy getOutPolicy() {
        return outPolicy;
    }

    public List<String> getFactors() {
        return factors;
    }
}
