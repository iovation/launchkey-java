package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MethodAmountPolicyTest {

    @Test
    public void getDenyRootedJailbroken() throws Exception {
        assertTrue(new MethodAmountPolicy(true,false,null).getDenyRootedJailbroken());
        assertFalse(new MethodAmountPolicy().getDenyEmulatorSimulator());
    }

    @Test
    public void getDenyEmulatorSimulator() throws Exception {
        assertTrue(new MethodAmountPolicy(false,true,null).getDenyEmulatorSimulator());
    }

    @Test
    public void getFences() throws Exception {
        List<Fence> fences = new ArrayList<>();
        fences.add(new GeoCircleFence("aFence", 0,0,5));
        MethodAmountPolicy methodPolicy = new MethodAmountPolicy(false,false, fences);
        assertEquals(fences,methodPolicy.getFences());
    }
}
