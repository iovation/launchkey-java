package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public class ConditionalGeoFencePolicy implements Policy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private InOrOutPolicy inPolicy;
    private InOrOutPolicy outPolicy;

    public ConditionalGeoFencePolicy() {
        this.denyEmulatorSimulator = null;
        this.denyEmulatorSimulator = null;
        this.fences = null;
        this.inPolicy = null;
        this.outPolicy = null;
    }

    public ConditionalGeoFencePolicy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, InOrOutPolicy inPolicy, InOrOutPolicy outPolicy) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
    }

    @Override
    public String getPolicyType() {
        return "COND_GEO";
    }

    @Override
    public Boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    @Override
    public Boolean getDenyEmulatorSimulator() {
        return this.denyEmulatorSimulator;
    }

    @Override
    public List<Fence> getFences() {
        return this.fences;
    }

    public InOrOutPolicy getInPolicy() {
        return this.inPolicy;
    }

    public InOrOutPolicy getOutPolicy() {
        return this.outPolicy;
    }

}
