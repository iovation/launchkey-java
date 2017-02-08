package com.launchkey.sdk.cache; /**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class JavaxCacheTest {
    private javax.cache.Cache javaxCache;
    private JavaxCache cache;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        javaxCache = mock(javax.cache.Cache.class);
        cache = new JavaxCache(javaxCache);
    }

    @After
    public void tearDown() throws Exception {
        javaxCache = null;
        cache = null;
    }

    @Test
    public void getCallsJavaxCacheGet() throws Exception {
        cache.get("key");
        verify(javaxCache).get(eq("key"));
    }

    @Test
    public void getReturnsJavaxCacheGetResponse() throws Exception {
        when(javaxCache.get(anyString())).thenReturn("Value");
        assertEquals("Value", cache.get(null));
    }

    @Test
    public void getRaisesCacheExceptionOnException() throws Exception {
        Throwable cause = new javax.cache.CacheException();
        when(javaxCache.get(anyString())).thenThrow(cause);
        thrown.expect(CacheException.class);
        thrown.expectCause(is(cause));
        cache.get(null);
    }

    @Test
    public void putCallsJavaxCachePut() throws Exception {
        cache.put("key", "value");
        verify(javaxCache).put("key", "value");
    }


    @Test
    public void putRaisesCacheExceptionOnException() throws Exception {
        Throwable cause = new javax.cache.CacheException();
        doThrow(cause).when(javaxCache).put(anyString(), anyString());
        thrown.expect(CacheException.class);
        thrown.expectCause(is(cause));
        cache.put(null, null);
    }
}