package com.iovation.launchkey.sdk.integration.managers;

import com.iovation.launchkey.sdk.domain.policy.Fence;

public class FenceCache {
    private Fence cachedFence;

    public Fence getCachedFence() {
        return cachedFence;
    }

    public void setCachedFence(Fence cachedFence) {
        this.cachedFence = cachedFence;
    }
}
