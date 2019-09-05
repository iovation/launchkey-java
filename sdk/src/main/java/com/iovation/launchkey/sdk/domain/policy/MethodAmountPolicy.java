package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public class MethodAmountPolicy implements Policy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private int amount;

    public MethodAmountPolicy() {
        this.fences = null;
        this.amount = 0;
    }

    public MethodAmountPolicy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, int amountOfFactors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.amount = amountOfFactors;
    }

    @Override
    public Boolean getDenyRootedJailbroken() {
        return this.denyRootedJailbroken;
    }

    @Override
    public Boolean getDenyEmulatorSimulator() {
        return this.denyEmulatorSimulator;
    }

    @Override
    public List<Fence> getFences() {
        return this.fences;
    }

    public int getAmount() { return amount; }

}
