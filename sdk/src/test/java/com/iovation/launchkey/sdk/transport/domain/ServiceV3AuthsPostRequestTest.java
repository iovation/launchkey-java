package com.iovation.launchkey.sdk.transport.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class ServiceV3AuthsPostRequestTest {

    @Test
    public void getUsername() {
        assertEquals("username", new ServiceV3AuthsPostRequest("username", null, null, null, null, null, null).getUsername());
    }

    @Test
    public void getPolicy() {
        AuthPolicy expected = new AuthPolicy(null, null, null, null, null);
        assertEquals(expected, new ServiceV3AuthsPostRequest(null, expected, null, null, null, null, null).getPolicy());
    }

    @Test
    public void getContext() {
        assertEquals("CTX", new ServiceV3AuthsPostRequest(null, null, "CTX", null, null, null, null).getContext());
    }

    @Test
    public void getTitle() {
        assertEquals("Title", new ServiceV3AuthsPostRequest(null, null, null, "Title", null, null, null).getTitle());
    }

    @Test
    public void getTTL() {
        assertEquals(Integer.valueOf(999), new ServiceV3AuthsPostRequest(null, null, null, null, 999, null, null).getTTL());
    }

    @Test
    public void getPushTitle() {
        assertEquals("PT", new ServiceV3AuthsPostRequest(null, null, null, null, null, "PT", null).getPushTitle());
    }

    @Test
    public void getPushBody() {
        assertEquals("PB", new ServiceV3AuthsPostRequest(null, null, null, null, null, null, "PB").getPushBody());
    }

    @Test
    public void equivalentAreEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM"),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM")
        );
    }

    @Test
    public void notEquivalentAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM"),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U", 2, "PU", "PN")
        );
    }

    @Test
    public void notEquivalentForPushTitleAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM"),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PM")
        );
    }

    @Test
    public void notEquivalentForPushMessageAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PM"),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PN")
        );
    }

    @Test
    public void hashCodeIsSameForEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM").hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM").hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM").hashCode(),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U", 2, "PU", "PN").hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalentPushTitle() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM").hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PM").hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalentPushMessage() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PM").hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PN").hashCode()
        );
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new ServiceV3AuthsPostRequest("A", null, "C", "T", 1, "PT", "PM").toString(),
                containsString(ServiceV3AuthsPostRequest.class.getSimpleName())
        );
    }
}