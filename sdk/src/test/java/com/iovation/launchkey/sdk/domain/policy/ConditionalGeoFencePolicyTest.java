package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.error.InvalidPolicyAttributes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ConditionalGeoFencePolicyTest {

    @Test
    public void getDenyRootedJailbroken() throws Exception {
        assertTrue(new ConditionalGeoFencePolicy(true, false, null, new MethodAmountPolicy(0), new MethodAmountPolicy(0)).getDenyRootedJailbroken());
    }

    @Test
    public void getDenyEmulatorSimulator() throws Exception {
        assertTrue(new ConditionalGeoFencePolicy(false, true, null, new MethodAmountPolicy(0), new MethodAmountPolicy(0)).getDenyEmulatorSimulator());
    }

    @Test
    public void getInPolicyRooted() throws Exception {
        Policy inPolicy = new MethodAmountPolicy(false, false, null, 0);
        ConditionalGeoFencePolicy conGeoPolicy = new ConditionalGeoFencePolicy(false, false, null, inPolicy, new MethodAmountPolicy(0));
        assertEquals(conGeoPolicy.getInside(), inPolicy);
        assertFalse(conGeoPolicy.getInside().getDenyRootedJailbroken());
    }

    @Test
    public void getInPolicyEmulator() throws Exception {
        Policy inPolicy = new MethodAmountPolicy(false, false, null, 0);
        ConditionalGeoFencePolicy conGeoPolicy = new ConditionalGeoFencePolicy(false, false, null, inPolicy, new MethodAmountPolicy(0));
        assertEquals(conGeoPolicy.getInside(), inPolicy);
        assertFalse(conGeoPolicy.getInside().getDenyEmulatorSimulator());
    }

    @Test
    public void getOutPolicyRooted() throws Exception {
        Policy outPolicy = new MethodAmountPolicy(false, false, null, 0);
        ConditionalGeoFencePolicy conGeoPolicy = new ConditionalGeoFencePolicy(false, false, null, new MethodAmountPolicy(0), outPolicy);
        assertEquals(conGeoPolicy.getOutside(), outPolicy);
        assertFalse(conGeoPolicy.getOutside().getDenyRootedJailbroken());
    }

    @Test
    public void getOutPolicyEmulator() throws Exception {
        Policy outPolicy = new MethodAmountPolicy(false, false, null, 0);
        ConditionalGeoFencePolicy conGeoPolicy = new ConditionalGeoFencePolicy(false, false, null, new MethodAmountPolicy(0), outPolicy);
        assertEquals(conGeoPolicy.getOutside(), outPolicy);
        assertFalse(conGeoPolicy.getOutside().getDenyEmulatorSimulator());
    }

    @Test
    public void getFences() throws Exception {
        List<Fence> fences = new ArrayList<>();
        ConditionalGeoFencePolicy conGeoPolicy = new ConditionalGeoFencePolicy(false, false, fences, new MethodAmountPolicy(0), new MethodAmountPolicy(0));
        assertEquals(fences, conGeoPolicy.getFences());
    }

    @Test(expected = InvalidPolicyAttributes.class)
    public void insideCannotBeNull() throws Exception {
        new ConditionalGeoFencePolicy(false, false, null, null, new MethodAmountPolicy(0));
    }

    @Test(expected = InvalidPolicyAttributes.class)
    public void outsideCannotBeNull() throws Exception {
        new ConditionalGeoFencePolicy(false, false, null, new MethodAmountPolicy(0), null);
    }

    @Test(expected = InvalidPolicyAttributes.class)
    public void insideCannotBeMethodAmountOrFactorsPolicy() throws Exception {
        new ConditionalGeoFencePolicy(false, false, null, mock(Policy.class), null);
    }

    @Test(expected = InvalidPolicyAttributes.class)
    public void outsideCannotBeMethodAmountOrFactorsPolicy() throws Exception {
        new ConditionalGeoFencePolicy(false, false, null, null, mock(Policy.class));
    }

    @Test
    public void insideCanBeMethodAmountPolicy() throws Exception {
        Policy actual = new ConditionalGeoFencePolicy(false, false, null, new MethodAmountPolicy(0), new MethodAmountPolicy(0)).getInside();
        assertThat(actual, instanceOf(MethodAmountPolicy.class));
    }

    @Test
    public void insideCanBeFactorsPolicy() throws Exception {
        Policy actual = new ConditionalGeoFencePolicy(false, false, null, new FactorsPolicy(false, false, false), new MethodAmountPolicy(0)).getInside();
        assertThat(actual, instanceOf(FactorsPolicy.class));
    }

    @Test
    public void outsideCanBeMethodAmountPolicy() throws Exception {
        Policy actual = new ConditionalGeoFencePolicy(false, false, null, new MethodAmountPolicy(0), new MethodAmountPolicy(0)).getOutside();
        assertThat(actual, instanceOf(MethodAmountPolicy.class));
    }

    @Test
    public void outsideCanBeFactorsPolicy() throws Exception {
        Policy actual = new ConditionalGeoFencePolicy(false, false, null, new MethodAmountPolicy(0), new FactorsPolicy(false, false, false)).getOutside();
        assertThat(actual, instanceOf(FactorsPolicy.class));
    }
}
