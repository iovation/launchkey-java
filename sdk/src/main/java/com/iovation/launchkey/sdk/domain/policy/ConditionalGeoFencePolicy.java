package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public class ConditionalGeoFencePolicy implements Policy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private InOrOutPolicy inPolicy;
    private InOrOutPolicy outPolicy;

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

    public void setInPolicy(InOrOutPolicy inPolicy) {
        this.inPolicy = inPolicy;
    }

    public InOrOutPolicy getInPolicy() {
        return this.inPolicy;
    }

    public void setOutPolicy(InOrOutPolicy outPolicy) {
        this.outPolicy = outPolicy;
    }

    public InOrOutPolicy getOutPolicy() {
        return this.outPolicy;
    }
}
