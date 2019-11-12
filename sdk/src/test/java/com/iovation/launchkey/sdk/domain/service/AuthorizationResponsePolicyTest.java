package com.iovation.launchkey.sdk.domain.service;

import com.iovation.launchkey.sdk.domain.policy.Fence;
import com.iovation.launchkey.sdk.domain.policy.GeoCircleFence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class AuthorizationResponsePolicyTest {

    private AuthorizationResponsePolicy policy;

    @Before
    public void setUp() throws Exception {
        policy = new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
    }

    @After
    public void tearDown() throws Exception {
        policy = null;
    }

    @Test
    public void getRequirement() {
        assertEquals(Requirement.OTHER, policy.getRequirement());
    }

    @Test
    public void getAmount() {
        assertEquals(1, policy.getAmount());
    }

    @Test
    public void getFences() {
        assertEquals(new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, policy.getFences());
    }

    @Test
    public void wasInherenceRequired() {
        policy =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, true, false, false);
        assertTrue(policy.wasInherenceRequired());
    }

    @Test
    public void wasKnowledgeRequired() {
        policy =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, false, true, false);
        assertTrue(policy.wasKnowledgeRequired());
    }

    @Test
    public void wasPossessionRequired() {
        policy =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, false, false, true);
        assertTrue(policy.wasPossessionRequired());
    }

    @Test
    public void testEqualsIsTrueForSameObject() {
        assertEquals(policy.hashCode(), policy.hashCode());
    }

    @Test
    public void testEqualsIsTrueForEquivalentObject() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
        assertEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsTrueForEquivalentObjectWithNullFences() {
        policy =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, true, true, true);
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, true, true, true);
        assertEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsFalseForDifferentRequirement() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.AMOUNT, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsFalseForDifferentAmount() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 2, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsFalseForDifferentFences() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(2.0, 2.0, 2.0));
        }}, true, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsFalseForDifferentInherenceRequired() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, false, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsFalseForDifferentKnowledgeRequired() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, false, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsIsFalseForDifferentPossessionRequired() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, false);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }


    @Test
    public void testHashcodeIsEqualForSameObject() {
        assertEquals(policy.hashCode(), policy.hashCode());
    }

    @Test
    public void testHashcodeIsEqualForEquivalentObject() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
        assertEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsEqualForEquivalentObjectWithNullFences() {
        policy =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, true, true, true);
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, null, true, true, true);
        assertEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsNotEqualForDifferentRequirement() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.AMOUNT, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsNotEqualForDifferentAmount() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 2, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsNotEqualForDifferentFences() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(2.0, 2.0, 2.0));
        }}, true, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsNotEqualForDifferentInherenceRequired() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, false, true, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsNotEqualForDifferentKnowledgeRequired() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, false, true);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testHashcodeIsNotEqualForDifferentPossessionRequired() {
        AuthorizationResponsePolicy other =  new AuthorizationResponsePolicy(Requirement.OTHER, 1, new ArrayList<Fence>(){{
            add(new GeoCircleFence(1.0, 2.0, 3.0));
        }}, true, true, false);
        assertNotEquals(policy.hashCode(), other.hashCode());
    }

    @Test
    public void testToStringHasClassName() {
        assertThat(policy.toString(), startsWith(policy.getClass().getSimpleName()));
    }
}