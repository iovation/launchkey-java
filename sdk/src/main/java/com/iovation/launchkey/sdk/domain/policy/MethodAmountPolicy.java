package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public class MethodAmountPolicy implements Policy, InOrOutPolicy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;

    @Override
    public String getPolicyType() {
        return "METHOD_AMOUNT";
    }

    @Override
    public Boolean getDenyRootedJailbroken() {
        return this.denyRootedJailbroken;
    }

    @Override
    public void setDenyRootedJailbroken(Boolean denyRootedJailbroken) {
        this.denyRootedJailbroken = denyRootedJailbroken;
    }

    @Override
    public Boolean getDenyEmulatorSimulator() {
        return this.denyEmulatorSimulator;
    }

    @Override
    public void setDenyEmulatorSimulator(Boolean denyEmulatorSimulator) {
        this.denyEmulatorSimulator = denyEmulatorSimulator;
    }

    @Override
    public List<Fence> getFences() {
        return this.fences;
    }

    @Override
    public void setFences(List<Fence> fences) {
        this.fences = fences;
    }

}
