package com.iovation.launchkey.sdk.crypto.jwe;

import com.iovation.launchkey.sdk.crypto.JCECrypto;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.Security;
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

    private final Provider provider = new BouncyCastleFipsProvider();
    private Jose4jJWEService jweService;
    private KeyPair keyPair;
    private PublicKey publicKey;
    private String publicKeyID;


    @Before
    public void setUp() throws Exception {
        if (Security.getProvider(BouncyCastleFipsProvider.PROVIDER_NAME) == null) {
            Security.addProvider(provider);
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", provider);
        keyPairGenerator.initialize(2048);
        keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        publicKeyID = "Public Key ID";

        jweService = new Jose4jJWEService((RSAPrivateKey) keyPair.getPrivate(), provider.getName());
    }

    @After
    public void tearDown() throws Exception {
        publicKey = null;
        publicKeyID = null;
        keyPair = null;
        jweService = null;
    }

    @Test
    public void deprecatedConstructorStillWorks() throws Exception {
        @SuppressWarnings("deprecation") JWEService jweService = new Jose4jJWEService((RSAPrivateKey) keyPair.getPrivate());
        String expected = "{\"auth_request\": \"AuthRequest\", \"action\": \"True\"," +
                " \"app_Pins\": \"\", \"device_id\": \"DeviceId\"}";
        assertEquals(expected, jweService.decrypt(jweService.encrypt(expected, publicKey, publicKeyID, "application/json")));
    }

    @Test
    public void decryptOfKnownValuesProducesExpectedResult() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        final String jwe = "eyJraWQiOiI1ZjowNDpkZTozMzpkNjpiZjpjYzo2Yzo1NzpkYTowYTo3ZDo4MDo3ZDphNDpjZSIsImN0eSI6ImFw" +
                "cGxpY2F0aW9uL2pzb24iLCJhbGciOiJSU0EtT0FFUC0yNTYiLCJlbmMiOiJBMjU2Q0JDLUhTNTEyIn0" +
                "." +
                "izy7zcD1fbbZg93TiouYmm9rq4I9aR5wSRPuASI1NoAL_5TMJq4s0hEQHp4DWXtbT1YZoSkbUazz-Cegt57XiHMO-z5lKSizJ6s" +
                "3t9VnMcIzlLXKKDQO4Ke7wVq80zYCaYgA5oT1V83686T0l1x4UtD7ZwrVRbHUOJ1JA4j-plMYr2TdM20diFv_a-2jDh4Q4DVs4A" +
                "sCYfAqC-FBJ-6yNjGFhkUGT-CtBHWswREPtGk5naiAKEz7gFuXOrh3gTOlP5J7-XWkEpdD_c084PBNKJgY-g6meYqKDBHCkfnDD" +
                "XZxALAOHWCtcB6KaaIB6fUvmAiT9DXfoCDv-lxxW08a5aS2cvrFXmOu-aFkxRYXNswhj0JXWFLVRdQd47r1Tqjb1bTtLUlzGeJw" +
                "8FOOnFbEGSqFzZNgDCpvcCFpgXei81mXPIXdSb_LZaFWqRXB298skvSPlKzkx58UGjH72P-8zQZJKp3_C_ZYwDS_7kzxGRnN_Kv" +
                "E0Weh1iqjFEBvTjK-4zfk23KaLllkqceYx2WPS5Tx16WBJ81mVECklugwkK6lyBb3xwS7vqKSkKb2uc9a4RAGjjr4yBVRR8C9MZ" +
                "bETX91t-hInCoFHc7efaoItmh2QoWtVl-ADjNdTNXfn0EL4de5IReHeIHBJRplaFala_4Ts-DF4IPw5VWRMFXxJks" +
                "." +
                "SQO-sjQj7ZaFRXMqzWhbSg" +
                "." +
                "Z5qQUSxoKhgjOibCLUqV0gNCNR9c7PUEduWUbuCifzTMvtLFN86954YZ4tOcRwyWwmO97LFEj4tknA1CCy30FyU9APejMaAWn3n" +
                "S9REhsQiXme9xy4cKzdV_kWCaBxS-" +
                "." +
                "rmGzWJj1GjQog7Ed0HvAKeDa9FG5D8q6Sq7qT3MK_Nw";

        @SuppressWarnings("SpellCheckingInspection")
        final String privateKeyPEM =
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
                        "-----END RSA PRIVATE KEY-----\n";


        final RSAPrivateKey privateKey = JCECrypto.getRSAPrivateKeyFromPEM(provider, privateKeyPEM);
        final JWEService jweService = new Jose4jJWEService(privateKey, provider.getName());

        String expected = "{\"auth_request\": \"AuthRequest\", \"action\": \"True\", \"app_Pins\": \"\"," +
                " \"device_id\": \"DeviceId\"}";
        String actual = jweService.decrypt(jwe);
        assertEquals(expected, actual);
    }

    @Test
    public void encryptCanBeDecrypted() throws Exception {
        String expected = "{\"auth_request\": \"AuthRequest\", \"action\": \"True\"," +
                " \"app_Pins\": \"\", \"device_id\": \"DeviceId\"}";
        assertEquals(expected, jweService.decrypt(jweService.encrypt(expected, publicKey, publicKeyID, "application/json")));
    }

    @Test
    public void encryptSetsKeyId() throws Exception {
        String plainText = "{\"test\": \"response\"}";
        String encrypted = jweService.encrypt(plainText, publicKey, publicKeyID, "application/json");
        assertEquals(publicKeyID, jweService.getHeaders(encrypted).get("kid"));
    }

    @Test(expected = JWEFailure.class)
    public void decryptJoseExceptionThrowsJweFailure() throws Exception {
        //noinspection SpellCheckingInspection
        jweService.decrypt("kjsdhflskd");
    }

    @Test
    public void getHeadersReturnsHeaders() throws Exception {
        //noinspection SpellCheckingInspection
        final String jwe = "eyJraWQiOiI1ZjowNDpkZTozMzpkNjpiZjpjYzo2Yzo1NzpkYTowYTo3ZDo4MDo3ZDphNDpjZSIsImN0eSI6ImFw" +
                "cGxpY2F0aW9uL2pzb24iLCJhbGciOiJSU0EtT0FFUC0yNTYiLCJlbmMiOiJBMjU2Q0JDLUhTNTEyIn0" +
                "." +
                "izy7zcD1fbbZg93TiouYmm9rq4I9aR5wSRPuASI1NoAL_5TMJq4s0hEQHp4DWXtbT1YZoSkbUazz-Cegt57XiHMO-z5lKSizJ6s" +
                "3t9VnMcIzlLXKKDQO4Ke7wVq80zYCaYgA5oT1V83686T0l1x4UtD7ZwrVRbHUOJ1JA4j-plMYr2TdM20diFv_a-2jDh4Q4DVs4A" +
                "sCYfAqC-FBJ-6yNjGFhkUGT-CtBHWswREPtGk5naiAKEz7gFuXOrh3gTOlP5J7-XWkEpdD_c084PBNKJgY-g6meYqKDBHCkfnDD" +
                "XZxALAOHWCtcB6KaaIB6fUvmAiT9DXfoCDv-lxxW08a5aS2cvrFXmOu-aFkxRYXNswhj0JXWFLVRdQd47r1Tqjb1bTtLUlzGeJw" +
                "8FOOnFbEGSqFzZNgDCpvcCFpgXei81mXPIXdSb_LZaFWqRXB298skvSPlKzkx58UGjH72P-8zQZJKp3_C_ZYwDS_7kzxGRnN_Kv" +
                "E0Weh1iqjFEBvTjK-4zfk23KaLllkqceYx2WPS5Tx16WBJ81mVECklugwkK6lyBb3xwS7vqKSkKb2uc9a4RAGjjr4yBVRR8C9MZ" +
                "bETX91t-hInCoFHc7efaoItmh2QoWtVl-ADjNdTNXfn0EL4de5IReHeIHBJRplaFala_4Ts-DF4IPw5VWRMFXxJks" +
                "." +
                "SQO-sjQj7ZaFRXMqzWhbSg" +
                "." +
                "Z5qQUSxoKhgjOibCLUqV0gNCNR9c7PUEduWUbuCifzTMvtLFN86954YZ4tOcRwyWwmO97LFEj4tknA1CCy30FyU9APejMaAWn3n" +
                "S9REhsQiXme9xy4cKzdV_kWCaBxS-" +
                "." +
                "rmGzWJj1GjQog7Ed0HvAKeDa9FG5D8q6Sq7qT3MK_Nw";
        Map<String, Object> expected = new LinkedHashMap<>();
        expected.put("alg", "RSA-OAEP-256");
        expected.put("enc", "A256CBC-HS512");
        expected.put("kid", "5f:04:de:33:d6:bf:cc:6c:57:da:0a:7d:80:7d:a4:ce");
        expected.put("cty", "application/json");

        assertEquals(expected, jweService.getHeaders(jwe));
    }
}
