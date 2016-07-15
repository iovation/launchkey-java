package com.launchkey.sdk.service.ping;

import com.launchkey.sdk.cache.CachePersistenceException;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.error.PlatformErrorException;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.PingRequest;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.number.IsCloseTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

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
public class V1PingServiceTest {
    private final static String PUBLIC_KEY = "Public Key";
    private static final Date DATE = new Date(1000L);
    private PingResponseCache pingResponseCache;
    private Transport transport;
    private Crypto crypto;
    private V1PingService pingService;
    private RSAPublicKey publicKey;
    private PingResponse pingResponse;

    @Before
    public void setUp() throws Exception {
        pingResponseCache = mock(PingResponseCache.class);
        crypto = mock(Crypto.class);
        publicKey = mock(RSAPublicKey.class);
        when(crypto.getRSAPublicKeyFromPEM(anyString())).thenReturn(publicKey);
        pingResponse = new PingResponse(null, DATE, PUBLIC_KEY);
        transport = mock(Transport.class);
        when(transport.ping(any(PingRequest.class))).thenReturn(pingResponse);
        pingService = new V1PingService(pingResponseCache, transport, crypto);
    }

    @After
    public void tearDown() throws Exception {
        pingResponseCache = null;
        transport = null;
        pingResponse = null;
        publicKey = null;
        crypto = null;
        pingService = null;
    }

    @Test
    public void getPublicKeyChecksCache() throws Exception {
        pingService.getPublicKey();
        verify(pingResponseCache).getPingResponse();
    }

    @Test
    public void getPublicKeyCallsPingOnTransportWhenNoPingResponseReturnedFromCache() throws Exception {
        when(pingResponseCache.getPingResponse()).thenReturn(null);
        pingService.getPublicKey();
        InOrder inOrder = inOrder(pingResponseCache, transport);
        inOrder.verify(pingResponseCache).getPingResponse();
        inOrder.verify(transport).ping(any(PingRequest.class));
    }

    @Test
    public void getPublicKeyCallsPingOnTransportWhenExceptionThrownBYCache() throws Exception {
        when(pingResponseCache.getPingResponse()).thenThrow(new CachePersistenceException(null));
        pingService.getPublicKey();
        InOrder inOrder = inOrder(pingResponseCache, transport);
        inOrder.verify(pingResponseCache).getPingResponse();
        inOrder.verify(transport).ping(any(PingRequest.class));
    }

    @Test
    public void getPublicKeyReturnsPublicKeyFromCacheValueWhenCacheHit() throws Exception {
        PingResponse otherPingResponse = new PingResponse("other fingerprint", new Date(999999L), "other public key");
        when(pingResponseCache.getPingResponse()).thenReturn(otherPingResponse);
        pingService.getPublicKey();
        verify(crypto).getRSAPublicKeyFromPEM(otherPingResponse.getPublicKey());
    }

    @Test
    public void getPublicKeyReturnsPublicKeyFromPingCallWhenNoCacheHit() throws Exception {
        PingResponse otherPingResponse = new PingResponse("other fingerprint", new Date(999999L), "other public key");
        when(pingResponseCache.getPingResponse()).thenReturn(null);
        pingService.getPublicKey();
        verify(crypto).getRSAPublicKeyFromPEM(PUBLIC_KEY);
    }

    @Test
    public void getPublicKeyDoesNotErrorWhenExceptionThrownByCachePersist() throws Exception {
        doThrow(new CachePersistenceException(null)).when(pingResponseCache).setPingResponse(any(PingResponse.class));
        pingService.getPublicKey();
        InOrder inOrder = inOrder(pingResponseCache, transport);
        inOrder.verify(pingResponseCache).getPingResponse();
        inOrder.verify(transport).ping(any(PingRequest.class));
    }

    @Test(expected = BaseException.class)
    public void getPublicKeyThrowsBaseExceptionWhenTransportThrowsApiException() throws Exception {
        when(transport.ping(any(PingRequest.class))).thenThrow(new ApiException());
        pingService.getPublicKey();
    }

    @Test
    public void getPlatformTimeCallsPingOnlyOnceButNeverChecksCacheForMultipleCalls() throws Exception {
        pingService.getPlatformTime();
        pingService.getPlatformTime();
        pingService.getPlatformTime();
        pingService.getPlatformTime();
        pingService.getPlatformTime();
        pingService.getPlatformTime();
        verify(transport, times(1)).ping(any(PingRequest.class));
        verify(pingResponseCache, never()).getPingResponse();
    }

    @Test
    public void getPlatformTimeReturnsPingResponseTimeForFirstCall() throws Exception {
        Date start = new Date();
        Date actual = pingService.getPlatformTime();
        Date end = new Date();

        assertEquals(DATE, pingService.getPlatformTime());
        assertThat(actual, isCloseTo(DATE, end.getTime() - start.getTime()));
    }

    @Test
    public void getPlatformTimeReturnsPingResponseTimePlusElapsedTimeForSubsequentCall() throws Exception {
        pingService.getPlatformTime();
        Thread.sleep(200);
        assertThat(pingService.getPlatformTime(), is(greaterThanOrEqualTo(new Date(DATE.getTime() + 200))));
    }

    @Test
    public void getPlatformTimeReturnsSystemDateWhenPingErrors() throws Exception {
        pingService.getPlatformTime();
        Thread.sleep(200);
        assertThat(pingService.getPlatformTime(), is(greaterThanOrEqualTo(new Date(DATE.getTime() + 200))));
    }

    @Test(expected = PlatformErrorException.class)
    public void getPlatformTimeThrowsBaseExceptionWhenTransportThrowsPlatformErrorException() throws Exception {
        when(transport.ping(any(PingRequest.class))).thenThrow(new ApiException());
        pingService.getPlatformTime();
    }

    @Test(expected = InvalidResponseException.class)
    public void getPlatformTimeThrowsConvertedInvalidResponseException() throws Exception {
        when(transport.ping(any(PingRequest.class)))
                .thenThrow(new com.launchkey.sdk.service.error.InvalidResponseException(null, 0));
        pingService.getPlatformTime();
    }

    @Test(expected = CommunicationErrorException.class)
    public void getPlatformTimeThrowsConvertedCommunicationErrorException() throws Exception {
        when(transport.ping(any(PingRequest.class)))
                .thenThrow(new com.launchkey.sdk.service.error.CommunicationErrorException(null, 0));
        pingService.getPlatformTime();
    }

    public static Matcher<Date> isCloseTo(final Date item, final long error) {

        return new BaseMatcher<Date>() {

            protected Date testValue = item;
            protected long threshold = error;

            @Override public boolean matches(Object item) {
                Date dateItem = (Date) item;
                return dateItem.getTime() >= testValue.getTime() - threshold
                        && dateItem.getTime() <= testValue.getTime() + threshold;
            }

            @Override public void describeTo(Description description) {
                description.appendValue(testValue);
            }
        };
    }
}
