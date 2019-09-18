package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "inside", "outside"})
public class ConditionalGeoFencePolicy extends Policy {

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
        super("COND_GEO",denyRootedJailbroken,denyEmulatorSimulator,fences);
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
    }

    public Policy getInPolicy() {
        return inPolicy;
    }

    public Policy getOutPolicy() {
        return outPolicy;
    }
}
