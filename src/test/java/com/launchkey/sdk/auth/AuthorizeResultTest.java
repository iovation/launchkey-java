package com.launchkey.sdk.auth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AuthorizeResultTest {

    private AuthorizeResult authorizeResult;

    @Before
    public void setUp() throws Exception {
        authorizeResult = new AuthorizeResult();
    }

    @After
    public void tearDown() throws Exception {
        authorizeResult = null;
    }

    @Test
    public void testSetGetAuthRequest() throws Exception {
        final String expected = "expected";
        authorizeResult.setAuthRequest(expected);
        String actual = authorizeResult.getAuthRequest();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetLaunchkeyTime() throws Exception {
        final String expected = "expected";
        authorizeResult.setLaunchkeyTime(expected);
        String actual = authorizeResult.getLaunchkeyTime();
        assertEquals(expected, actual);
    }
}