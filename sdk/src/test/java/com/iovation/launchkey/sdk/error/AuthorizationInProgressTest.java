package com.iovation.launchkey.sdk.error;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class AuthorizationInProgressTest {

    private Exception cause;
    private Date expires;
    private AuthorizationInProgress exception;

    @Before
    public void setUp() throws Exception {
        cause = new Exception();
        expires = new Date();
        exception = new AuthorizationInProgress("Message", cause, "Error Code", "Auth Request ID", true, expires);
    }

    @Test
    public void getMessage() {
        assertEquals("Message", exception.getMessage());
    }

    @Test
    public void getCause() {
        assertSame(cause, exception.getCause());
    }

    @Test
    public void getErrorCode() {
        assertEquals("Error Code", exception.getErrorCode());
    }

    @Test
    public void getAuthorizationRequestId() {
        assertEquals("Auth Request ID", exception.getAuthorizationRequestId());
    }

    @Test
    public void isMyAuthorizationRequest() {
        assertTrue(exception.isMyAuthorizationRequest());
    }

    @Test
    public void getExpires() {
        assertEquals(expires, exception.getExpires());
    }
}