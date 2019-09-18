package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "factors"})
public class FactorsPolicy extends Policy {

    @JsonProperty("factors")
    private final List<String> factors;

    @JsonCreator
    public FactorsPolicy(@JsonProperty("deny_rooted_jailbroken") Boolean denyRootedJailbroken,
                         @JsonProperty("deny_emulator_simulator") Boolean denyEmulatorSimulator,
                         @JsonProperty("fences") List<Fence> fences,
                         @JsonProperty("factors") List<String> factors) {
        super("FACTORS",denyRootedJailbroken,denyEmulatorSimulator, fences);
        this.factors = factors;
    }

    public List<String> getFactors() {
        return factors;
    }
}
