package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class DirectoryV3DevicesPostRequestTest {

    private DirectoryV3DevicesPostRequest request;

    @Before
    public void setUp() throws Exception {
        request = new DirectoryV3DevicesPostRequest("expected identifier", 999);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getIdentifier() {
        assertEquals("expected identifier", request.getIdentifier());
    }

    @Test
    public void getTTL() {
        assertEquals(Integer.valueOf(999), request.getTTL());
    }

    @Test
    public void equalObjectsAreEqual() {
        assertEquals(new DirectoryV3DevicesPostRequest("expected identifier", 999), request);
    }

    @Test
    public void differentIdentifiersAreNotEqual() {
        assertNotEquals(new DirectoryV3DevicesPostRequest("different", 999), request);
    }

    @Test
    public void differentTtlIsNotEqual() {
        assertNotEquals(new DirectoryV3DevicesPostRequest("expected identifier", 998), request);
    }

    @Test
    public void hashCodeEqualForEquivalentObjects() {
        assertEquals(new DirectoryV3DevicesPostRequest("expected identifier", 999).hashCode(), request.hashCode());
    }

    @Test
    public void hashCodeDifferentForDifferentIdentifiers() {
        assertNotEquals(new DirectoryV3DevicesPostRequest("different", 999).hashCode(), request.hashCode());
    }

    @Test
    public void hashCodeDifferentForDifferentTtl() {
        assertNotEquals(new DirectoryV3DevicesPostRequest("expected identifier", 998).hashCode(), request.hashCode());
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(request.toString(), containsString(DirectoryV3DevicesPostRequest.class.getSimpleName()));
    }

    @Test
    public void marshallingIncludesIdentifierAndTtlWhenTtlNotNull() throws JsonProcessingException {
        assertEquals(
                "{\"identifier\":\"expected identifier\",\"ttl\":999}",
                new ObjectMapper().writeValueAsString(request)
        );
    }

    @Test
    public void marshallingIncludesIdentifierButNotTtlWhenTtlIsNull() throws JsonProcessingException {
        assertEquals(
                "{\"identifier\":\"expected identifier\"}",
                new ObjectMapper().writeValueAsString(new DirectoryV3DevicesPostRequest("expected identifier", null))
        );
    }
}