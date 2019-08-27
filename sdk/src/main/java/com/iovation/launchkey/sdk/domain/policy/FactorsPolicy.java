package com.iovation.launchkey.sdk.domain.policy;

import java.util.List;
import java.util.Set;

public class FactorsPolicy implements Policy, InOrOutPolicy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private Set<Factor> factors;

    @Override
    public String getPolicyType() {
        return "FACTORS";
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

    public void setFactors(Set<Factor> factors) {
        this.factors = factors;
    }

    public Set<Factor> getFactors() {
        return this.factors;
    }

}
