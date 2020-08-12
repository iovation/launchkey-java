package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class DirectoryV3TotpPostRequestTest {

    private DirectoryV3TotpPostRequest request;

    @Before
    public void setUp() throws Exception {
        request = new DirectoryV3TotpPostRequest("expected identifier");
    }

    @Test
    public void testGetIdentifier() {
            assertEquals("expected identifier", request.getIdentifier());
    }

    @Test
    public void testEqualObjectsAreEqual() {
        assertEquals(new DirectoryV3TotpPostRequest("expected identifier"), request);
    }

    @Test
    public void testNotEqualObjectsAreNotEqual() {
        assertNotEquals(new DirectoryV3TotpPostRequest("unexpected identifier"), request);
    }

    @Test
    public void hashCodeEqualForEquivalentObjects() {
        assertEquals(new DirectoryV3TotpPostRequest("expected identifier").hashCode(), request.hashCode());
    }

    @Test
    public void hashCodeDifferentForDifferentIdentifiers() {
        assertNotEquals(new DirectoryV3TotpPostRequest("different").hashCode(), request.hashCode());
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(request.toString(), containsString(DirectoryV3TotpPostRequest.class.getSimpleName()));
    }


    @Test
    public void marshallingIncludesIdentifier() throws JsonProcessingException {
        assertEquals(
                "{\"identifier\":\"expected identifier\"}",
                new ObjectMapper().writeValueAsString(request)
        );
    }
}