package com.iovation.launchkey.sdk.crypto.jwe;

import com.iovation.launchkey.sdk.crypto.JCECrypto;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Jose4jJWEServiceTest {

    private final Provider provider = new BouncyCastleProvider();
    private Jose4jJWEService jweService;
    private KeyPair keyPair;
    private PublicKey publicKey;
    private String publicKeyFingerprint;


    @Before
    public void setUp() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        publicKeyFingerprint = "Public Key Fingerprint";

        jweService = new Jose4jJWEService((RSAPrivateKey) keyPair.getPrivate());
    }

    @After
    public void tearDown() throws Exception {
        publicKey = null;
        publicKeyFingerprint = null;
        keyPair = null;
        jweService = null;
    }

    @Test
    public void decryptOfKnownValuesProducesExpectedResult() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String jwe = "eyJhbGciOiAiUlNBLU9BRVAtMjU2IiwgImVuYyI6ICJBMjU2Q0JDLUhTNTEyIn0" +
                "." +
                "Y1MWFsSjxD2_dz5R4O5YxlltyyyD9TEWvfx78APqbHWbG93ABCHKoGVObyDDT0WewtrsOdTO64zkqoOo_aWQhTU93ghcrscQlWu-zeoT5Gm4g_HEDxblnmnquL5i_VmNwWtF9v-d540xc9kBzGaeCjh7TSeiovF5kdjzcBXnLcrhtk51ojjtE8B21kl3qk7XHKC7BH_wE9l8WZK-4qvFyRBJ7URga3XLBSpTDobUy--Wk2tRgbHw-WbELGf9Eylz99uKlfUrR7ayhrWF61vS2WrTpYtwW0E_AY21Uoxj8Orz0QlMmOtCKNcGfqpnVaV5QCveRB6pKbR0IT4i7Y7kmw" +
                "." +
                "tXye4I3nk1QMfp0jKSsPnA" +
                "." +
                "ATzHOCjSdR4OPnQEhKU9iVwr6zfoUJ4wdxZ-YZ852X0" +
                "." +
                "yWl6YlFVXgio_brw0UbqpP1CKrpg2gVQD16smhzu6Jk";

        @SuppressWarnings("SpellCheckingInspection")
        final String privateKeyPEM =
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


        final RSAPrivateKey privateKey = JCECrypto.getRSAPrivateKeyFromPEM(provider, privateKeyPEM);
        final JWEService jweService = new Jose4jJWEService(privateKey);

        String expected = "{\"test\": \"response\"}";
        String actual = jweService.decrypt(jwe);
        assertEquals(expected, actual);
    }

    @Test
    public void encryptCanBeDecrypted() throws Exception {
        String expected = "{\"test\": \"response\"}";
        assertEquals(expected, jweService.decrypt(jweService.encrypt(expected, publicKey, publicKeyFingerprint, "application/json")));
    }

    @Test
    public void encryptSetsKeyId() throws Exception {
        String expected = "{\"test\": \"response\"}";
        assertEquals(expected, jweService.decrypt(jweService.encrypt(expected, publicKey, publicKeyFingerprint, "application/json")));
    }

    @Test(expected = JWEFailure.class)
    public void decryptJoseExceptionThrowsJweFailure() throws Exception {
        jweService.decrypt("kjsdhflskd");
    }

    @Test
    public void getHeadersReturnsHeaders() throws Exception {
        final String jwe = "eyJhbGciOiAiUlNBLU9BRVAtMjU2IiwgImVuYyI6ICJBMjU2Q0JDLUhTNTEyIn0" +
                "." +
                "Y1MWFsSjxD2_dz5R4O5YxlltyyyD9TEWvfx78APqbHWbG93ABCHKoGVObyDDT0WewtrsOdTO64zkqoOo_aWQhTU93ghcrscQlWu-zeoT5Gm4g_HEDxblnmnquL5i_VmNwWtF9v-d540xc9kBzGaeCjh7TSeiovF5kdjzcBXnLcrhtk51ojjtE8B21kl3qk7XHKC7BH_wE9l8WZK-4qvFyRBJ7URga3XLBSpTDobUy--Wk2tRgbHw-WbELGf9Eylz99uKlfUrR7ayhrWF61vS2WrTpYtwW0E_AY21Uoxj8Orz0QlMmOtCKNcGfqpnVaV5QCveRB6pKbR0IT4i7Y7kmw" +
                "." +
                "tXye4I3nk1QMfp0jKSsPnA" +
                "." +
                "ATzHOCjSdR4OPnQEhKU9iVwr6zfoUJ4wdxZ-YZ852X0" +
                "." +
                "yWl6YlFVXgio_brw0UbqpP1CKrpg2gVQD16smhzu6Jk";
        Map<String, Object> expected = new LinkedHashMap<String, Object>();
        expected.put("alg", "RSA-OAEP-256");
        expected.put("enc", "A256CBC-HS512");

        assertEquals(expected, jweService.getHeaders(jwe));
    }
}
