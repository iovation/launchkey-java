package com.iovation.launchkey.sdk.transport.domain;

import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ServiceV3AuthsPostResponseTest {

    private final static UUID auth_request = UUID.randomUUID();

    @Test
    public void getAuthRequest() {
        assertEquals(auth_request, new ServiceV3AuthsPostResponse(auth_request, null).getAuthRequest());
    }

    @Test
    public void getPushPackage() {
        String expected = "Push Package";
        assertEquals(expected, new ServiceV3AuthsPostResponse(null, expected).getPushPackage());
    }

    @Test
    public void equalsTrueWhenAllEqual() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Push Package");
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Push Package");
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsTrueWhenAllEmpty() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(null, null);
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(null, null);
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsFalseWhenAuthRequestDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package");
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package");
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsFalseWhenPushPackageDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Left Push Package");
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Right Push Package");
        assertFalse(left.equals(right));
    }

    @Test
    public void hashCodeEqualWhenAllEqual() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Push Package");
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Push Package");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeEqualWhenAllEmpty() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(null, null);
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(null, null);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenAuthRequestDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package");
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenPushPackageDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Left Push Package");
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Right Push Package");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new ServiceV3AuthsPostResponse(null, null).toString(),
                containsString(ServiceV3AuthsPostResponse.class.getSimpleName()));
    }
}