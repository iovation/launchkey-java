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

package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.cache.CacheException;
import com.launchkey.sdk.transport.v1.domain.AuthsRequest;
import com.launchkey.sdk.transport.v1.domain.AuthsResponse;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class V1AuthServicePingCacheTest extends V1AuthServiceTestBase {
    private AuthsResponse authResponse;

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        authResponse = new AuthsResponse("AuthRequest");
        when(transport.auths(any(AuthsRequest.class))).thenReturn(authResponse);
    }

    @Override @After
    public void tearDown() throws Exception {
        authResponse = null;
        super.tearDown();
    }

    @Test
    public void testGetPublicKeySwallowsCachePersistenceErrorOnCacheGet() throws Exception {
        when(pingResponseCache.getPingResponse()).thenThrow(new CacheException("Duh"));
        assertEquals(authResponse.getAuthRequestId(), service.authorize("username"));
    }

    @Test
    public void testGetPublicKeySwallowsCachePersistenceErrorOnCacheSet() throws Exception {
        doThrow(new CacheException("Duh")).when(pingResponseCache).setPingResponse(any(PingResponse.class));
        assertEquals(authResponse.getAuthRequestId(), service.authorize("username"));
    }
}
