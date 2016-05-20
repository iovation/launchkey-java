package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.transport.v1.domain.LogsRequest;
import com.launchkey.sdk.transport.v1.domain.PollRequest;
import com.launchkey.sdk.transport.v1.domain.PollResponse;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
public class V1AuthServiceGetAuthResponseTest extends V1AuthServiceTestBase {

    private final Base64 base64 = new Base64(0);

    private final PollResponse pollResponse = new PollResponse(
            base64.encodeAsString("Auth Value".getBytes()),
            "User Hash",
            "Organization User ID",
            "User Push ID"
    );

    @Before @Override
    public void setUp() throws Exception {
        super.setUp();
        when(transport.poll(any(PollRequest.class))).thenReturn(pollResponse);
        when(crypto.decryptRSA(any(byte[].class))).thenReturn(
                ("{\"response\": true," +
                        "\"auth_request\":\"Auth Request ID\"," +
                        "\"device_id\":\"Device ID\"}").getBytes()
        );
    }

    @Test
    public void testChecksPingResponseCache() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verifyChecksPingResponseCache();
    }

    @Test
    public void testDoesNotCallPingWhenPingResponseCacheReturnsResponse() throws Exception {
        when(pingResponseCache.getPingResponse()).thenReturn(pingResponse);

        service.getAuthResponse("Auth Request ID");
        verifyDoesNotCallPingWhenPingResponseCacheReturnsResponse();
    }

    @Test
    public void testDoesCallPingWhenPingResponseCacheReturnsNull() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verifyDoesCallPingWhenPingResponseCacheReturnsNull();
    }

    @Test
    public void testAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verifyAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue();
    }

    @Test
    public void testUsesPemFromPingToCreatePublicKey() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verifyUsesPemFromPingToCreatePublicKey();
    }

    @Test
    public void testPassesCorrectAuthRequestIDInPollRequest() throws Exception {
        service.getAuthResponse("Auth Request ID");
        ArgumentCaptor<PollRequest> argumentCaptor = ArgumentCaptor.forClass(PollRequest.class);
        verify(transport).poll(argumentCaptor.capture());
        assertEquals(appKey, argumentCaptor.getValue().getAppKey());
    }

    @Test
    public void testPassesCorrectAppKeyInPollRequest() throws Exception {
        service.getAuthResponse("Auth Request ID");
        ArgumentCaptor<PollRequest> argumentCaptor = ArgumentCaptor.forClass(PollRequest.class);
        verify(transport).poll(argumentCaptor.capture());
        assertEquals(appKey, argumentCaptor.getValue().getAppKey());
    }

    @Test
    public void testPassesCorrectRsaEncryptedSecretKeyInPollRequest() throws Exception {
        service.getAuthResponse("Auth Request ID");
        ArgumentCaptor<PollRequest> argumentCaptor = ArgumentCaptor.forClass(PollRequest.class);
        verify(transport).poll(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Encrypted Value".getBytes()), argumentCaptor.getValue().getSecretKey());
    }

    @Test
    public void testRsaEncryptedWithCorrectPublicKeyToCreateSecretKeyInPollRequest() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verifyRsaEncryptedWithCorrectPublicKeyToCreateSecretKey();
    }

    @Test
    public void testRsaEncryptedJsonWithCorrectDataToCreateSecretKeyInPollRequest() throws Exception {
        Date start = new Date();
        service.getAuthResponse("Auth Request ID");
        Date end = new Date();
        verifyRsaEncryptedJsonWithCorrectDataToCreateSecretKey(start, end);
    }

    @Test
    public void testPassesCorrectSignatureInPollRequest() throws Exception {
        service.getAuthResponse("Auth Request ID");
        ArgumentCaptor<PollRequest> argumentCaptor = ArgumentCaptor.forClass(PollRequest.class);
        verify(transport).poll(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Signature".getBytes()), argumentCaptor.getValue().getSignature());
    }

    @Test
    public void testSignatureInPollRequestUsedEncryptedButNotEncodedSecretKeyValue() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verifySignatureUsedEncryptedSecretKeyValue();
    }

    @Test
    public void testReturnsNullWhenPollReturnsNull() throws Exception {
        service.getAuthResponse("Auth Request ID");
        when(transport.poll(any(PollRequest.class))).thenReturn(null);
        assertNull(service.getAuthResponse("Auth Request ID"));
    }

    @Test
    public void testReturnsAuthResponseWhenPollReturnsAuthsResponse() throws Exception {
        service.getAuthResponse("Auth Request ID");
        assertNotNull(service.getAuthResponse("Auth Request ID"));
    }

    @Test
    public void testDecryptsAuthsResponseBase64DecodedAuthValue() throws Exception {
        ArgumentCaptor<byte[]> argumentCaptor = ArgumentCaptor.forClass(byte[].class);
        service.getAuthResponse("Auth Request ID");
        verify(crypto).decryptRSA(argumentCaptor.capture());
        assertEquals("Auth Value", new String(argumentCaptor.getValue()));
    }

    @Test(expected = InvalidResponseException.class)
    public void testErrorsWhenRequestAndResponseAuthRequestIdDoesNotMatch() throws Exception {
        service.getAuthResponse("Other Auth Request ID");
    }

    @Test
    public void testSendsLogsCallWhenAuthResponseReceived() throws Exception {
        service.getAuthResponse("Auth Request ID");
        verify(transport).logs(any(LogsRequest.class));
    }

    @Test
    public void testDoesNotSendLogsCallWhenNoAuthResponseReceived() throws Exception {
        when(transport.poll(any(PollRequest.class))).thenReturn(null);
        service.getAuthResponse("Auth Request ID");
        verify(transport, never()).logs(any(LogsRequest.class));
    }

    @Test
    public void testSendsLogsCallAuthenticateTrueWhenResultIsTrue() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        service.getAuthResponse("Auth Request ID");
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("true", argumentCaptor.getValue().getStatus());
    }

    @Test
    public void testSendsLogsCallAuthenticateFalseWhenResultIsFalse() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        when(crypto.decryptRSA(any(byte[].class)))
                .thenReturn(("{\"auth_request\":\"Auth Request ID\",\"response\": false}").getBytes());
        service.getAuthResponse("Auth Request ID");
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("false", argumentCaptor.getValue().getStatus());
    }

    @Test
    public void testReturnsAuthResponseWithCorrectAuthRequestId() throws Exception {
        AuthResponse authResponse = service.getAuthResponse("Auth Request ID");
        assertEquals("Auth Request ID", authResponse.getAuthRequestId());
    }

    @Test
    public void testReturnsAuthResponseWithCorrectAuthorized() throws Exception {
        AuthResponse authResponse = service.getAuthResponse("Auth Request ID");
        assertEquals(true, authResponse.isAuthorized());
    }

    @Test
    public void testReturnsAuthResponseWithCorrectUserHash() throws Exception {
        AuthResponse authResponse = service.getAuthResponse("Auth Request ID");
        assertEquals("User Hash", authResponse.getUserHash());
    }

    @Test
    public void testReturnsAuthResponseWithCorrectOrganizationUserId() throws Exception {
        AuthResponse authResponse = service.getAuthResponse("Auth Request ID");
        assertEquals("Organization User ID", authResponse.getOrganizationUserId());
    }

    @Test
    public void testReturnsAuthResponseWithCorrectUserPushId() throws Exception {
        AuthResponse authResponse = service.getAuthResponse("Auth Request ID");
        assertEquals("User Push ID", authResponse.getUserPushId());
    }

    @Test
    public void testReturnsAuthResponseWithCorrectDeviceId() throws Exception {
        AuthResponse authResponse = service.getAuthResponse("Auth Request ID");
        assertEquals("Device ID", authResponse.getDeviceId());
    }

    @Test(expected = InvalidResponseException.class)
    public void testCatchesIOExceptionFromAuthDataNotParsableAndThrowsInvalidResponseException() throws Exception {
        when(crypto.decryptRSA(any(byte[].class))).thenReturn("XXX".getBytes());
        service.getAuthResponse("Auth Request ID");
    }
}
