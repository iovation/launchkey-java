package com.launchkey.sdk;

import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import org.apache.http.client.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.Provider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
public class ConfigTest {
    private Config config;

    @Before
    public void setUp() throws Exception {
        config = new Config(12345, "secret key");
    }

    @After
    public void tearDown() throws Exception {
        config = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullThrowsIllegalArgumentException() throws Exception {
        new Config(12345, null);
    }

    @Test
    public void testGetRocketKey() throws Exception {
        assertEquals(12345, config.getRocketKey());
    }

    @Test
    public void testGetSecretKey() throws Exception {
        assertEquals("secret key", config.getSecretKey());
    }

    @Test
    public void testSetCrypto() throws Exception {
        Crypto expected = mock(Crypto.class);
        assertSame(expected, config.setCrypto(expected).getCrypto());
    }

    @Test
    public void testGetCryptoDefaultsToNull() throws Exception {
        assertNull(config.getCrypto());
    }

    @Test
    public void testSetJCEProvider() throws Exception {
        Provider expected = mock(Provider.class);
        assertSame(expected, config.setJCEProvider(expected).getJCEProvider());
    }

    @Test
    public void testGetJCEProviderDefaultsToNull() throws Exception {
        assertNull(config.getJCEProvider());
    }

    @Test
    public void testSetPrivateKey() throws Exception {
        PrivateKey expected = mock(PrivateKey.class);
        assertSame(expected, config.setPrivateKey(expected).getPrivateKey());
    }

    @Test
    public void testGetPrivateKeyDefaultsToNull() throws Exception {
        assertNull(config.getPrivateKey());
    }

    @Test
    public void testSetRSAPrivateKeyPEM() throws Exception {
        final String expected = "PEM";
        assertSame(expected, config.setRSAPrivateKeyPEM(expected).getRSAPrivateKeyPEM());
    }

    @Test
    public void testGetRSAPrivateKeyPEMDefaultsToNull() throws Exception {
        assertNull(config.getRSAPrivateKeyPEM());
    }

    @Test
    public void testSetApacheHttpClient() throws Exception {
        HttpClient expected = mock(HttpClient.class);
        assertSame(expected, config.setApacheHttpClient(expected).getApacheHttpClient());
    }

    @Test
    public void testGetApacheHttpClientDefaultsToNull() throws Exception {
        assertNull(config.getApacheHttpClient());
    }

    @Test
    public void testGetHttpClientConnectionTTLSecsDefaultsToNull() throws Exception {
        int expected = 12345;
        assertEquals(
                (Integer) expected,
                config.setHttpClientConnectionTTLSecs(expected).getHttpClientConnectionTTLSecs()
        );
    }

    @Test
    public void testSetHttpClientConnectionTTLSecs() throws Exception {
    }

    @Test
    public void testGetHttpMaxClientsDefaultsToNull() throws Exception {
        assertNull(config.getHttpMaxClients());
    }

    @Test
    public void testSetHttpMaxClients() throws Exception {
        int expected = 12345;
        assertEquals((Integer) expected, config.setHttpMaxClients(expected).getHttpMaxClients());
    }

    @Test
    public void testGetPingResponseCacheTTLDefaultsToNull() throws Exception {
        assertNull(config.getPingResponseCacheTTL());
    }

    @Test
    public void testSetPingResponseCacheTTL() throws Exception {
        int expected = 12345;
        assertEquals((Integer) expected, config.setPingResponseCacheTTL(expected).getPingResponseCacheTTL());
    }

    @Test
    public void testGetAPIBaseURLDefaultsToNull() throws Exception {
        assertNull(config.getAPIBaseURL());

    }

    @Test
    public void testSetAPIBaseURL() throws Exception {
        String expected = "Expected URL";
        assertSame(expected, config.setAPIBaseURL(expected).getAPIBaseURL());
    }

    @Test
    public void testGetPingResponseCacheDefaultsToNull() throws Exception {
        assertNull(config.getPingResponseCache());
    }

    @Test
    public void testSetPingResponseCache() throws Exception {
        PingResponseCache expected = mock(PingResponseCache.class);
        assertSame(expected, config.setPingResponseCache(expected).getPingResponseCache());
        assertSame(expected, config.setPingResponseCache(expected).getPingResponseCache());
    }
}