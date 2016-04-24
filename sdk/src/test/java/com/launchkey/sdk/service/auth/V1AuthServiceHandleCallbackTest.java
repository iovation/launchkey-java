package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.service.error.InvalidCallbackException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.InvalidSignatureException;
import com.launchkey.sdk.transport.v1.domain.LogsRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class V1AuthServiceHandleCallbackTest extends V1AuthServiceTestBase {

    protected final String deOrbitMessage = "{\"launchkey_time\":\"" + launchKeyDateFormat.format(new Date()) +
            "\",\"user_hash\":\"Expected User Hash\"}";

    protected final Map<String, String> authData = new HashMap<String, String>() {{
        put("auth", base64.encodeAsString("Auth Value".getBytes()));
        put("user_hash", "User Hash");
        put("auth_request", "Auth Request ID");
        put("organization_user", "Organization User");
        put("user_push_id", "User Push ID");
    }};


    protected final Map<String, String> deOrbitData = new HashMap<String, String>() {{
        put("deorbit", deOrbitMessage);
        put("signature", base64.encodeAsString("Expected Signature".getBytes()));
    }};


    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(crypto.verifySignature(any(byte[].class), any(byte[].class), any(RSAPublicKey.class))).thenReturn(true);
        when(crypto.decryptRSA(any(byte[].class))).thenReturn(
                ("{\"response\": true," +
                        "\"auth_request\":\"Auth Request ID\"," +
                        "\"device_id\":\"Device ID\"}").getBytes()
        );
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test( expected = InvalidCallbackException.class)
    public void testThrowsInvalidCallbackActionWhenNotAuthOrDeOrbitCallback() throws Exception {
        service.handleCallback(new HashMap<String, String>());
    }

    @Test
    public void testDeOrbitValidatesSignature() throws Exception {
        service.handleCallback(deOrbitData);
        verify(crypto).verifySignature("Expected Signature".getBytes(), deOrbitMessage.getBytes(), publicKey);
    }

    @Test(expected = InvalidSignatureException.class)
    public void testDeOrbitThrowsInvalidSignatureExceptionWhenSignatureDoesNotVerify() throws Exception {
        when(crypto.verifySignature(any(byte[].class), any(byte[].class), any(RSAPublicKey.class))).thenReturn(false);
        service.handleCallback(deOrbitData);
    }

    @Test(expected = InvalidCallbackException.class)
    public void testDeOrbitThrowsInvalidCallbackExceptionWhenLaunchKeyTimeExceedsThreshold() throws Exception {
        service.handleCallback(
                new HashMap<String, String>() {{
                    put("deorbit", "{\"launchkey_time\":\"2001-01-01 01:01:01\",\"user_hash\":\"Expected User Hash\"}");
                    put("signature", base64.encodeAsString("Signature".getBytes()));
                }}
        );
    }

    @Test
    public void testDeOrbitReturnsExpectedDeOrbitCallbackResponse() throws Exception {
        final String launchKeyTime = launchKeyDateFormat.format(new Date());
        DeOrbitCallbackResponse expected = new DeOrbitCallbackResponse(launchKeyTime, "Expected User Hash");
        CallbackResponse actual = service.handleCallback(deOrbitData);
        assertEquals(expected, actual);
    }

    @Test(expected = InvalidCallbackException.class)
    public void testDeOrbitCatchesIOExceptionFromDeOrbitDataNotParsableAndThrowsInvalidResponseException() throws Exception {
        service.handleCallback(
                new HashMap<String, String>() {{
                    put("deorbit", "XXX");
                    put("signature", base64.encodeAsString("Signature".getBytes()));
                }}
        );
    }

    @Test
    public void testAuthResponseDecryptsAuthsResponseBase64DecodedAuthValue() throws Exception {
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        service.handleCallback(authData);
        verify(crypto).decryptRSA(argumentCaptor.capture());
        assertEquals("Auth Value", new String(argumentCaptor.getValue()));
    }

    @Test(expected = InvalidResponseException.class)
    public void testAuthResponseErrorsWhenParameterAndEncryptedAuthRequestIdDoesNotMatch() throws Exception {
        service.handleCallback(
                new HashMap<String, String>() {{
                    put("auth", base64.encodeAsString("Auth Value".getBytes()));
                    put("user_hash", "User Hash");
                    put("auth_request", "Other Auth Request ID");
                }}
        );
    }

    @Test
    public void testAuthResponseSendsLogsCallWhenAuthResponseReceived() throws Exception {
        service.handleCallback(authData);
        verify(transport).logs(any(LogsRequest.class));
    }

    @Test
    public void testAuthResponseSendsLogsCallTrueGrantedWhenResultIsTrue() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        service.handleCallback(authData);
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("true", argumentCaptor.getValue().getStatus());
    }

    @Test
    public void testAuthResponseSendsLogsCallAuthenticateDeniedWhenResultIsFalse() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        when(crypto.decryptRSA(any(byte[].class)))
                .thenReturn(("{\"auth_request\":\"Auth Request ID\",\"response\": false}").getBytes());
        service.handleCallback(authData);
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("false", argumentCaptor.getValue().getStatus());
    }

    @Test
    public void testAuthResponseReturnsExpectedAuthResponseCallbackResponse() throws Exception {
        AuthResponseCallbackResponse expected = new AuthResponseCallbackResponse(new AuthResponse(
            "Auth Request ID",
            true,
            "User Hash",
            "Organization User",
            "User Push ID",
            "Device ID"
        ));
        CallbackResponse actual = service.handleCallback(authData);
        assertEquals(expected, actual);
    }

    @Test(expected = InvalidCallbackException.class)
    public void testAuthResponseCatchesIOExceptionFromAuthDataNotParsableAndThrowsInvalidResponseException() throws Exception {
        when(crypto.decryptRSA(any(byte[].class))).thenReturn("XXX".getBytes());
        service.handleCallback(authData);
    }
}