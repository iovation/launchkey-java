package com.iovation.launchkey.sdk.domain.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class AuthorizationRequestTest {

    @Test
    public void getId() {
        String expected = "ID";
        assertEquals(expected, new AuthorizationRequest(expected, null, null).getId());
    }

    @Test
    public void getPushPackage() {
        String expected = "push package";
        assertEquals(expected, new AuthorizationRequest(null, expected, null).getPushPackage());
    }

    @Test
    public void getDeviceIDs() {
        List<String> expected = new ArrayList<String>(){{add("ID");}};
        assertEquals(expected, new AuthorizationRequest(null, null, expected).getDeviceIds());
    }

    @Test
    public void equalsTrueWhenAllItemsEqual() {
        AuthorizationRequest left = new AuthorizationRequest("ID", "Push package", new ArrayList<String>(){{add("ID");}});
        AuthorizationRequest right = new AuthorizationRequest("ID", "Push package", new ArrayList<String>(){{add("ID");}});
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsTrueWhenAllNull() {
        AuthorizationRequest left = new AuthorizationRequest(null, null, null);
        AuthorizationRequest right = new AuthorizationRequest(null, null, null);
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsFalseWhenIdNotEqual() {
        AuthorizationRequest left = new AuthorizationRequest("Left ID", "Push package", null);
        AuthorizationRequest right = new AuthorizationRequest("Right ID", "Push package", null);
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsFalseWhenPushPackageNotEqual() {
        AuthorizationRequest left = new AuthorizationRequest("ID", "Left Push package", null);
        AuthorizationRequest right = new AuthorizationRequest("ID", "Right Push package", null);
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsFalseWhenDeviceIdsNotEqual() {
        AuthorizationRequest left = new AuthorizationRequest("ID", "Push package", new ArrayList<String>(){{add("Left");}});
        AuthorizationRequest right = new AuthorizationRequest("ID", "Push package", new ArrayList<String>(){{add("Right");}});
        assertFalse(left.equals(right));
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new AuthorizationRequest(null, null, null).toString(),
                containsString(AuthorizationRequest.class.getSimpleName()));
    }
}