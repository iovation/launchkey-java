package com.iovation.launchkey.sdk.domain.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactorsPolicy implements Policy {

    private boolean denyRootedJailbroken = false;
    private boolean denyEmulatorSimulator = false;
    private boolean inherenceRequired = false;
    private boolean knowledgeRequired = false;
    private boolean possessionRequired = false;
    private List<Fence> fences;

    public FactorsPolicy() {
        this.fences = null;
    }

    public FactorsPolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, boolean inherenceRequired, boolean knowledgeRequired, boolean possessionRequired) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.inherenceRequired = inherenceRequired;
        this.knowledgeRequired = knowledgeRequired;
        this.possessionRequired = possessionRequired;
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

    public boolean isInherenceRequired() {
        return inherenceRequired;
    }

    public boolean isKnowledgeRequired() {
        return knowledgeRequired;
    }

    public boolean isPossessionRequired() {
        return possessionRequired;
    }
}
