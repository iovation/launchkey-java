package com.iovation.launchkey.sdk.domain.directory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
    private DirectoryUserDeviceLinkData deviceAddResponse;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        deviceAddResponse = new DirectoryUserDeviceLinkData(CODE, URL);
    }

    @After
    public void tearDown() throws Exception {
        deviceAddResponse = null;
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
    public void toStringContainsClassName() throws Exception {
        assertTrue(deviceAddResponse.toString().contains(deviceAddResponse.getClass().getSimpleName()));
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        assertTrue(deviceAddResponse.equals(deviceAddResponse));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        assertTrue(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(CODE, URL)));
    }

    @Test
    public void equalsIsFalseForDifferentCode() throws Exception {
        assertFalse(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(null, URL)));
    }

    @Test
    public void equalsIsFalseForDifferentUrl() throws Exception {
        assertFalse(deviceAddResponse.equals(new DirectoryUserDeviceLinkData(CODE, null)));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(deviceAddResponse.hashCode(), deviceAddResponse.hashCode());
    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        assertEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(CODE, URL).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCode() throws Exception {
        assertNotEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(null, URL).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentUrl() throws Exception {
        assertNotEquals(deviceAddResponse.hashCode(), new DirectoryUserDeviceLinkData(CODE, null).hashCode());
    }

    @Test
    public void jsonDecodeReturnsExpectedData() throws Exception {
        String json = "{" +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"" +
                "}";

        assertEquals(deviceAddResponse, mapper.readValue(json, deviceAddResponse.getClass()));
    }

    @Test
    public void jsonDecodeAllowsAdditionalData() throws Exception {
        String json = "{" +
                "\"other\": \"data\"," +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"," +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"" +
                "}";

        assertEquals(deviceAddResponse, mapper.readValue(json, deviceAddResponse.getClass()));
    }

    @Test(expected = JsonMappingException.class)
    public void jsonDecodeThrowsJsonMappingExceptionWhenNoCode() throws Exception {
        String json = "{" +
                "\"qrcode\": \"" + deviceAddResponse.getQrCodeUrl() + "\"" +
                "}";

        mapper.readValue(json, deviceAddResponse.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void jsonDecodeThrowsJsonMappingExceptionWhenNoURL() throws Exception {
        String json = "{" +
                "\"code\": \"" + deviceAddResponse.getCode() +"\"" +
                "}";

        mapper.readValue(json, deviceAddResponse.getClass());
    }
}
