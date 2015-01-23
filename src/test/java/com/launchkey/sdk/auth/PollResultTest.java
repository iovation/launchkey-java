package com.launchkey.sdk.auth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PollResultTest {

    private PollResult pollResult;

    @Before
    public void setUp() {
        pollResult = new PollResult();
    }

    @After
    public void tearDown() {
        pollResult = null;
    }

    @Test
    public void testSetGetUserPushId() throws Exception {
        String expected = "expected";
        pollResult.setUserPushId(expected);
        String actual = pollResult.getUserPushId();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetUserHash() throws Exception {
        String expected = "expected";
        pollResult.setUserHash(expected);
        String actual = pollResult.getUserHash();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetAuthRequest() throws Exception {
        String expected = "expected";
        pollResult.setAuthRequest(expected);
        String actual = pollResult.getAuthRequest();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetAppPins() throws Exception {
        String expected = "expected";
        pollResult.setAppPins(expected);
        String actual = pollResult.getAppPins();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetDeviceId() throws Exception {
        String expected = "expected";
        pollResult.setDeviceId(expected);
        String actual = pollResult.getDeviceId();
        assertEquals(expected, actual);
    }
}