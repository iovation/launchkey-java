package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.transport.v1.domain.LogsRequest;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.Assert.*;
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
public class V1AuthServiceLogoutTest extends V1AuthServiceTestBase {

    @Test
    public void testChecksPingResponseCache() throws Exception {
        service.logout("Auth Request ID");
        verifyChecksPingResponseCache();
    }

    @Test
    public void testDoesNotCallPingWhenPingResponseCacheReturnsResponse() throws Exception {
        when(pingResponseCache.getPingResponse()).thenReturn(pingResponse);

        service.logout("Auth Request ID");
        verifyDoesNotCallPingWhenPingResponseCacheReturnsResponse();
    }

    @Test
    public void testDoesCallPingWhenPingResponseCacheReturnsNull() throws Exception {
        service.logout("Auth Request ID");
        verifyDoesCallPingWhenPingResponseCacheReturnsNull();
    }

    @Test
    public void testAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue() throws Exception {
        service.logout("Auth Request ID");
        verifyAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue();
    }

    @Test
    public void testUsesPemFromPingToCreatePublicKey() throws Exception {
        service.logout("Auth Request ID");
        verifyUsesPemFromPingToCreatePublicKey();
    }

    @Test
    public void testPassesCorrectRsaEncryptedSecretKeyInLogsRequest() throws Exception {
        service.logout("Auth Request ID");
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        verify(transport).logs(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Encrypted Value".getBytes()), argumentCaptor.getValue().getSecretKey());
    }

    @Test
    public void testRsaEncryptedWithCorrectPublicKeyToCreateSecretKeyInLogsRequest() throws Exception {
        service.logout("Auth Request ID");
        verifyRsaEncryptedWithCorrectPublicKeyToCreateSecretKey();
    }

    @Test
    public void testRsaEncryptedJsonWithCorrectDataToCreateSecretKeyInLogsRequest() throws Exception {
        Date start = new Date();
        service.logout("Auth Request ID");
        Date end = new Date();
        verifyRsaEncryptedJsonWithCorrectDataToCreateSecretKey(start, end);
    }

    @Test
    public void testPassesCorrectSignatureInLogsRequest() throws Exception {
        service.logout("Auth Request ID");
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        verify(transport).logs(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Signature".getBytes()), argumentCaptor.getValue().getSignature());
    }

    @Test
    public void testSignatureInLogsRequestUsedEncryptedButNotEncodedSecretKeyValue() throws Exception {
        service.logout("Auth Request ID");
        verifySignatureUsedEncryptedSecretKeyValue();
    }

    @Test
    public void testPassesCorrectAuthRequestInLogsRequest() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        service.logout("Auth Request ID");
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("Auth Request ID", argumentCaptor.getValue().getAuthRequest());
    }

    @Test
    public void testPassesCorrectAppKeyInLogsRequest() throws Exception {
        service.logout("Auth Request ID");
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        verify(transport).logs(argumentCaptor.capture());
        assertEquals(appKey, argumentCaptor.getValue().getAppKey());
    }

    @Test
    public void testSendsLogsCallWithActionRevoke() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        service.logout("Auth Request ID");
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("Revoke", argumentCaptor.getValue().getAction());
    }

    @Test
    public void testSendsLogsCallWithStatusTrue() throws Exception {
        ArgumentCaptor<LogsRequest> argumentCaptor = ArgumentCaptor.forClass(LogsRequest.class);
        service.logout("Auth Request ID");
        verify(this.transport).logs(argumentCaptor.capture());
        assertEquals("true", argumentCaptor.getValue().getStatus());
    }
}
