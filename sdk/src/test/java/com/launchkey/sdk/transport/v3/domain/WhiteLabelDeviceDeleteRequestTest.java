package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;

import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class WhiteLabelDeviceDeleteRequestTest {
    private static final String ISSUER = "Issuer";
    private static final String SDK_KEY = "SDK Key";
    private static final String IDENTIFIER = "Identifier";
    private static final String DEVICE_NAME = "Device Name";
    private WhiteLabelDeviceDeleteRequest request;
    private RSAPrivateKey privateKey;

    @Before
    public void setUp() throws Exception {
        privateKey = (RSAPrivateKey) KeyPairGenerator.getInstance("RSA").generateKeyPair().getPrivate();
        request = new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, IDENTIFIER, DEVICE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        request = null;
        privateKey = null;
    }

    @Test
    public void getSdkKey() throws Exception {
        assertEquals(SDK_KEY, request.getSdkKey());
    }

    @Test
    public void getIdentifier() throws Exception {
        assertEquals(IDENTIFIER, request.getIdentifier());
    }

    @Test
    public void hashCodeIsTheSameForSameObject() throws Exception {
        assertEquals(request.hashCode(), request.hashCode());
    }

    @Test
    public void hashCodeIsTheSameForEquivalentObject() throws Exception {
        assertEquals(request.hashCode(), new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, IDENTIFIER, DEVICE_NAME).hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentPublicKey() throws Exception {
        assertNotEquals(request.hashCode(), new WhiteLabelDeviceDeleteRequest((RSAPrivateKey) KeyPairGenerator.getInstance("RSA").generateKeyPair().getPrivate(), ISSUER, SDK_KEY, IDENTIFIER, DEVICE_NAME).hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentIssuer() throws Exception {
        assertNotEquals(request.hashCode(), new WhiteLabelDeviceDeleteRequest(privateKey, "Other Issuer", SDK_KEY, IDENTIFIER, DEVICE_NAME).hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentSdkKey() throws Exception {
        assertNotEquals(request.hashCode(), new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, "Other SDK Key", IDENTIFIER, DEVICE_NAME).hashCode());
    }


    @Test
    public void hashCodeIsDifferentForDifferentIdentifier() throws Exception {
        assertNotEquals(request.hashCode(), new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, "Other Identifier", DEVICE_NAME).hashCode());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        assertTrue(request.equals(request));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        assertTrue(request.equals(new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, IDENTIFIER, DEVICE_NAME)));
    }

    @Test
    public void equalsIsFalseForDifferentPublicKey() throws Exception {
        assertFalse(request.equals(new WhiteLabelDeviceDeleteRequest((RSAPrivateKey) KeyPairGenerator.getInstance("RSA").generateKeyPair().getPrivate(), ISSUER, SDK_KEY, IDENTIFIER, DEVICE_NAME)));
    }

    @Test
    public void equalsIsFalseForDifferentIssuer() throws Exception {
        assertFalse(request.equals(new WhiteLabelDeviceDeleteRequest(privateKey, "Different Issuer", SDK_KEY, IDENTIFIER, DEVICE_NAME)));
    }

    @Test
    public void equalsIsFalseForDifferentSdkKey() throws Exception {
        assertFalse(request.equals(new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, "Different SDK Key", IDENTIFIER, DEVICE_NAME)));
    }

    @Test
    public void equalsIsFalseForDifferentIdentifier() throws Exception {
        assertFalse(request.equals(new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, "Different Identifier", DEVICE_NAME)));
    }

    @Test
    public void hashCodeIsDifferentForDifferentDeviceName() throws Exception {
        assertNotEquals(request.hashCode(), new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, IDENTIFIER, "Different Device name").hashCode());
    }

    @Test
    public void equalsIsFalseForDifferentDeviceName() throws Exception {
        assertFalse(request.equals(new WhiteLabelDeviceDeleteRequest(privateKey, ISSUER, SDK_KEY, IDENTIFIER, "Different Device Name")));
    }

    @Test
    public void toStringContainsClassSimpleName() throws Exception {
        assertTrue(request.toString().contains(request.getClass().getSimpleName()));
    }

    @Test
    public void getPrivateKey() throws Exception {
        assertEquals(privateKey, request.getPrivateKey());
    }

    @Test
    public void getIssuer() throws Exception {
        assertEquals(ISSUER, request.getIssuer());
    }

    @Test
    public void jsonEncode() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String expected = "{\"sdk_key\":\"SDK Key\",\"identifier\":\"Identifier\",\"device_name\":\"Device Name\"}";
        String actual = mapper.writeValueAsString(request);
        assertEquals(expected, actual);
    }
}
