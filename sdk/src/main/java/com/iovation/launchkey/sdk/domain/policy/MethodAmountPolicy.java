package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.domain.service.AuthPolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
        if (fences != null) {
            return Collections.unmodifiableList(new ArrayList<>(fences));
        } else {
            return null;
        }
    }

    public int getAmount() { return amount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodAmountPolicy)) return false;
        MethodAmountPolicy that = (MethodAmountPolicy) o;
        return Objects.equals(getFences(), that.getFences()) &&
                Objects.equals(getDenyEmulatorSimulator(), that.getDenyEmulatorSimulator()) &&
                Objects.equals(getDenyRootedJailbroken(), that.getDenyRootedJailbroken()) &&
                Objects.equals(getAmount(), that.getAmount());
    }

}
