package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ServiceV3AuthsPostResponseTest {

    private final static UUID auth_request = UUID.randomUUID();

    @Test
    public void getAuthRequest() {
        assertEquals(auth_request, new ServiceV3AuthsPostResponse(auth_request, null, null).getAuthRequest());
    }

    @Test
    public void getPushPackage() {
        String expected = "Push Package";
        assertEquals(expected, new ServiceV3AuthsPostResponse(null, expected, null).getPushPackage());
    }

    @Test
    public void getDeviceIDs() {
        List<String> expected = new ArrayList<String>(){{add("Device ID");}};
        assertEquals(expected, new ServiceV3AuthsPostResponse(null, null, expected).getDeviceIds());
    }

    @Test
    public void equalsTrueWhenAllEqual() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Device ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Device ID");}});
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsTrueWhenAllEmpty() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(null, null, null);
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(null, null, null);
        assertTrue(left.equals(right));
    }

    @Test
    public void equalsFalseWhenAuthRequestDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package", new ArrayList<String>(){{add("Device ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package", new ArrayList<String>(){{add("Device ID");}});
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsFalseWhenPushPackageDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Left Push Package", new ArrayList<String>(){{add("Device ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Right Push Package", new ArrayList<String>(){{add("Device ID");}});
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsFalseWhenDeviceIdsDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Left ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Right ID");}});
        assertFalse(left.equals(right));
    }

    @Test
    public void hashCodeEqualWhenAllEqual() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Device ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Device ID");}});
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeEqualWhenAllEmpty() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(null, null, null);
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(null, null, null);
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenAuthRequestDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package", new ArrayList<String>(){{add("Device ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(UUID.randomUUID(), "Push Package", new ArrayList<String>(){{add("Device ID");}});
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenPushPackageDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Left Push Package", new ArrayList<String>(){{add("Device ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Right Push Package", new ArrayList<String>(){{add("Device ID");}});
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenDeviceIdsDifferent() {
        ServiceV3AuthsPostResponse left = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Left ID");}});
        ServiceV3AuthsPostResponse right = new ServiceV3AuthsPostResponse(auth_request, "Push Package", new ArrayList<String>(){{add("Right ID");}});
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void toStringContainsClassName() {
        assertThat(
                new ServiceV3AuthsPostResponse(null, null, null).toString(),
                containsString(ServiceV3AuthsPostResponse.class.getSimpleName()));
    }

    @Test
    public void parsesMissingDeviceIdsAsNull() throws Exception {
        String json = "{\"auth_request\":\"" + UUID.randomUUID() + "\",\"push_package\":\"package\"}";
        List<String> actual = new ObjectMapper().readValue(json, ServiceV3AuthsPostResponse.class).getDeviceIds();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void parsesGivenDeviceIdsAsListOfString() throws Exception {
        List<String> expected = new ArrayList<String>() {{
            add("1");
            add("2");
        }};
        String json = "{\"auth_request\":\"" + UUID.randomUUID() + "\",\"push_package\":\"package\",\"device_ids\":[\"1\", \"2\"]}";
        List<String> actual = new ObjectMapper().readValue(json, ServiceV3AuthsPostResponse.class).getDeviceIds();
        assertThat(actual, is(equalTo(expected)));
    }
}