package com.iovation.launchkey.sdk.transport.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class ServiceV3AuthsPostRequestTest {

    @Test
    public void getUsername() {
        assertEquals("username", new ServiceV3AuthsPostRequest("username", null, null, null).getUsername());
    }

    @Test
    public void getPolicy() {
        AuthPolicy expected = new AuthPolicy(null, null, null, null, null);
        assertEquals(expected, new ServiceV3AuthsPostRequest(null, expected, null, null).getPolicy());
    }

    @Test
    public void getContext() {
        assertEquals("CTX", new ServiceV3AuthsPostRequest(null, null, "CTX", null).getContext());
    }

    @Test
    public void getTitle() {
        assertEquals("Title", new ServiceV3AuthsPostRequest(null, null, null, "Title").getTitle());
    }

    @Test
    public void equivalentAreEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T"),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T")
        );
    }

    @Test
    public void notEquivalentAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T"),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U")
        );
    }

    @Test
    public void hashCodeIsSameForEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T").hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T").hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T").hashCode(),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U").hashCode()
        );
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new ServiceV3AuthsPostRequest("A", null, "C", "T").toString(),
                containsString(ServiceV3AuthsPostRequest.class.getSimpleName())
        );
    }
}