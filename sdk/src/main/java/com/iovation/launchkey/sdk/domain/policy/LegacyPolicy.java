package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LegacyPolicy implements Policy {

    private final int amount;
    private final Boolean inherence;
    private final Boolean knowledge;
    private final Boolean possession;
    private final boolean denyRootedJailbroken;
    private final List<GeoCircleFence> fences;
    private final List<ServicePolicy.TimeFence> timeFences;

    public LegacyPolicy(int amount, Boolean inherence, Boolean knowledge, Boolean possession, boolean denyRootedJailbroken, List<GeoCircleFence> fences, List<ServicePolicy.TimeFence> timeFences) {
        this.amount = amount;
        this.inherence = inherence;
        this.knowledge = knowledge;
        this.possession = possession;
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.fences = fences;
        this.timeFences = timeFences;
    }

    public int getAmount() {
        return amount;
    }

    public Boolean isInherenceRequired() {
        return inherence;
    }

    public Boolean isKnowledgeRequired() {
        return knowledge;
    }

    public Boolean isPossessionRequired() {
        return possession;
    }

    @Override
    public boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    @Override
    public boolean getDenyEmulatorSimulator() {
        return false;
    }

    public List<Fence> getFences() {
        if (fences != null) {
            return Collections.unmodifiableList(new ArrayList<Fence>(fences));
        }
        else {
            return null;
        }
    }

    public List<ServicePolicy.TimeFence> getTimeFences() {
        if (timeFences != null) {
            return Collections.unmodifiableList(timeFences);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LegacyPolicy)) return false;
        LegacyPolicy that = (LegacyPolicy) o;
        return Objects.equals(getFences(), that.getFences()) &&
                Objects.equals(getDenyEmulatorSimulator(), that.getDenyEmulatorSimulator()) &&
                Objects.equals(getDenyRootedJailbroken(), that.getDenyRootedJailbroken()) &&
                Objects.equals(isInherenceRequired(), that.isInherenceRequired()) &&
                Objects.equals(isKnowledgeRequired(), that.isKnowledgeRequired()) &&
                Objects.equals(isPossessionRequired(), that.isPossessionRequired()) &&
                Objects.equals(getTimeFences(), that.getTimeFences());
    }
}
