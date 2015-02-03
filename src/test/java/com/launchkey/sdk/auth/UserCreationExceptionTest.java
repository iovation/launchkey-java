package com.launchkey.sdk.auth;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserCreationExceptionTest {
    @Test
    public void testMessageCodeConstructorSetsMessage() throws Exception {
        UserCreationException ex = new UserCreationException("expected", "1111");
        assertEquals("expected", ex.getMessage());
    }

    @Test
    public void testMessageCodeConstructorSetsCode() throws Exception {
        UserCreationException ex = new UserCreationException("expected", "1111");
        assertEquals("1111", ex.getCode());
    }

    @Test
    public void testMessageCauseCodeSetsMessage() throws Exception {
        UserCreationException ex = new UserCreationException("expected", new Exception(), "1111");
        assertEquals("expected", ex.getMessage());
    }

    @Test
    public void testMessageCauseCodeSetsThrowable() throws Exception {
        Exception expected = new Exception();
        UserCreationException ex = new UserCreationException("expected", expected, "1111");
        assertSame(expected, ex.getCause());
    }

    @Test
    public void testMessageCauseCodeSetsCode() throws Exception {
        UserCreationException ex = new UserCreationException("expected", new Exception(), "1111");
        assertEquals("1111", ex.getCode());
    }
}