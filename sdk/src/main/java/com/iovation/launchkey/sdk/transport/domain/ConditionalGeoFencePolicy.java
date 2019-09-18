package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "inside", "outside"})
public class ConditionalGeoFencePolicy implements Policy {

    @JsonProperty("type")
    private final String policyType = "COND_GEO";

    @JsonProperty("deny_rooted_jailbroken")
    private final Boolean denyRootedJailbroken;

    @JsonProperty("deny_emulator_simulator")
    private final Boolean denyEmulatorSimulator;

    // TODO: Fence needs Jackson annotation
    @JsonProperty("fences")
    private final List<Fence> fences;

    @JsonProperty("inside")
    private final Policy inPolicy;

    @JsonProperty("outside")
    private final Policy outPolicy;

    @JsonCreator
    public ConditionalGeoFencePolicy(@JsonProperty("deny_rooted_jailbroken") Boolean denyRootedJailbroken,
                                     @JsonProperty("deny_emulator_simulator") Boolean denyEmulatorSimulator,
                                     @JsonProperty("fences") List<Fence> fences,
                                     @JsonProperty("inside") Policy inPolicy,
                                     @JsonProperty("outside") Policy outPolicy) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
    }

    @Override
    public String getPolicyType() {
        return policyType;
    }

    @Override
    public Boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    @Override
    public Boolean getDenyEmulatorSimulator() {
        return denyEmulatorSimulator;
    }

    @Override
    public List<Fence> getFences() {
        return fences;
    }

    public Policy getInPolicy() {
        return inPolicy;
    }

    public Policy getOutPolicy() {
        return outPolicy;
    }
}
