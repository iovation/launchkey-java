package com.iovation.launchkey.sdk.domain.service;

import com.iovation.launchkey.sdk.domain.policy.Fence;

import java.util.List;
import java.util.Objects;

public class AuthorizationResponsePolicy {

    private final Requirement requirement;
    private final int amount;
    private final List<Fence> fences;
    private final boolean inherenceRequired;
    private final boolean knowledgeRequired;
    private final boolean possessionRequired;

    public AuthorizationResponsePolicy(Requirement requirement, int amount, List<Fence> fences,
                                       boolean inherenceRequired, boolean knowledgeRequired,
                                       boolean possessionRequired) {
        this.requirement = requirement;
        this.amount = amount;
        this.fences = fences;
        this.inherenceRequired = inherenceRequired;
        this.knowledgeRequired = knowledgeRequired;
        this.possessionRequired = possessionRequired;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public int getAmount() {
        return amount;
    }

    public List<Fence> getFences() {
        return fences;
    }

    public boolean wasInherenceRequired() {
        return inherenceRequired;
    }

    public boolean wasKnowledgeRequired() {
        return knowledgeRequired;
    }

    public boolean wasPossessionRequired() {
        return possessionRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationResponsePolicy that = (AuthorizationResponsePolicy) o;
        return amount == that.amount &&
                inherenceRequired == that.inherenceRequired &&
                knowledgeRequired == that.knowledgeRequired &&
                possessionRequired == that.possessionRequired &&
                requirement == that.requirement &&
                Objects.equals(fences, that.fences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requirement, amount, fences, inherenceRequired, knowledgeRequired, possessionRequired);
    }

    @Override
    public String toString() {
        return "AuthorizationResponsePolicy{" +
                "requirement=" + requirement +
                ", amount=" + amount +
                ", fences=" + fences +
                ", inherenceRequired=" + inherenceRequired +
                ", knowledgeRequired=" + knowledgeRequired +
                ", possessionRequired=" + possessionRequired +
                '}';
    }
}
