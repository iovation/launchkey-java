package com.iovation.launchkey.sdk.domain.policy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MethodAmountPolicyTest {

    @Test
    public void getDenyRootedJailbroken() throws Exception {
        assertTrue(new MethodAmountPolicy(true,false,null, 0).getDenyRootedJailbroken());
    }

    @Test
    public void getDenyEmulatorSimulator() throws Exception {
        assertTrue(new MethodAmountPolicy(false,true,null,0).getDenyEmulatorSimulator());
    }

    @Test
    public void getAmount() throws Exception {
        assertEquals(new MethodAmountPolicy(false,true,null,5).getAmount(), 5);
    }

    @Test
    public void getFences() throws Exception {
        List<Fence> fences = new ArrayList<>();
        fences.add(new GeoCircleFence("aFence", 0,0,5));
        MethodAmountPolicy methodPolicy = new MethodAmountPolicy(false,false, fences, 0);
        assertEquals(fences,methodPolicy.getFences());
    }
}
