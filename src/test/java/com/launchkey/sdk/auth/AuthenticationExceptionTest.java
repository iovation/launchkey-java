package com.launchkey.sdk.auth;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuthenticationExceptionTest {

    @Test
    public void testConstructorCreatesProperMessage() {
        String expected = "Error. Error code: Code";
        AuthenticationException x = new AuthenticationException("Error", "Code");
        String actual = x.getMessage();
        assertEquals(expected, actual);
    }
}