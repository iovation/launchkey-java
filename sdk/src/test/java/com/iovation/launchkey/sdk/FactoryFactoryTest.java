package com.iovation.launchkey.sdk;

import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.client.DirectoryFactory;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.transport.domain.EntityKeyMap;
import org.apache.http.client.HttpClient;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class FactoryFactoryTest {
    private static final String UUID1 = "49af9c38-31b3-11e7-93ae-92361f002671";

    @SuppressWarnings("SpellCheckingInspection")
    private static final String privateKeyPEM =
            "-----BEGIN RSA PRIVATE KEY-----\n" +
                    "MIIJQwIBADANBgkqhkiG9w0BAQEFAASCCS0wggkpAgEAAoICAQCZ4sq1uy7AGXkB\n" +
                    "0tD4DGk8c+oJBml1TkToxf2S+lHH0kUTIOThSKxEehGBPb+tTytwkGU7kW/mta8c\n" +
                    "2XzMAzx8Zc10Jjc5LWpj4boQLORZjSz5tpshfHTffM6lsMKq/T4KWk6QNAg1AQWN\n" +
                    "npY3oz72kSxcp8+qSyy6QnBLjv+8I8eMCft4Ozu6ZgjkaKToRk7yuifyzKehzMaT\n" +
                    "s5gn5cGqA0vQ95Ple/1hwzfyXSXy1dhRTxNuFDOYhbdOxfxE2v3dB6hXq+aP2icS\n" +
                    "9WAkUAKojvLwlIb0RFgdtdl3l/mdygQy7NnIVCFK01khfuEj+zKYkMjQrzFQpV3K\n" +
                    "R4EuN/hyvtLkY+cJytiY1woileAKNtxHfQDavnt6cJbJLHf7XgJ5gR3GbR13bctk\n" +
                    "sBwUktO+TH572bpZrg9rtOM6Rsdx7Thpk0hjBgc6WE5MFhwCuu5TPQhYEIIhoSqL\n" +
                    "NYaCxwf/75ip7ytRXx8unDQWcoBlCRkJUgC9ks65iv0GgpyKwbJkx/9x9/MIWELU\n" +
                    "wy5YTO28l6pg88PbR0pdzfwk7rJz6PIBf+SIkEJJKhSyMBQDzc62wCkxe9qjX/C7\n" +
                    "2+m/uoHYgaSUqfVEVPXRCqsRjGJ8sAsj1y/jvTeNlOnbPajUbOg5sXBxy6QXxR+l\n" +
                    "6dshEpOn6tQpTW5Je3aOzGO8OIKjcwIDAQABAoICAQCDgtZKSRXDBgHsFQaRdcnZ\n" +
                    "6BXycJBft+wcIlh664JIiuKNXmc8QKc4PjoHgYX1ztsI794T8k73k/17YkLM91cR\n" +
                    "2M6knKRFMRLjGV2xxSvBfG0bW3toOryG4lqYL1+uUY5buqG5iz4YTi84wHcQRWrh\n" +
                    "vV90L9XBa01mQLFHYYYce1dlzoIiVjzNJ7YhvWxxNOTzezP4w/3ewOpT///g2PGR\n" +
                    "IhABHhQ+4p/AlN2TOxfbV/XuRu4L/Se7CUuG/pyBG8YoxwqPIlGATqdmBF2NqvMa\n" +
                    "qk/kWoqa5m2HtomLM04YEzVTcCcOlRd+ovBhN9WgsmSjDdkQvygeC7UD0egMX7jq\n" +
                    "vtj2cSxX8jUADy8PKjRPs8KlhflJmb1G1Tkanoi+PkAmPlZupscF34AzepHrdwNc\n" +
                    "wbeJ1W4QXcQ3V/RqwaM+St4JkXmmYjbW5gh0RiE3BcMNTewIeIE/nd8Vx3jTzbE6\n" +
                    "wz+WesdNdnkEeFIOGGVwKlVbWfMI5R2Ou3I+vOd79yiu3aMiygJYZ26C040CUYUB\n" +
                    "VpDPWufDb4KHedxhq1snJMhBMDaeCsUrr9qsqs9ZZh3+9oEVwQesKptR4NSWEuY3\n" +
                    "3EZVxXsG5xzY+S4+jqvdzNr1RD47Nch/xhf9tbz7r8DH9cUokX7o8Oa89syXG/Wi\n" +
                    "0SrrHGkSvg7aM0M4fSD8gQKCAQEA7CA4avCLuOvxYyEN7bSNszTUABa3lxHEAF8f\n" +
                    "AxqKil585ug1SJj6V94fVviJUIs70DMFBIEfWnMh7xvKwFIqC/LiSqlroUjiXHoT\n" +
                    "PY7nblyi+u17z16EgZJ4ziUSDIvAg5pul4bDg5FoAoUppXV0DR3peHBDv+3aMu7S\n" +
                    "pSsLUQEYsPkP8qIocGLvCEe3J/pXvWcHJLjvwzQoWp2aZnoezPyf4YxFcjhQv03b\n" +
                    "dPP3oDxflJIFhUfAlp4ZQnH5wtS+qD2FZfUSL0jwK++20Z4DeXq6hJQmjS+l0bsH\n" +
                    "71DHUeo6VP/pIABo0sOVvnTN304ukqZux5UY8bjrotsh6imYuwKCAQEAptaOPAM2\n" +
                    "Xszw28yRDKNOHIbUBRezZNAPEDs/uBFJvfbTQL83RpJAuLp3gR4axuoKvpsy6CD1\n" +
                    "PEwAgus4uKw0GzWQuFDl7wyzMbGoDNozgrbTZNYyRiQNUAN8mUNnjp6U0e3OQSoq\n" +
                    "exlfQhPRgugqELBSDvVZWi4xP2fHoggJcn/nwow/9ECqLo/hVSyp2pQEJ7yv3l4w\n" +
                    "Vj31/PyjJHIFwsa7bElZ5ElGBjDJeAteUTn5LK/xtBuxYAO0bMEhT+1ohz8etzec\n" +
                    "N0h3mt5Zdr5F+wfma1kCGLikFbbC3JL9SYVB2FbMgcYCBqfD1J75wb+JnzNXGjcF\n" +
                    "uRYzE8B+zoFwqQKCAQEAodvEUKnrXSt/IDB8V8E4kOtZl2X7Gzc2X/rUS1BaP1dd\n" +
                    "zvrF66nRkYHVgcyEdA29Ro5ylg/c6ieZz0oBxauM3vvzWrKf8MMBR9r2bXAT/HbL\n" +
                    "0a4Q/KkRs7Av1z9aC/eQU6X8wSnDw+Bcp72YOq80iflDHSf3iQ0GUXucMVQ8QZ66\n" +
                    "yjUwVWYKyl9G8yoVxvW4R0DkiKuszuZl8xetyylTC7jv77AzuoQX9crs8FJ8H/7C\n" +
                    "lhkyZ5Yz0gs1zXJLft5Ogw0I8Eb53CfnWnbLnwzt3MvgJxlXA9jxlb7bRZTdzKz+\n" +
                    "p1109FbThAZGE3QF21jAXA5ySaVOoAPeopgLu3QgGQKCAQBNTZCt4dcpadAYJ9r1\n" +
                    "fh1NPnOywF6Q0Y6JOMq3YNtIN7t+fpsACfgPH+cLXoWNsRe1ZXfa8ppui9CY2KB5\n" +
                    "gODL0q/xlxpS/xFwbx6shdXkNQ4R5OV6dm3sqxDqer7a6EOQWZ19uCniy8jFdyVW\n" +
                    "gHgtL2V2JNx32ntbI5zuSMcH1JfwHsfrRqMT2/rOWlmBO6AJQXZDlGTVMPRveiel\n" +
                    "VWex7h8dd4c9LW5So/xVsP7MqA36VLOrfkFbeZv54CqtPBV4xRhYUF4Dh4JTsb7G\n" +
                    "NDd8rxZmmuFLzxHINdxoE3tku2fc86riXnrF1qn4NIkI6tS7fTBYpzHxpoWYG1Mm\n" +
                    "H/exAoIBADVG6h2dEmhdimFL7ghw+Tx9j2yDOFkH1phq4LU3AV+E9HqYUXcqYWTB\n" +
                    "IZt+xZHk8AoYg2SBjiMke6lIsEk+uADW1qUU1TMYl7ajKpLCtNfNykSP8bt1wT8c\n" +
                    "MXTW635SDs6Ts1iPrjxijn/XjL7HYF9YKwpdyRtC3n4n69hdF/j9S2XFNAdH4ENa\n" +
                    "Cs1unFgMgp+k5CVwVsEAdfEt/o1XECNepC/mOqjPIl6U5c+1SCdmy1iHy9trZJ1c\n" +
                    "fH0GRVJfFNdq+1LpeeMTTNeXS3WSBG/ErgWSKfvQ0mXfZ7sQHvvJSWXpv8m1xtI0\n" +
                    "z7EJEc1gLGLLZnL3KAGc+re1tjx5wCM=\n" +
                    "-----END RSA PRIVATE KEY-----";


    private FactoryFactory factoryFactory;

    @Before
    public void setUp() throws Exception {
        Provider provider = new BouncyCastleFipsProvider();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);

        factoryFactory = new FactoryFactory(
                provider,
                mock(HttpClient.class),
                mock(Cache.class),
                "https://base/url",
                "id",
                0,
                0,
                0,
                new EntityKeyMap()
        );
    }

    @After
    public void tearDown() throws Exception {
        factoryFactory = null;
    }

    @Test
    public void makeServiceFactoryWithKey() throws Exception {
        assertThat(
                factoryFactory.makeServiceFactory(UUID1, privateKeyPEM),
                instanceOf(ServiceFactory.class)
        );
    }

    @Test
    public void makeServiceFactoryWithKeymap() throws Exception {
        assertThat(
                factoryFactory.makeServiceFactory(UUID1, new HashMap<String, RSAPrivateKey>(), "Current key"),
                instanceOf(ServiceFactory.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void makeDirectoryFactoryWithNullUUIDThrowsIllegalArgument() throws Exception {
        factoryFactory.makeDirectoryFactory(null, privateKeyPEM);
    }

    @Test
    public void makeDirectoryFactoryWithKey() throws Exception {
        assertThat(
                factoryFactory.makeDirectoryFactory(UUID1, privateKeyPEM),
                instanceOf(DirectoryFactory.class)
        );
    }

    @Test
    public void makeDirectoryFactoryWithKeymap() throws Exception {
        assertThat(
                factoryFactory.makeDirectoryFactory(UUID1, new HashMap<String, RSAPrivateKey>(), "Current key"),
                instanceOf(DirectoryFactory.class)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void makOrganizationFactoryWithNullUUIDThrowsIllegalArgument() throws Exception {
        factoryFactory.makeOrganizationFactory(null, privateKeyPEM);
    }

    @Test
    public void makeOrganizationFactoryWithKey() throws Exception {
        assertThat(
                factoryFactory.makeOrganizationFactory(UUID1, privateKeyPEM),
                instanceOf(OrganizationFactory.class)
        );
    }

    @Test
    public void makeOrganizationFactoryWithKeymap() throws Exception {
        assertThat(
                factoryFactory.makeOrganizationFactory(UUID1, new HashMap<String, RSAPrivateKey>(), "Current key"),
                instanceOf(OrganizationFactory.class)
        );
    }

}
