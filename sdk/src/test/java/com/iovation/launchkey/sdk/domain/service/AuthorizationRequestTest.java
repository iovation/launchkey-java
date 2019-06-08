package com.iovation.launchkey.sdk.domain.service;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class AuthorizationRequestTest {

    @Test
    public void getId() {
        String expected = "ID";
        assertEquals(expected, new AuthorizationRequest(expected, null).getId());
    }

    @Test
    public void getPushPackage() {
        String expected = "push package";
        assertEquals(expected, new AuthorizationRequest(null, expected).getPushPackage());
    }

    @Test
    public void equalsTrueWhenAllItemsEqual() {
        AuthorizationRequest left = new AuthorizationRequest("ID", "Push package");
        AuthorizationRequest right = new AuthorizationRequest("ID", "Push package");
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsTrueWhenAllNull() {
        AuthorizationRequest left = new AuthorizationRequest(null, null);
        AuthorizationRequest right = new AuthorizationRequest(null, null);
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsFalseWhenIdNotEqual() {
        AuthorizationRequest left = new AuthorizationRequest("Left ID", "Push package");
        AuthorizationRequest right = new AuthorizationRequest("Right ID", "Push package");
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsFalseWhenPushPackageNotEqual() {
        AuthorizationRequest left = new AuthorizationRequest("ID", "Left Push package");
        AuthorizationRequest right = new AuthorizationRequest("ID", "Right Push package");
        assertFalse(left.equals(right));
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new AuthorizationRequest(null, null).toString(),
                containsString(AuthorizationRequest.class.getSimpleName()));
    }
}