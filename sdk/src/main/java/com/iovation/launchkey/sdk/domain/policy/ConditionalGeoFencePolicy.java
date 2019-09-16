package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;
import com.iovation.launchkey.sdk.error.UnknownPolicyException;

import java.util.ArrayList;
import java.util.List;

public class ConditionalGeoFencePolicy implements Policy {

    private boolean denyRootedJailbroken = false;
    private boolean denyEmulatorSimulator = false;
    private List<Fence> fences;
    private Policy inPolicy;
    private Policy outPolicy;

    public ConditionalGeoFencePolicy() {
        this.fences = null;
        this.inPolicy = null;
        this.outPolicy = null;
    }

    public ConditionalGeoFencePolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, Policy inPolicy, Policy outPolicy) throws InvalidPolicyAttributes {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        verifySubPolicy(inPolicy);
        verifySubPolicy(outPolicy);
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
    }

    @Override
    public boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    @Override
    public boolean getDenyEmulatorSimulator() {
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

    private void verifySubPolicy(Policy subPolicy) throws InvalidPolicyAttributes {
        // Assert policy is either null or is of Type FactorsPolicy or MethodAmountPolicy
        // Assert denyRootedJailbroken and denyEmulatorSimulator are false
        // Assert no fences
        if (subPolicy == null) {
            return;
        }
        if ((subPolicy instanceof FactorsPolicy) || (subPolicy instanceof MethodAmountPolicy)) {
            if (subPolicy.getDenyEmulatorSimulator() || subPolicy.getDenyRootedJailbroken()) {
                throw new InvalidPolicyAttributes("Inside or Outside Policy objects cannot have denyEmulatorSimulator or denyRootedJailbroken set to true", null, null);
            }
            if (subPolicy.getFences() != null) {
                throw new InvalidPolicyAttributes("Fences are not supported on Inside or Outside Policy objects",null,null);
            }
        }
        else {
            throw new InvalidPolicyAttributes("Inside or Outside Policy objects must be of type FactorsPolicy or MethodAmountPolicy, or null if no policy", null, null);
        }
    }
}
