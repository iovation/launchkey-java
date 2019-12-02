package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class FenceTest {
    private Fence fence;

    @Before
    public void setUp() throws Exception {
        fence = new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal");
    }

    @After
    public void tearDown() throws Exception {
        fence = null;
    }

    @Test
    public void getName() {
        assertEquals("name", fence.getName());
    }

    @Test
    public void getType() {
        assertEquals("type", fence.getType());
    }

    @Test
    public void getLatitude() {
        assertEquals(new Double(1.0), fence.getLatitude());
    }

    @Test
    public void getLongitude() {
        assertEquals(new Double(2.0), fence.getLongitude());
    }

    @Test
    public void getRadius() {
        assertEquals(new Double(3.0), fence.getRadius());
    }

    @Test
    public void getCountry() {
        assertEquals("country", fence.getCountry());
    }

    @Test
    public void getAdministrativeArea() {
        assertEquals("admin", fence.getAdministrativeArea());
    }

    @Test
    public void getPostalCode() {
        assertEquals("postal", fence.getPostalCode());
    }

    @Test
    public void testEquals() {
        Fence other = new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal");
        assertEquals(fence, other);
    }

    @Test
    public void testHashCode() {
        Fence other = new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal");
        assertEquals(fence.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        assertThat(fence.toString(), startsWith(fence.getClass().getSimpleName()));
    }

    @Test
    public void testParseAll() throws JsonProcessingException {
        String json = "{\"name\":\"name\",\"type\":\"type\",\"latitude\":1,\"longitude\":2,\"radius\":3," +
                "\"country\":\"country\",\"administrative_area\":\"admin\",\"postal_code\":\"postal\"}";
        Fence actual = new ObjectMapper().readValue(json, Fence.class);
        assertEquals(fence, actual);
    }

    @Test
    public void testParseDefaultsAllToNull() throws JsonProcessingException {
        Fence expected = new Fence(null, null, null, null, null, null, null, null);
        Fence actual = new ObjectMapper().readValue("{}", Fence.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseExtraWithoutError() throws JsonProcessingException {
        Fence expected = new Fence(null, null, null, null, null, null, null, null);
        Fence actual = new ObjectMapper().readValue("{\"foo\": null}", Fence.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testIgnoresNullOnSerialize() throws JsonProcessingException {
        fence = new Fence(null, null, null, null, null, null, null, null);
        String actual = new ObjectMapper().writeValueAsString(fence);
        assertEquals("{}", actual);
    }
}