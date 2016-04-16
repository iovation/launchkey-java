package com.launchkey.sdk;

import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.service.auth.AuthService;
import com.launchkey.sdk.service.whitelabel.WhiteLabelService;
import org.apache.http.client.HttpClient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
public class BasicClientTest {
    private static final String PRIVATE_KEY =
            ("-----BEGIN RSA PRIVATE KEY-----\n" +
                    "MIIEogIBAAKCAQEAq2izh7NEarDdrdZLrplizezZG/JzW14XQ74IXkjEqkvxhZ1s\n" +
                    "6joGtvoxX+P0QRyWrhgtiUnN3DSRAa0QLsZ0ZKk5a2AMGqu0/6eoJGwXSHcLreLE\n" +
                    "fqdd8+zlvDrbWISekTLoecLttwKSIcP2bcq1nAKz+ZNKMPvB/lm/dHXOqlnybo0J\n" +
                    "7efUkbd81fHrMOZNLKRYXx3Zx8zsQFf2ee/ypnnw5lKwX+9IBAT/679eGUlh8HfT\n" +
                    "SG6JQaNezyRG1cOd+pO6hKxff6Q2GVXqHsrIac4AlR80AEaBeiuFYxjHpruS6BRc\n" +
                    "yW8UvqX0l9rKMDAWNAtMWt2egYAe6XOEXIWOiQIDAQABAoIBADUmDOzZ0DAI0WPS\n" +
                    "m7rywqk5dIRu5AgDn9EYfn3FsH1heO1GR/xEq8pWv7KM+zKpS6uFwbDdGqDaB9Bu\n" +
                    "OiNW08ZWloBN0tL+ROw0rzVD8uA8UXnEY8sl2EMHRKDd2x+SV5yMHXuLzqu9d1RS\n" +
                    "7/lRLojGacnMOuf/WEKmz2+sC73UDfYm7Kq39LStE0Hi9iAq8eF+9U8b3l7Pikx/\n" +
                    "t70wOfCQJCrlfAFn0MdoxXoybr4HCy7tA2pqWPG2yhGnROaJSA430UNJQ9sU9p5M\n" +
                    "qyU8VWz8I2lFZkpflgf34D9sxt2BaRQvR0T0GBILHf0BfwDjlF+fdgZjQb0uTdez\n" +
                    "mcIhiNECgYEAxju+IzfDHis3GSu/6GALoDnxLpOi3y8QjBBa8nEd4XpRGAyaHgbt\n" +
                    "/Q03Sd9jfST0jP7hKyJPWiPR5l4M9BpCEuQlhxdpSdy0acvXhuwdAWawaOHkMcUV\n" +
                    "iBZfzOB0VY2L55RVpaAqO1rq0EOydsD3n9uX/eEjWiaEEZNhdzrcgkUCgYEA3Vva\n" +
                    "cW4wguSB7VWJDJCd+o69AS29tBQBqYtCXRokmzWU6hitNa36wJMI2/fTW2lxegAi\n" +
                    "8RJ8HRAj8D3GpwbdIm5tgH+2EBoGqraxwXfyt4NKiVvRFEyg0zLq31U9VDm11BlG\n" +
                    "KU6XdxzD5aC+/txML+ib85WQsVInKVdP5pXowXUCgYB2scT6f2QER2n5V1nUQNYV\n" +
                    "PTxtYBcQvbSRuSVLr3Ft1fiChuEtA4cyktw9DlYa06reVarrUeLjnTkMT9o/uw0/\n" +
                    "FH5n8huoD0+zXUuSzQPdF+ifFEq3hkOLNaJtISRnKZbQtd/GiS1gVuLsiuxr8MUU\n" +
                    "Yb8TU+AAFbnUcEPWyVbJZQKBgBPtjQDhNqTSBZBkPu5OpqpD52gPwiBQHMYyr0rK\n" +
                    "a7k9XaalihJnE0f69LU43mJAX+Ln2D1zuJC1P0cFiLjIuWe8IUeMN8vDTA5aXC5a\n" +
                    "qhMzUqaDCZOWQnRBBTwN5HOMrn3luJdHaANlJ42opwkys/ksK74GHPyZtMTYA21y\n" +
                    "2X1xAoGAW3Yu0n/VcvDcQZmE++iPDKLD/Mpc18G1sRLNwrdhVEgRVk8sfYiQxmOb\n" +
                    "NNHiXe4njK7waEKHPo86poV22FAum0zBMFSf9igfCk5kuL/pk4EVa58NftF69S8V\n" +
                    "Ud+Zy3E0RJXToW0t3Eo5UexVieglvpgxG7x1SCdvxYtTl6CZ520=\n" +
                    "-----END RSA PRIVATE KEY-----\n");

