package com.iovation.launchkey.sdk.domain.directory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DirectoryUserDeviceLinkDataTest {
    private static final String URL = "Expected URL";
    private static final String CODE = "Expected Code";
    private static final UUID DEVICE_ID = UUID.randomUUID();
    private DirectoryUserDeviceLinkData deviceAddResponse;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        deviceAddResponse = new DirectoryUserDeviceLinkData(CODE, URL, DEVICE_ID);
    }

    @After
    public void tearDown() throws Exception {
        deviceAddResponse = null;
    }

    @Test
    public void deprecatedConstructorPopulatesValues() throws Exception {
        deviceAddResponse = new DirectoryUserDeviceLinkData(CODE, URL);
        assertEquals(CODE, deviceAddResponse.getCode());
        assertEquals(URL, deviceAddResponse.getQrCodeUrl());
        assertNull(deviceAddResponse.getDeviceId());
    }

    @Test
    public void getCode() throws Exception {
        assertEquals(CODE, deviceAddResponse.getCode());
    }

    @Test
    public void getQrCodeUrl() throws Exception {
        assertEquals(URL, deviceAddResponse.getQrCodeUrl());
    }

    @Test
    public void getDeviceId() throws Exception {
        assertEquals(DEVICE_ID, deviceAddResponse.getDeviceId());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(deviceAddResponse.toString().contains(deviceAddResponse.getClass().getSimpleName()));
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        assertTrue(deviceAddResponse.equals(deviceAddResponse));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        assertTrue(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(CODE, URL, DEVICE_ID)));
    }

    @Test
    public void equalsIsFalseForDifferentCode() throws Exception {
        assertFalse(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(null, URL, DEVICE_ID)));
    }

    @Test
    public void equalsIsFalseForDifferentUrl() throws Exception {
        assertFalse(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(CODE, null, DEVICE_ID)));
    }

    @Test
    public void equalsIsFalseForDifferentDeviceId() throws Exception {
        assertFalse(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(CODE, URL, null)));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(deviceAddResponse.hashCode(), deviceAddResponse.hashCode());
    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        assertEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(CODE, URL, DEVICE_ID).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCode() throws Exception {
        assertNotEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(null, URL, DEVICE_ID).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentUrl() throws Exception {
        assertNotEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(CODE, null, DEVICE_ID).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentDeviceId() throws Exception {
        assertNotEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(CODE, URL, null).hashCode());
    }

    @Test
    public void jsonDecodeReturnsExpectedDataWithoutDeviceId() throws Exception {
        String json = "{" +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"," +
                "\"device_id\": \"" + deviceAddResponse.getDeviceId() + "\"" +
                "}";

        assertEquals(deviceAddResponse, mapper.readValue(json, deviceAddResponse.getClass()));
    }

    @Test
    public void jsonDecodeReturnsExpectedDataWithDeviceId() throws Exception {
        String json = "{" +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"," +
                "\"device_id\": \"" + deviceAddResponse.getDeviceId() + "\"" +
                "}";

        assertEquals(deviceAddResponse, mapper.readValue(json, deviceAddResponse.getClass()));
    }

    @Test
    public void jsonDecodeAllowsAdditionalData() throws Exception {
        String json = "{" +
                "\"other\": \"data\"," +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"," +
                "\"device_id\": \"" + deviceAddResponse.getDeviceId() + "\"" +
                "}";

        assertEquals(deviceAddResponse, mapper.readValue(json, deviceAddResponse.getClass()));
    }

    @Test(expected = JsonMappingException.class)
    public void jsonDecodeThrowsJsonMappingExceptionWhenNoCode() throws Exception {
        String json = "{" +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"," +
                "\"device_id\": \"" + deviceAddResponse.getDeviceId() + "\"" +
                "}";

        mapper.readValue(json, deviceAddResponse.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void jsonDecodeThrowsJsonMappingExceptionWhenNoURL() throws Exception {
        String json = "{" +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"device_id\": \"" + deviceAddResponse.getDeviceId() + "\"" +
                "}";

        mapper.readValue(json, deviceAddResponse.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void jsonDecodeThrowsJsonMappingExceptionWhenNoDeviceId() throws Exception {
        String json = "{" +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"device_id\": \"" + deviceAddResponse.getDeviceId() + "\"" +
                "}";

        mapper.readValue(json, deviceAddResponse.getClass());
    }
}
