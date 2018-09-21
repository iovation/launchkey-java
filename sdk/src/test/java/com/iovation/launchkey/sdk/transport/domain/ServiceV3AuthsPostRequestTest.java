package com.iovation.launchkey.sdk.transport.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class ServiceV3AuthsPostRequestTest {

    @Test
    public void getUsername() {
        assertEquals("username", new ServiceV3AuthsPostRequest("username", null, null, null, null).getUsername());
    }

    @Test
    public void getPolicy() {
        AuthPolicy expected = new AuthPolicy(null, null, null, null, null);
        assertEquals(expected, new ServiceV3AuthsPostRequest(null, expected, null, null, null).getPolicy());
    }

    @Test
    public void getContext() {
        assertEquals("CTX", new ServiceV3AuthsPostRequest(null, null, "CTX", null, null).getContext());
    }

    @Test
    public void getTitle() {
        assertEquals("Title", new ServiceV3AuthsPostRequest(null, null, null, "Title", null).getTitle());
    }

    @Test
    public void getTTL() {
        assertEquals(Integer.valueOf(999), new ServiceV3AuthsPostRequest(null, null, null, null,999).getTTL());
    }

    @Test
    public void equivalentAreEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1)
        );
    }

    @Test
    public void notEquivalentAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U", 2)
        );
    }

    @Test
    public void hashCodeIsSameForEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1).hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1).hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1).hashCode(),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U", 2).hashCode()
        );
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new ServiceV3AuthsPostRequest("A", null, "C", "T", 1).toString(),
                containsString(ServiceV3AuthsPostRequest.class.getSimpleName())
        );
    }
}