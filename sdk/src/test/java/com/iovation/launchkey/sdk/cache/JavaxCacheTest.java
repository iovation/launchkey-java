package com.iovation.launchkey.sdk.cache; /**
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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JavaxCacheTest {

    @Mock
    private javax.cache.Cache<String, String> javaxCache;
    private JavaxCache cache;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        cache = new JavaxCache(javaxCache);
    }

    @Test
    public void getCallsJavaxCacheGet() throws Exception {
        cache.get("key");
        verify(javaxCache).get(eq("key"));
    }

    @Test
    public void getReturnsJavaxCacheGetResponse() throws Exception {
        when(javaxCache.get(anyString())).thenReturn("Value");
        assertEquals("Value", cache.get(""));
    }

    @Test
    public void getRaisesCacheExceptionOnException() throws Exception {
        Throwable cause = new javax.cache.CacheException();
        when(javaxCache.get(anyString())).thenThrow(cause);
        thrown.expect(CacheException.class);
        thrown.expectCause(is(cause));
        cache.get("");
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
        cache.put("", "");
    }
}