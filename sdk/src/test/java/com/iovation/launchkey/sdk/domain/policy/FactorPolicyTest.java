package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class FactorPolicyTest {

    @Test
    public void getDenyRootedJailbroken() throws Exception {
        assertTrue(new FactorsPolicy(true,false,null,null).getDenyRootedJailbroken());
    }

    @Test
    public void getDenyEmulatorSimulator() throws Exception {
        assertTrue(new FactorsPolicy(false,true,null,null).getDenyEmulatorSimulator());
    }

    @Test
    public void getFences() throws Exception {
        List<Fence> fences = new ArrayList<>();
        fences.add(new GeoCircleFence("aFence", 0,0,5));
        FactorsPolicy factorsPolicy = new FactorsPolicy(false,false, fences, null);
        assertEquals(fences,factorsPolicy.getFences());
    }

    @Test
    public void getFactors() throws Exception {
        List<Factor> factors = new ArrayList<>();
        factors.add(Factor.INHERENCE);
        factors.add(Factor.POSSESSION);
        FactorsPolicy factorsPolicy = new FactorsPolicy(false,false, null, factors);
        assertEquals(factors,factorsPolicy.getFactors());
    }

    @Test
    public void defaultConstructor() throws Exception {
        FactorsPolicy policy = new FactorsPolicy();
        assertFalse(policy.getDenyEmulatorSimulator());
        assertFalse(policy.getDenyRootedJailbroken());
        assertNull(policy.getFences());
        assertNull(policy.getFactors());
    }
}
