package com.iovation.launchkey.sdk.integration.managers;

import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.domain.policy.Fence;
import com.iovation.launchkey.sdk.domain.policy.Policy;

@Singleton
public class PolicyContext {
    public MutablePolicy currentPolicy;
    public Fence fenceCache;

    public void setCurrentPolicy(Policy policy) {
        currentPolicy = new MutablePolicy(policy);
    }
}
