package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

public class FactorPolicyTest {

    @Test
    public void getDenyRootedJailbroken() throws Exception {
        assertTrue(new FactorsPolicy(true,false,null,false,false,false).getDenyRootedJailbroken());
    }

    @Test
    public void getDenyEmulatorSimulator() throws Exception {
        assertTrue(new FactorsPolicy(false,true,null,false,false,false).getDenyEmulatorSimulator());
    }

    @Test
    public void getFences() throws Exception {
        List<Fence> fences = new ArrayList<>();
        fences.add(new GeoCircleFence("aFence", 0,0,5));
        FactorsPolicy factorsPolicy = new FactorsPolicy(false,false, fences, false,false,false);
        assertEquals(fences,factorsPolicy.getFences());
    }
}
