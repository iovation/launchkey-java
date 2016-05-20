package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.transport.v1.domain.AuthsRequest;
import com.launchkey.sdk.transport.v1.domain.AuthsResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
public class V1AuthServiceLoginTest extends V1AuthServiceTestBase{
    protected final AuthsResponse authsResponse = new AuthsResponse("auth request");

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        when(transport.auths(any(AuthsRequest.class))).thenReturn(authsResponse);
    }

    @Test
    public void testChecksPingResponseCache() throws Exception {
        service.login("username");
        verifyChecksPingResponseCache();
    }

    @Test
    public void testDoesNotCallPingWhenPingResponseCacheReturnsResponse() throws Exception {
        when(pingResponseCache.getPingResponse()).thenReturn(pingResponse);

        service.login("username");
        verifyDoesNotCallPingWhenPingResponseCacheReturnsResponse();
    }

    @Test
    public void testDoesCallPingWhenPingResponseCacheReturnsNull() throws Exception {
        service.login("username");
        verifyDoesCallPingWhenPingResponseCacheReturnsNull();
    }

    @Test
    public void testAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue() throws Exception {
        service.login("username");
        verifyAddsPingResponseToPingResponseCacheWhenPingCallReturnsValue();
    }

    @Test
    public void testUsesPemFromPingToCreatePublicKey() throws Exception {
        service.login("username");
        verifyUsesPemFromPingToCreatePublicKey();
    }

    @Test
    public void testPassesAuthsRequestToTransportAuthsCall() throws Exception {
        service.login("username");
        verify(transport).auths(any(AuthsRequest.class));
    }

    @Test
    public void testPassesCorrectAppKeyInAuthsRequest() throws Exception {
        service.login("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(appKey, argumentCaptor.getValue().getAppKey());
    }

    @Test
    public void testPassesCorrectRsaEncryptedSecretKeyInAuthsRequest() throws Exception {
        service.login("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Encrypted Value".getBytes()), argumentCaptor.getValue().getSecretKey());
    }

    @Test
    public void testRsaEncryptedWithCorrectPublicKeyToCreateSecretKeyInAuthsRequest() throws Exception {
        service.login("username");
        verifyRsaEncryptedWithCorrectPublicKeyToCreateSecretKey();
    }

    @Test
    public void testRsaEncryptedJsonWithCorrectDataToCreateSecretKeyInAuthsRequest() throws Exception {
        Date start = new Date();
        service.login("username");
        Date end = new Date();

        verifyRsaEncryptedJsonWithCorrectDataToCreateSecretKey(start, end);
    }

    @Test
    public void testPassesCorrectSignatureInAuthsRequest() throws Exception {
        service.login("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(base64.encodeAsString("RSA Signature".getBytes()), argumentCaptor.getValue().getSignature());
    }

    @Test
    public void testSignatureInAuthsRequestUsedEncryptedButNotEncodedSecretKeyValue() throws Exception {
        service.login("username");
        verifySignatureUsedEncryptedSecretKeyValue();
    }

    @Test
    public void testPassesOneAsSessionInAuthsRequest() throws Exception {
        service.login("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue().getSession());
    }

    @Test
    public void testPassesOneAsUserPushIdInAuthsRequest() throws Exception {
        service.login("username");
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(1, argumentCaptor.getValue().getUserPushID());
    }

    @Test
    public void testPassesContextInAuthsRequest() throws Exception {
        String expected = "Expected context";
        service.login("username", expected);
        ArgumentCaptor<AuthsRequest> argumentCaptor = ArgumentCaptor.forClass(AuthsRequest.class);
        verify(transport).auths(argumentCaptor.capture());
        assertEquals(expected, argumentCaptor.getValue().getContext());
    }
}
