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
    private String policyType;

    @JsonProperty("deny_rooted_jailbroken")
    private Boolean denyRootedJailbroken;

    @JsonProperty("deny_emulator_simulator")
    private Boolean denyEmulatorSimulator;

    @JsonProperty("fences")
    private List<Fence> fences;

    @JsonProperty("inside")
    private Policy inPolicy;

    @JsonProperty("outside")
    private Policy outPolicy;

    public Policy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, Policy inPolicy, Policy outPolicy, String policyType) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
        this.policyType = policyType;
    }
}
