package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "amount"})
public class MethodAmountPolicy extends Policy {

    @JsonProperty("amount")
    private final int amount;

    @JsonCreator
    public MethodAmountPolicy(@JsonProperty("deny_rooted_jailbroken") Boolean denyRootedJailbroken,
                              @JsonProperty("deny_emulator_simulator") Boolean denyEmulatorSimulator,
                              @JsonProperty("fences") List<Fence> fences,
                              @JsonProperty("amount") int amount) {
        super("METHOD_AMOUNT",denyRootedJailbroken,denyEmulatorSimulator, fences);
        this.amount = amount;
    }



    public int getAmount() { return amount; }
}
