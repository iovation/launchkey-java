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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
public class LaunchKeyClientTest {
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
    private Client targetClient;

    @Before
    public void setUp() throws Exception {
        provider = new BouncyCastleProvider();
        auth = mock(AuthService.class);
        whiteLabel = mock(WhiteLabelService.class);
        targetClient = mock(Client.class);
    }

    @After
    public void tearDown() throws Exception {
        targetClient = null;
        auth = null;
        whiteLabel = null;
        provider = null;
    }

    @Test
    public void testServicesConstructorAuth() throws Exception {
        client = new LaunchKeyClient(auth, whiteLabel);
        assertSame(auth, client.auth());
    }

    @Test
    public void testServicesConstructorWhiteLabel() throws Exception {
        client = new LaunchKeyClient(auth, whiteLabel);
        assertSame(whiteLabel, client.whiteLabel());
    }
    @Test
    public void testClientConstructorAuth() throws Exception {
        when(this.targetClient.auth()).thenReturn(this.auth);
        client = new LaunchKeyClient(targetClient);
        AuthService actual = client.auth();
        verify(targetClient).auth();
        assertSame(this.auth, actual);
    }

    @Test
    public void testClientConstructorWhiteLabel() throws Exception {
        when(this.targetClient.whiteLabel()).thenReturn(this.whiteLabel);
        client = new LaunchKeyClient(targetClient);
        WhiteLabelService actual = client.whiteLabel();
        verify(targetClient).whiteLabel();
        assertSame(this.whiteLabel, actual);
    }

    @Test
    public void testFactoryConfig() throws Exception {
        Config config = mock(Config.class);
        when(config.getCrypto()).thenReturn(mock(Crypto.class));
        when(config.getHttpMaxClients()).thenReturn(null);
        assertNotNull(LaunchKeyClient.factory(config));
    }

    @Test
    public void testFactoryValues() throws Exception {
        assertNotNull(LaunchKeyClient.factory(123L, "secret key", PRIVATE_KEY, this.provider));
    }
}