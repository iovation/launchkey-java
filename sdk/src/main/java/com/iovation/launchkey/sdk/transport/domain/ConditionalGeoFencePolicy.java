package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionalGeoFencePolicy)) return false;
        ConditionalGeoFencePolicy that = (ConditionalGeoFencePolicy) o;
        return Objects.equals(getFences(), that.getFences()) &&
                Objects.equals(getDenyEmulatorSimulator(), that.getDenyEmulatorSimulator()) &&
                Objects.equals(getDenyRootedJailbroken(), that.getDenyRootedJailbroken()) &&
                Objects.equals(getInPolicy(), that.getInPolicy()) &&
                Objects.equals(getOutPolicy(), that.getOutPolicy());
    }
}
