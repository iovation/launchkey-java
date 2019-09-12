package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;

import java.util.List;

public class ConditionalGeoFencePolicy implements Policy {

    private Boolean denyRootedJailbroken = false;
    private Boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private Policy inPolicy;
    private Policy outPolicy;

    public ConditionalGeoFencePolicy() {
        this.fences = null;
        this.inPolicy = null;
        this.outPolicy = null;
    }

    public ConditionalGeoFencePolicy(Boolean denyRootedJailbroken, Boolean denyEmulatorSimulator, List<Fence> fences, Policy inPolicy, Policy outPolicy) throws InvalidPolicyAttributes {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        if ((inPolicy instanceof FactorsPolicy) || (inPolicy instanceof MethodAmountPolicy) || (inPolicy == null)) {
            this.inPolicy = inPolicy;
        } else {
            throw new InvalidPolicyAttributes("inPolicy must be of type FactorsPolicy or MethodAmountPolicy", null, null);
        }
        if ((outPolicy instanceof FactorsPolicy) || (outPolicy instanceof MethodAmountPolicy) || (outPolicy == null)) {
            this.outPolicy = outPolicy;
        } else {
            throw new InvalidPolicyAttributes("outPolicy must be of type FactorsPolicy or MethodAmountPolicy", null, null);
        }
        // TODO: Check fences and sub policy booleans
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

    public Policy getInPolicy() {
        return this.inPolicy;
    }

    public Policy getOutPolicy() {
        return this.outPolicy;
    }

}
