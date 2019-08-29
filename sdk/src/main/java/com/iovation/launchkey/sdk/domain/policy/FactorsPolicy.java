package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;

public class FactorsPolicy implements Policy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private List<Factor> factors;

    public FactorsPolicy() {
        this.denyRootedJailbroken = false;
        this.denyEmulatorSimulator = false;
        this.fences = null;
        this.factors = null;
    }

    public FactorsPolicy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, List<Factor> factors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.factors = factors;
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

    public List<Factor> getFactors() {
        return this.factors;
    }

}
