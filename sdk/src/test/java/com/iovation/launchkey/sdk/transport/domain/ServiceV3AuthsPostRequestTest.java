package com.iovation.launchkey.sdk.transport.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class ServiceV3AuthsPostRequestTest {

    @Test
    public void getUsername() {
        assertEquals("username", new ServiceV3AuthsPostRequest("username", null, null, null, null, null, null, null).getUsername());
    }

    @Test
    public void getPolicy() {
        AuthPolicy expected = new AuthPolicy(null, null, null, null, null);
        assertEquals(expected, new ServiceV3AuthsPostRequest(null, expected, null, null, null, null, null, null).getPolicy());
    }

    @Test
    public void getContext() {
        assertEquals("CTX", new ServiceV3AuthsPostRequest(null, null, "CTX", null, null, null, null, null).getContext());
    }

    @Test
    public void getTitle() {
        assertEquals("Title", new ServiceV3AuthsPostRequest(null, null, null, "Title", null, null, null, null).getTitle());
    }

    @Test
    public void getTTL() {
        assertEquals(Integer.valueOf(999), new ServiceV3AuthsPostRequest(null, null, null, null, 999, null, null, null).getTTL());
    }

    @Test
    public void getPushTitle() {
        assertEquals("PT", new ServiceV3AuthsPostRequest(null, null, null, null, null, "PT", null, null).getPushTitle());
    }

    @Test
    public void getPushBody() {
        assertEquals("PB", new ServiceV3AuthsPostRequest(null, null, null, null, null, null, "PB", null).getPushBody());
    }

    @Test
    public void getReasons() {
        List<DenialReason> expected = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertEquals(expected, new ServiceV3AuthsPostRequest(null, null, null, null, null, null, null, expected).getDenialReasons());
    }

    @Test
    public void equivalentAreEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons)
        );
    }

    @Test
    public void notEquivalentAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U", 2, "PU", "PC", reasons)
        );
    }

    @Test
    public void notEquivalentForPushTitleAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PB", reasons)
        );
    }

    @Test
    public void notEquivalentForPushBodyAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PB", reasons),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PC", reasons)
        );
    }

    @Test
    public void notEquivalentForReasonsAreNotEqual() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> leftReasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        List<DenialReason> rightReasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("2", "b", false));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PB", leftReasons),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PB", rightReasons)
        );
    }

    @Test
    public void hashCodeIsSameForEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons).hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons).hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalent() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons).hashCode(),
                new ServiceV3AuthsPostRequest("B", policy, "D", "U", 2, "PU", "PC", reasons).hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalentPushTitle() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons).hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PU", "PB", reasons).hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalentPushBody() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> reasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", reasons).hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PC", reasons).hashCode()
        );
    }

    @Test
    public void hashCodeNotSameForNotEquivalentDenialReasons() {
        AuthPolicy policy = new AuthPolicy(null, null, null, null, null);
        List<DenialReason> leftReasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("1", "a", true));
        }};
        List<DenialReason> rightReasons = new ArrayList<DenialReason>(){{
            add(new DenialReason("2", "b", false));
        }};
        assertNotEquals(
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", leftReasons).hashCode(),
                new ServiceV3AuthsPostRequest("A", policy, "C", "T", 1, "PT", "PB", rightReasons).hashCode()
        );
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new ServiceV3AuthsPostRequest("A", null, "C", "T", 1, "PT", "PB", null).toString(),
                containsString(ServiceV3AuthsPostRequest.class.getSimpleName())
        );
    }
}