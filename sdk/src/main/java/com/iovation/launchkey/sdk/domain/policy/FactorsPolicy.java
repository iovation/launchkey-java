package com.iovation.launchkey.sdk.domain.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: Remove list of factors and add boolean values with getters
public class FactorsPolicy implements Policy {

    private boolean denyRootedJailbroken = false;
    private boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private List<Factor> factors;


    public FactorsPolicy() {
        this.fences = null;
        this.factors = null;
    }

    public FactorsPolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, List<Factor> factors) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.factors = factors;
    }

    @Override
    public boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    @Override
    public boolean getDenyEmulatorSimulator() {
        return denyEmulatorSimulator;
    }

    @Override
    public List<Fence> getFences() {
        return Collections.unmodifiableList(new ArrayList<>(fences));
    }

    public List<Factor> getFactors() {
        return  Collections.unmodifiableList(new ArrayList<>(factors));
    }

}
