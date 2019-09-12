package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "amount"})
public class MethodAmountPolicy implements Policy {

    @JsonProperty("type")
    private final String policyType = "METHOD_AMOUNT";

    @JsonProperty("deny_rooted_jailbroken")
    private final Boolean denyRootedJailbroken;

    @JsonProperty("deny_emulator_simulator")
    private final Boolean denyEmulatorSimulator;

    @JsonProperty("fences")
    private final List<Fence> fences;

    @JsonProperty("amount")
    private final int amount;

    @JsonCreator
    public MethodAmountPolicy(@JsonProperty("deny_rooted_jailbroken") Boolean denyRootedJailbroken,
                              @JsonProperty("deny_emulator_simulator") Boolean denyEmulatorSimulator,
                              @JsonProperty("fences") List<Fence> fences,
                              @JsonProperty("amount") int amount) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.amount = amount;
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

    public int getAmount() { return amount; }
}
