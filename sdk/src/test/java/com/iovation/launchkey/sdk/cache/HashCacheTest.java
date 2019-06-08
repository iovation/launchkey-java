package com.iovation.launchkey.sdk.cache;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HashCacheTest {

    private HashCache cache;

    @Before
    public void setUp() throws Exception {
        cache = new HashCache();
    }

    @Test(expected = CacheException.class)
    public void putRaisesCacheExceptionForNull() throws Exception {
        cache.put(null, null);
    }

    @Test(expected = CacheException.class)
    public void gettRaisesCacheExceptionForNull() throws Exception {
        cache.get(null);
    }

    @Test
    public void getReturnsValueWhenSet() throws Exception {
        String expected = "Hello, World!";
        cache.put("key", expected);
        assertEquals(expected, cache.get("key"));
    }

    @Test
    public void getReturnsNullWhenNotSet() throws Exception {
        assertNull(cache.get("key"));
    }
}