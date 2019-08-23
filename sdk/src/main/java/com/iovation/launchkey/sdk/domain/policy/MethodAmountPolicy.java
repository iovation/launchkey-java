package com.iovation.launchkey.sdk.domain.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MethodAmountPolicy implements Policy {

    private final boolean denyRootedJailbroken;
    private final boolean denyEmulatorSimulator;
    private final List<Fence> fences;
    private final int amount;

    public MethodAmountPolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, int amountOfFactors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.amount = amountOfFactors;
    }

    public MethodAmountPolicy(int amount) {
        this(false, false, null, amount);
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

    public int getAmount() {
        return amount;
    }

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
