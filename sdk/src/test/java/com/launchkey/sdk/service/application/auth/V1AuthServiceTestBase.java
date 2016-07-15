package com.launchkey.sdk.service.application.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.ping.PingService;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.PingRequest;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import com.launchkey.sdk.transport.v1.domain.PlatformDateFormat;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public abstract class V1AuthServiceTestBase {

    protected V1AuthService service;
    protected final String rsaEncryptedValue = "RSA Encrypted Value";
    protected final String rsaSignedValue = "RSA Signature";
    protected final Base64 base64 = new Base64(0);
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final long appKey = 12345674890L;
    protected final String secretKey = "ffd62938c9c42c471a440a44854f6b9a";
    protected Transport transport;
    protected Crypto crypto;
    protected PingService pingService;
    protected RSAPublicKey publicKey;
    protected PingResponse pingResponse;

    @Before
    public void setUp() throws Exception {
        transport = mock(Transport.class);
        crypto = mock(Crypto.class);
        pingService = mock(PingService.class);
        publicKey = mock(RSAPublicKey.class);

        pingResponse = new PingResponse("Fingerprint", new Date(0L), "Expected Key");
        when(pingService.getPublicKey()).thenReturn(publicKey);
        when(crypto.encryptRSA(any(byte[].class), any(PublicKey.class))).thenReturn(rsaEncryptedValue.getBytes());
        when(crypto.sign(any(byte[].class))).thenReturn(rsaSignedValue.getBytes());
        service = new V1AuthService(transport, crypto, pingService, appKey, secretKey);
    }

    @SuppressWarnings("Duplicates")
    @After
    public void tearDown() throws Exception {
        transport = null;
        crypto = null;
        pingService = null;
        publicKey = null;
        pingResponse = null;
        service = null;
    }

    public void verifyGetPublicKeyFromPingService() throws Exception {
        verify(pingService, atLeastOnce()).getPublicKey();
    }

    public void verifyRsaEncryptedWithCorrectPublicKeyToCreateSecretKey() throws Exception {
        ArgumentCaptor<PublicKey> argumentCaptor = ArgumentCaptor.forClass(PublicKey.class);
        verify(crypto, atLeastOnce()).encryptRSA(any(byte[].class), argumentCaptor.capture());
        assertEquals(publicKey, argumentCaptor.getValue());
    }

    public void verifyRsaEncryptedJsonWithCorrectDataToCreateSecretKey(Date start, Date end) throws Exception {
        // "API time" precision is in seconds, Java is milliseconds, round down to the second
        Date actualStart = new Date((start.getTime() / 1000) * 1000);
        Date actualEnd = new Date((end.getTime() / 1000) * 1000);

        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(crypto, atLeastOnce()).encryptRSA(argumentCaptor.capture(), any(PublicKey.class));
        String json = new String(argumentCaptor.getValue());
        JsonNode actual = objectMapper.readTree(json);
        assertEquals("Expected only 2 elements, stamped and secret, in JSON: " + json, 2, actual.size());
        assertEquals("Unexpected secret value in JSON object", secretKey, actual.get("secret").textValue());
        String stamped = actual.get("stamped").asText();
        Date actualDate = new PlatformDateFormat().parseObject(stamped);
        assertThat(actualDate, greaterThanOrEqualTo(actualStart));
        assertThat(actualDate, lessThanOrEqualTo(actualEnd));
    }

    public void verifySignatureUsedEncryptedSecretKeyValue() throws Exception {
        ArgumentCaptor<byte[]> signatureCaptor = ArgumentCaptor.forClass(byte[].class);
        verify(crypto, atLeastOnce()).sign(signatureCaptor.capture());
        String actual = new String(signatureCaptor.getValue());
        assertEquals(rsaEncryptedValue, actual);
    }
}
