package com.launchkey.sdk.service;

import com.launchkey.sdk.cache.CachePersistenceException;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.interfaces.RSAPublicKey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
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
public class V1ServiceAbstractTest extends V1ServiceTestBase {
    private ConcreteV1Service service;

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        service = new ConcreteV1Service(transport, crypto, pingResponseCache, appKey, secretKey);
    }

    @Override @After
    public void tearDown() throws Exception {
        service = null;
        super.tearDown();
    }

    @Test
    public void testGetLaunchKeyPublicKeySwallowsCachePersistenceErrorOnCacheGet() throws Exception {
        when(pingResponseCache.getPingResponse()).thenThrow(new CachePersistenceException("Duh"));
        assertEquals(publicKey, service.executeGetLaunchKeyPublicKey());
    }

    @Test
    public void testGetLaunchKeyPublicKeySwallowsCachePersistenceErrorOnCacheSet() throws Exception {
        doThrow(new CachePersistenceException("Duh")).when(pingResponseCache).setPingResponse(any(PingResponse.class));
        assertEquals(publicKey, service.executeGetLaunchKeyPublicKey());
    }

    private static class ConcreteV1Service extends V1ServiceAbstract {

        public ConcreteV1Service(
                Transport transport, Crypto crypto,
                PingResponseCache pingResponseCache,
                long appKey,
                String secretKey
        ) {
            super(transport, crypto, pingResponseCache, appKey, secretKey);
        }

        public RSAPublicKey executeGetLaunchKeyPublicKey() throws ApiException {
            return getLaunchKeyPublicKey();
        }
    }
}