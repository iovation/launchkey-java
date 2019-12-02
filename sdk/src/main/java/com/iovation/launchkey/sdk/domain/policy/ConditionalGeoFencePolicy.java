package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConditionalGeoFencePolicy implements Policy {

    private final boolean denyRootedJailbroken;
    private final boolean denyEmulatorSimulator;
    private final List<Fence> fences;
    private final Policy inside;
    private final Policy outside;

    public ConditionalGeoFencePolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, Policy inside, Policy outside) throws InvalidPolicyAttributes {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        verifySubPolicy(inside);
        verifySubPolicy(outside);
        this.inside = inside;
        this.outside = outside;
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
        if (fences != null) {
            return Collections.unmodifiableList(new ArrayList<Fence>(fences));
        }
        else {
            return null;
        }
    }

    /**
     * Get the policy to execute when the device in inside one of the fences
     * @return Inside policy
     */
    public Policy getInside() {
        return this.inside;
    }

    /**
     *
     * @return Outside policy
     */
    public Policy getOutside() {
        return this.outside;
    }

    private void verifySubPolicy(Policy subPolicy) throws InvalidPolicyAttributes {
        // Assert policy is either FactorsPolicy or MethodAmountPolicy
        // Assert denyRootedJailbroken and denyEmulatorSimulator are false
        // Assert no fences
        if (subPolicy == null) {
            throw new InvalidPolicyAttributes("Inside or Outside Policy objects must not be null", null, null);
        } else if (!(subPolicy instanceof FactorsPolicy) && !(subPolicy instanceof MethodAmountPolicy)) {
            throw new InvalidPolicyAttributes("Inside or Outside Policy objects must be of type FactorsPolicy or MethodAmountPolicy", null, null);
        }
        if (subPolicy.getDenyEmulatorSimulator() || subPolicy.getDenyRootedJailbroken()) {
            throw new InvalidPolicyAttributes("Inside or Outside Policy objects must have denyEmulatorSimulator or denyRootedJailbroken set to false", null, null);
        }
        if (subPolicy.getFences() != null) {
            throw new InvalidPolicyAttributes("Fences are not supported on Inside or Outside Policy objects",null,null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConditionalGeoFencePolicy)) return false;
        ConditionalGeoFencePolicy that = (ConditionalGeoFencePolicy) o;
        return Objects.equals(getFences(), that.getFences()) &&
                Objects.equals(getDenyEmulatorSimulator(), that.getDenyEmulatorSimulator()) &&
                Objects.equals(getDenyRootedJailbroken(), that.getDenyRootedJailbroken()) &&
                Objects.equals(getInside(), that.getInside()) &&
                Objects.equals(getOutside(), that.getOutside());
    }
}