    private WhiteLabelService whiteLabel;
    private AuthService auth;
    private Provider provider;
    private Client client;

    @Before
    public void setUp() throws Exception {
        provider = new BouncyCastleProvider();
        auth = mock(AuthService.class);
        whiteLabel = mock(WhiteLabelService.class);
        client = new BasicClient(auth, whiteLabel);
    }

    @After
    public void tearDown() throws Exception {
        client = null;
        auth = null;
        whiteLabel = null;
        provider = null;
    }

    @Test
    public void testAuth() throws Exception {
        assertSame(auth, client.auth());
    }

    @Test
    public void testWhiteLabel() throws Exception {
        assertSame(whiteLabel, client.whiteLabel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryLongStringStringProviderThrowsIllegalArgumentExceptionForNullSecretKey() throws Exception {
        BasicClient.factory(12345, null, PRIVATE_KEY, provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryLongStringStringProviderThrowsIllegalArgumentExceptionForInvalidPrivateKey() throws Exception {
        BasicClient.factory(12345, "secret key", "Invalid private key", provider);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryLongStringStringProviderThrowsIllegalArgumentExceptionForNullProvider() throws Exception {
        BasicClient.factory(12345, "secret key", PRIVATE_KEY, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryLongStringStringProviderReturnsLaunchKeyClient() throws Exception {
        Client actual = BasicClient.factory(12345, "secret key", PRIVATE_KEY, null);
        assertNotNull(actual);
    }

    @Test
    public void testFactoryConfigReturnsClient() throws Exception {
        Config config = new Config(12345, "secret key")
                .setRSAPrivateKeyPEM(PRIVATE_KEY)
                .setJCEProvider(provider);
        assertNotNull(BasicClient.factory(config));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryConfigThrowsIllegalArgumentExceptionForNullSecretKey() throws Exception {
        Config config = new Config(12345, null)
                .setRSAPrivateKeyPEM(PRIVATE_KEY)
                .setJCEProvider(provider);
        BasicClient.factory(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryConfigThrowsIllegalArgumentExceptionForNoPrivateKeyOrPEM() throws Exception {
        Config config = new Config(12345, "secret key")
                .setJCEProvider(provider);
        BasicClient.factory(config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryConfigThrowsIllegalArgumentExceptionForNoProvider() throws Exception {
        Config config = new Config(12345, "secret key")
                .setRSAPrivateKeyPEM(PRIVATE_KEY);
        BasicClient.factory(config);
    }

    @Test
    public void testFactoryConfigAllowsCryptoInsteadOfProviderAndPPrivateKeyOrPEM() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class));
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsPrivateKeyInsteadOfPEM() throws Exception {
        Config config = new Config(12345, "secret key")
                .setPrivateKey(JCECrypto.getRSAPrivateKeyFromPEM(provider, PRIVATE_KEY))
                .setJCEProvider(provider);
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsProvidingApacheHttpClient() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setApacheHttpClient(mock(HttpClient.class));
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsProvidingApiBaseUrl() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setAPIBaseURL("https://api.launchkey.com/xxx");
        assertNotNull(BasicClient.factory(config));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryConfigThrowsIllegalArgumentExceptionForInvalidApiBaseUrl() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setAPIBaseURL("Invalid URI");
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsProvidingPingResponseCache() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setPingResponseCache(mock(PingResponseCache.class));
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsProvidingPingResponseCacheTTL() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setPingResponseCacheTTL(30);
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsProvidingHttpClientConnectionTTLSecs() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setHttpClientConnectionTTLSecs(30);
        assertNotNull(BasicClient.factory(config));
    }

    @Test
    public void testFactoryConfigAllowsProvidingHttpMaxClients() throws Exception {
        Config config = new Config(12345, "secret key")
                .setCrypto(mock(Crypto.class))
                .setHttpMaxClients(30);
        assertNotNull(BasicClient.factory(config));
    }
}