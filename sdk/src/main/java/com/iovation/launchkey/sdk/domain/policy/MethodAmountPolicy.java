package com.iovation.launchkey.sdk.domain.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MethodAmountPolicy implements Policy {

    private boolean denyRootedJailbroken = false;
    private boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private int amount;

    public MethodAmountPolicy() {
        this.fences = null;
        this.amount = 0;
    }

    public MethodAmountPolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, int amountOfFactors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.amount = amountOfFactors;
    }

    @Override
    public boolean getDenyRootedJailbroken() {
        return this.denyRootedJailbroken;
    }

    @Override
    public boolean getDenyEmulatorSimulator() {
        return this.denyEmulatorSimulator;
    }

    @Override
    public List<Fence> getFences() {
        return Collections.unmodifiableList(new ArrayList<>(fences));
    }

    public int getAmount() { return amount; }

}
