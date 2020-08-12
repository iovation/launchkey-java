package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectoryV3TotpDeleteRequestTest {

    private DirectoryV3TotpDeleteRequest transport;

    @Before
    public void setUp() throws Exception {
        transport = new DirectoryV3TotpDeleteRequest("expected identifier");
    }

    @Test
    public void getIdentifier() {
        assertEquals("expected identifier", transport.getIdentifier());
    }

    @Test
    public void equalObjectsAreEqual() {
        assertEquals(new DirectoryV3TotpDeleteRequest("expected identifier"), transport);
    }

    @Test
    public void unequalObjectsAreNotEqual() {
        assertNotEquals(new DirectoryV3TotpDeleteRequest("unexpected identifier"), transport);
    }

    @Test
    public void equalObjectsHaveHashCodeEqual() {
        assertEquals(new DirectoryV3TotpDeleteRequest("expected identifier").hashCode(), transport.hashCode());
    }

    @Test
    public void unequalObjectsHaveHasCodeNotEqual() {
        assertNotEquals(new DirectoryV3TotpDeleteRequest("unexpected identifier").hashCode(), transport.hashCode());
    }

    @Test
    public void toStringHasClassName() {
        assertTrue(transport.toString().contains(transport.getClass().getSimpleName()));
    }

    @Test
    public void marshallCreatesProperJSON() throws Exception {
        String expected = "{\"identifier\":\"expected identifier\"}";
        String actual = (new ObjectMapper()).writeValueAsString(transport);
        assertEquals(expected, actual);

    }
}