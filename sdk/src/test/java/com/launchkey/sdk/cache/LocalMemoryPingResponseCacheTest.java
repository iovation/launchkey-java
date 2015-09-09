package com.launchkey.sdk.cache;

import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.junit.Test;

import static org.junit.Assert.*;

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
public class LocalMemoryPingResponseCacheTest {

    private LocalMemoryPingResponseCache cache;

    @Test
    public void testBelowReturnsCachedPingResponse() throws Exception {
        cache = new LocalMemoryPingResponseCache(Integer.MAX_VALUE);
        cache.setPingResponse(new PingResponse("2000-01-01 00:00:00", "2000-02-02 00:00:00", "Public Key"));
        assertEquals(new PingResponse("2000-01-01 00:00:00", "2000-02-02 00:00:00", "Public Key"), cache.getPingResponse());
    }

    @Test
    public void testAboveReturnsNull() throws Exception {
        cache = new LocalMemoryPingResponseCache(-1);
        cache.setPingResponse(new PingResponse("2000-01-01 00:00:00", "2000-02-02 00:00:00", "Public Key"));
        assertNull(cache.getPingResponse());
    }
 }