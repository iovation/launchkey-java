package com.iovation.launchkey.sdk.integration.managers;

import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;

import com.google.inject.Singleton;
import cucumber.api.java.After;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores context of policy objects when running feature files
 */
@Singleton
public class PolicyCache {

    public Policy cachedPolicy;

    public void createMethodAmountPolicy() {
        cachedPolicy = new MethodAmountPolicy();
    }

    public void createFactorsPolicy() {
        cachedPolicy = new FactorsPolicy();
    }

    public void createConditionalGeoFencePolicy() {
        cachedPolicy = new ConditionalGeoFencePolicy();
    }

    public void setDenyRootedJailBroken(Boolean newValue) throws InvalidPolicyAttributes {
        if (cachedPolicy == null) {
            return;
        }
        if (cachedPolicy instanceof MethodAmountPolicy) {
            cachedPolicy = new MethodAmountPolicy(newValue,cachedPolicy.getDenyEmulatorSimulator(),cachedPolicy.getFences(),((MethodAmountPolicy) cachedPolicy).getAmount());
        }
        else if (cachedPolicy instanceof FactorsPolicy) {
            cachedPolicy = new FactorsPolicy(newValue,cachedPolicy.getDenyEmulatorSimulator(),cachedPolicy.getFences(),((FactorsPolicy) cachedPolicy).getFactors());
        }
        else if (cachedPolicy instanceof ConditionalGeoFencePolicy) {
            cachedPolicy = new ConditionalGeoFencePolicy(newValue,cachedPolicy.getDenyEmulatorSimulator(),cachedPolicy.getFences(), ((ConditionalGeoFencePolicy) cachedPolicy).getInPolicy(), ((ConditionalGeoFencePolicy) cachedPolicy).getOutPolicy());
        }
    }

    public void setDenyEmulatorSimulator(Boolean newValue) throws InvalidPolicyAttributes {
        if (cachedPolicy == null) {
            return;
        }
        if (cachedPolicy instanceof MethodAmountPolicy) {
            cachedPolicy = new MethodAmountPolicy(cachedPolicy.getDenyRootedJailbroken(),newValue,cachedPolicy.getFences(),((MethodAmountPolicy) cachedPolicy).getAmount());
        }
        else if (cachedPolicy instanceof FactorsPolicy) {
            cachedPolicy = new FactorsPolicy(cachedPolicy.getDenyRootedJailbroken(),newValue,cachedPolicy.getFences(),((FactorsPolicy) cachedPolicy).getFactors());
        }
        else if (cachedPolicy instanceof ConditionalGeoFencePolicy) {
            cachedPolicy = new ConditionalGeoFencePolicy(cachedPolicy.getDenyRootedJailbroken(),newValue,cachedPolicy.getFences(), ((ConditionalGeoFencePolicy) cachedPolicy).getInPolicy(), ((ConditionalGeoFencePolicy) cachedPolicy).getOutPolicy());
        }
    }

    public void addFences(List<Fence> newFences) throws InvalidPolicyAttributes {
        if (cachedPolicy == null) {
            return;
        }
        ArrayList<Fence> fences = new ArrayList<>();
        if (cachedPolicy.getFences() != null) {
            fences.addAll(cachedPolicy.getFences());
        }
        fences.addAll(newFences);
        if (cachedPolicy instanceof MethodAmountPolicy) {
            cachedPolicy = new MethodAmountPolicy(cachedPolicy.getDenyRootedJailbroken(),cachedPolicy.getDenyEmulatorSimulator(),fences,((MethodAmountPolicy) cachedPolicy).getAmount());
        }
        else if (cachedPolicy instanceof FactorsPolicy) {
            cachedPolicy = new FactorsPolicy(cachedPolicy.getDenyRootedJailbroken(),cachedPolicy.getDenyEmulatorSimulator(),fences,((FactorsPolicy) cachedPolicy).getFactors());
        }
        else if (cachedPolicy instanceof ConditionalGeoFencePolicy) {
            cachedPolicy = new ConditionalGeoFencePolicy(cachedPolicy.getDenyRootedJailbroken(),cachedPolicy.getDenyEmulatorSimulator(),fences, ((ConditionalGeoFencePolicy) cachedPolicy).getInPolicy(), ((ConditionalGeoFencePolicy) cachedPolicy).getOutPolicy());
        }
    }

}
