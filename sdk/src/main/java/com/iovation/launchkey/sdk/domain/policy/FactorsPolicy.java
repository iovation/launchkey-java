package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;
import java.util.Set;

public class FactorsPolicy implements Policy, InOrOutPolicy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private Set<Factor> factors;

    public FactorsPolicy() {
        this.denyRootedJailbroken = false;
        this.denyEmulatorSimulator = false;
        this.fences = null;
        this.factors = null;
    }

    public FactorsPolicy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, Set<Factor> factors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.factors = factors;
    }

    @Override
    public String getPolicyType() {
        return "FACTORS";
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

    public Set<Factor> getFactors() {
        return this.factors;
    }

}
