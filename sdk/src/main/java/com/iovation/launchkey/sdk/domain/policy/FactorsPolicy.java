package com.iovation.launchkey.sdk.domain.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FactorsPolicy implements Policy {

    private final boolean denyRootedJailbroken;
    private final boolean denyEmulatorSimulator;
    private final boolean inherenceRequired;
    private final boolean knowledgeRequired;
    private final boolean possessionRequired;
    private List<Fence> fences;

    public FactorsPolicy(boolean denyRootedJailbroken, boolean denyEmulatorSimulator, List<Fence> fences, boolean inherenceRequired, boolean knowledgeRequired, boolean possessionRequired) {
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.inherenceRequired = inherenceRequired;
        this.knowledgeRequired = knowledgeRequired;
        this.possessionRequired = possessionRequired;
    }

    public FactorsPolicy(boolean inherenceRequired, boolean knowledgeRequired, boolean possessionRequired) {
        this(false, false, null, inherenceRequired, knowledgeRequired, possessionRequired);
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
        if (fences != null) {
            return Collections.unmodifiableList(new ArrayList<>(fences));
        } else {
            return null;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FactorsPolicy)) return false;
        FactorsPolicy that = (FactorsPolicy) o;
        return Objects.equals(getFences(), that.getFences()) &&
                Objects.equals(getDenyEmulatorSimulator(), that.getDenyEmulatorSimulator()) &&
                Objects.equals(getDenyRootedJailbroken(), that.getDenyRootedJailbroken()) &&
                Objects.equals(isInherenceRequired(), that.isInherenceRequired()) &&
                Objects.equals(isKnowledgeRequired(), that.isKnowledgeRequired()) &&
                Objects.equals(isPossessionRequired(), that.isPossessionRequired());
    }
}
