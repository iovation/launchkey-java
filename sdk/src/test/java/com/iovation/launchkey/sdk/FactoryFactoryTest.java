package com.iovation.launchkey.sdk;

import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.client.DirectoryFactory;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.transport.domain.EntityKeyMap;
import org.apache.http.client.HttpClient;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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
                    "MIIEpAIBAAKCAQEAn+agtJ3eGHNxwkkss2jTCSdcLNrWKawjmvNc27YFe8cVmBlJ\n" +
                    "e/GCru+uIiN9BLncK3B/d1IyZRvC/qza8Gk7SA3/EbXmuo05vnwNuGKLS9+3CJqn\n" +
                    "F0ih7+Umfs8xFqWt0o4Ss5znDgjMuIdI0Aa34imUR0VjMWozTtBZQ4S2+x3zYdKF\n" +
                    "Qgc0QbejI3VjmATUTsf+e4lDxL29yFeK0XIGCWL4bmpF37Kys8+N84iT7Pox/8Jx\n" +
                    "G+P/Po6apijJ8EmCaFwEoLwI7vtIdEzDbK3JYB0QyWtEhpDIApY/H1EV+OlSWovy\n" +
                    "oMcTR3rN2QJruQWjhwbkQXP0FVbUvlyjNcOPgQIDAQABAoIBAFttIrYjDbotR/4u\n" +
                    "mMB8Ty4Ms0dyWMG7CyCtxYpaqQf87PvqeXnEvVQI9yfN4s2w95Je4IL5HJdzKCs1\n" +
                    "6nlLsfmhBxa5SxOfbETd6Xpj3gGkCKOCyccmYHEqNLiM/yRT1u9tDIRmUjZUEbnw\n" +
                    "oFFRhhJqc73MjsrSTkPiHSfd2kH6FplOYjEp6Jw85kZIsajji99LwYrGZU+tIRdo\n" +
                    "6XBfvpKKqSQgcFhypXRDF4F3gnCve8VUsVCFomx9b+D1LLfABOB+kP8n1k5QLsc4\n" +
                    "HUDy0DzEFQWbQt7w8NKS+JOudTyl9MeUtC39gV7iNm33qlMcaP+ibcBIc6JftPB1\n" +
                    "D63Mt4ECgYEA0B0MsgSWQOWGOE14VdDoJVnVZ8J6aCHzs8etyKLKqz85AR8+OQbR\n" +
                    "75Zr8pZGTl1v/w5yThkin/qhBbVrXugEMDpsB3W8V3R6fnOvBI9/JD1fpOdsiF0P\n" +
                    "8Jb/T5+P+Qm9bFEgpODarCWDOx5EQmk3hCgbVQbdyI45/LxPvXSY6dMCgYEAxLGc\n" +
                    "GaM73oywMqEzG241dsDBZPFoKYf99IwSeFXxJRqNP1oJlmfVMJgxdpsGWBZNxpXR\n" +
                    "nyY7qwBgYVub4FdSrf6xWFi9FlP7c+0cuzHlSoWiaPl+kVpWinF3YOpkI9qnhDkD\n" +
                    "RWyCfMzmmBWXTN9j5savWTZH/ON0kYDHmFhBWNsCgYB1rzeyoy1kXWbdB9H4lT3x\n" +
                    "gS9QkYUuxaROEaiQQJ5i4EmX/Nbu4JBu75zzfU8cmXHvxXeV+li+JrhPxhuFe6aC\n" +
                    "r0bPoSB0RAXQ5BIgfhTjpWEkm0mggx+0aW7O0+hSZ9OaIPZYxl5h4oSnNaJb+6vN\n" +
                    "K/Do0A0ykFryNHrTI9AkyQKBgQCdLqHkCwII8rzLeO5yeTvYTmk91Xy706UqC9Wm\n" +
                    "vGcck39UpQsMWqWPU9ATTAdh1VEJ5SfC2rbMDFz48FA8kwvLZ8EpLKDOewZyuYVA\n" +
                    "QrtNNkV3RgnS9jI6KDqSBD1KOa2siDMJA5GzUbrkxjxGfi5DeXse+7XDbbD1pcfb\n" +
                    "AowWCQKBgQCTgi04tg5Ar3VHpmcfyJOA5UtRS044LWtr0Kr5S0uwxwf6aE/dUrTu\n" +
                    "7MM89vvNH6FTzh76P4B+sgEuNDQ1KA/Pa9SgTibVqU/kE62Nyg6AcTkS2bdr9ENd\n" +
                    "mo5GzkQVT4GyetA0hQJoJorT2Rfx9KSCCQ6cdNKnhvxjEYgbJuRKfw==\n" +
                    "-----END RSA PRIVATE KEY-----";


    private FactoryFactory factoryFactory;

    @Before
    public void setUp() throws Exception {
        Provider provider = new BouncyCastleProvider();
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
