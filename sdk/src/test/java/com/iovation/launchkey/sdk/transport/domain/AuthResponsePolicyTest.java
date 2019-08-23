package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class AuthResponsePolicyTest {

    private AuthResponsePolicy policy;

    @Before
    public void setUp() throws Exception {
        policy = new AuthResponsePolicy("REQ", 1, Arrays.asList("T1", "T2"), new ArrayList<Fence>() {{
            add(new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal"));
        }});
    }

    @After
    public void tearDown() throws Exception {
        policy = null;
    }

    @Test
    public void getRequirement() {
        assertEquals("REQ", policy.getRequirement());
    }

    @Test
    public void getAmount() {
        assertEquals(1, policy.getAmount());
    }

    @Test
    public void getTypes() {
        assertEquals(Arrays.asList("T1", "T2"), policy.getTypes());
    }

    @Test
    public void getFences() {
        assertEquals(new ArrayList<Fence>() {{
            add(new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal"));
        }}, policy.getFences());
    }

    @Test
    public void testEquals() {
        AuthResponsePolicy other = new AuthResponsePolicy("REQ", 1, Arrays.asList("T1", "T2"), new ArrayList<Fence>() {{
            add(new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal"));
        }});
        assertEquals(other, policy);
    }

    @Test
    public void testHashCode() {
        AuthResponsePolicy other = new AuthResponsePolicy("REQ", 1, Arrays.asList("T1", "T2"), new ArrayList<Fence>() {{
            add(new Fence("name", "type", 1.0, 2.0, 3.0, "country", "admin", "postal"));
        }});
        assertEquals(other.hashCode(), policy.hashCode());
    }

    @Test
    public void testToString() {
        assertThat(policy.toString(), startsWith(policy.getClass().getSimpleName()));
    }

    @Test
    public void testParsesDefaultsAsExpected() throws JsonProcessingException {
        AuthResponsePolicy expected = new AuthResponsePolicy(null, 0, null, null);
        AuthResponsePolicy actual = new ObjectMapper().readValue("{}", AuthResponsePolicy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testParsesItemsAsExpected() throws JsonProcessingException {
        AuthResponsePolicy expected = new AuthResponsePolicy("REQ", 1,
                new ArrayList<String>() {{
                    add("KNOWLEDGE");
                    add("STUFF");
                }},
                new ArrayList<Fence>() {{
                    add(new Fence("NAME", "TYPE", 1.0, 2.0, 3.0, null, null, null));
                }});
        AuthResponsePolicy actual = new ObjectMapper().readValue(
                "{\"requirement\":\"REQ\",\"amount\":1,\"types\":[\"KNOWLEDGE\",\"STUFF\"],\"geofences\":[" +
                    "{\"name\":\"NAME\",\"type\":\"TYPE\",\"latitude\":1,\"longitude\":2,\"radius\":3}" +
                "]}",
                AuthResponsePolicy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testParsesExtraWithoutError() throws JsonProcessingException {
        AuthResponsePolicy expected = new AuthResponsePolicy(null, 0, null, null);
        AuthResponsePolicy actual = new ObjectMapper().readValue("{\"foo\": \"bar\"}", AuthResponsePolicy.class);
        assertEquals(expected, actual);
    }
}