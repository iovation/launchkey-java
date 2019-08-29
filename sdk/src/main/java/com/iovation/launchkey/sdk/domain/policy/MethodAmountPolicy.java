package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public class MethodAmountPolicy implements Policy {

    private Boolean denyRootedJailbroken;
    private Boolean denyEmulatorSimulator;
    private List<Fence> fences;

    public MethodAmountPolicy() {
        this.denyEmulatorSimulator = false;
        this.denyEmulatorSimulator = false;
        this.fences = null;
    }

    public MethodAmountPolicy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
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

}
