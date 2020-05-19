package com.iovation.launchkey.sdk.crypto;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JCECryptoTest {
    @SuppressWarnings("SpellCheckingInspection")
    private static final String PRIVATE_KEY =
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


    private static final String PRIVATE_KEY_CARRIAGE_RETURN = PRIVATE_KEY.replace('\n', '\r');
    private static final String PRIVATE_KEY_CARRIAGE_RETURN_NEWLINE = PRIVATE_KEY.replace("\n", "\r\n");

    @SuppressWarnings("SpellCheckingInspection")
    private static final String PUBLIC_KEY =
            "-----BEGIN PUBLIC KEY-----\n" +
                    "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAmeLKtbsuwBl5AdLQ+Axp\n" +
                    "PHPqCQZpdU5E6MX9kvpRx9JFEyDk4UisRHoRgT2/rU8rcJBlO5Fv5rWvHNl8zAM8\n" +
                    "fGXNdCY3OS1qY+G6ECzkWY0s+babIXx033zOpbDCqv0+ClpOkDQINQEFjZ6WN6M+\n" +
                    "9pEsXKfPqkssukJwS47/vCPHjAn7eDs7umYI5Gik6EZO8ron8synoczGk7OYJ+XB\n" +
                    "qgNL0PeT5Xv9YcM38l0l8tXYUU8TbhQzmIW3TsX8RNr93QeoV6vmj9onEvVgJFAC\n" +
                    "qI7y8JSG9ERYHbXZd5f5ncoEMuzZyFQhStNZIX7hI/symJDI0K8xUKVdykeBLjf4\n" +
                    "cr7S5GPnCcrYmNcKIpXgCjbcR30A2r57enCWySx3+14CeYEdxm0dd23LZLAcFJLT\n" +
                    "vkx+e9m6Wa4Pa7TjOkbHce04aZNIYwYHOlhOTBYcArruUz0IWBCCIaEqizWGgscH\n" +
                    "/++Yqe8rUV8fLpw0FnKAZQkZCVIAvZLOuYr9BoKcisGyZMf/cffzCFhC1MMuWEzt\n" +
                    "vJeqYPPD20dKXc38JO6yc+jyAX/kiJBCSSoUsjAUA83OtsApMXvao1/wu9vpv7qB\n" +
                    "2IGklKn1RFT10QqrEYxifLALI9cv4703jZTp2z2o1GzoObFwccukF8UfpenbIRKT\n" +
                    "p+rUKU1uSXt2jsxjvDiCo3MCAwEAAQ==\n" +
                    "-----END PUBLIC KEY-----\n";

    private static final String PUBLIC_KEY_FINGERPRINT = "5f:04:de:33:d6:bf:cc:6c:57:da:0a:7d:80:7d:a4:ce";

    private static final String PUBLIC_KEY_CARRIAGE_RETURN = PUBLIC_KEY.replace('\n', '\r');
    private static final String PUBLIC_KEY_CARRIAGE_RETURN_NEWLINE = PUBLIC_KEY.replace("\n", "\r\n");

    private Base64 base64;
    private BouncyCastleFipsProvider provider;
    private JCECrypto crypto;
    private RSAPublicKey rsaPublicKey;
    private RSAPrivateKey rsaPrivateKey;

    @Before
    public void setUp() throws Exception {
        base64 = new Base64(0);
        provider = new BouncyCastleFipsProvider();
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", provider);
        PemObject pem = new PemReader(new StringReader(PRIVATE_KEY)).readPemObject();
        rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(pem.getContent()));
        pem = new PemReader(new StringReader(PUBLIC_KEY)).readPemObject();
        rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(pem.getContent()));

        crypto = new JCECrypto(provider);
    }

    @After
    public void tearDown() throws Exception {
        base64 = null;
        provider = null;
        crypto = null;
        rsaPublicKey = null;
        rsaPrivateKey = null;
        rsaPublicKey = null;
    }

    @Test
    public void testDecryptRSA() throws Exception {
        String expected = "This is the expected unencrypted value";
        @SuppressWarnings("SpellCheckingInspection")
        String base64encodedEncrypted =
                "cADKKaB+W63omDsisrdZFuC/mkZZSyYRQfAUiS6jXS1O2pXqVxhVnZdYIgry7RcSEAGJOGSAOn2ws\n" +
                        "8NERQLER3TTAjlCIH9qDas2bMGrftqeI/WHTvvaH/Gb9gILSfpQ/MVpdPdzzCZtBWnT3/jlA/ZVyTgA\n" +
                        "vin1s/CSgQfbuKljIeD2MWZdU9Nyp8o+PJUTf8gs2NMAV+Ps4pffxsJRvmJ4IQWEfRpBO3RSvNaDGsn\n" +
                        "meMXOj2TSMa5/3IezUAjuXOCMx6tu3uPfsdOkITMidxfdOAfO5uip2A1P7Uns2ljTFKtrhgbXzmAT/9\n" +
                        "OiI7gSYCNLAG7awSUACQozVC6mc99/Pr9i3V3pe5kc58irU3jIczhJplAsLaoypsfsltVC+RJURtKoh\n" +
                        "Avc2E1pmjPr2+idcysYO5HjO2XSWPxdCbgQQw2uwEBvRrEnN751wXa367BnKFmUXCRo5MF8BPKKGm+a\n" +
                        "ebccFMllrpAYvibdgVxtFMNOjau9SxvC0uM1n4QS9AyW1vmtzwpgFcVRg79SnLU4++pBMqJ6g5vXHFi\n" +
                        "vUuLcCKY66rG3cNFzjfzgw7VCakHTBr0wjniIjVemSo7MIhjir5az+zemN68pynuDpEf/vDEbjhXOu1\n" +
                        "RHqh80cnExVivaRXeR7MjxEQ3Kca1C38FSmTB48M2H/zIQPcsZVhw=\n";
        String actual = new String(crypto.decryptRSA(base64.decode(base64encodedEncrypted), rsaPrivateKey));
        assertEquals(expected, actual);
    }

    @Test
    public void testEncryptRSA() throws Exception {
        String expected = "This is the expected unencrypted value";
        String actual =
                new String(crypto.decryptRSA(crypto.encryptRSA(expected.getBytes(), rsaPublicKey), rsaPrivateKey));
        assertEquals(expected, actual);
    }

    @Test
    public void testDecryptAuthTypeDataWithNewLines() throws Exception {
        String expected = "{\"auth_request\": \"AuthRequest\", \"action\": \"True\"," +
                " \"app_Pins\": \"\", \"device_id\": \"DeviceId\"}";
        @SuppressWarnings("SpellCheckingInspection")
        String base64encodedEncrypted =
                "R4tPK4PnHE6gVZk3T3EvtuXZhJUZ6QkRwJNki/5cbfSGXHCIJPdFm/erQLb81LX/NP4fakqZGhjOXNc\n" +
                        "IBrqkCf7JnVEA/cnVfg2zlL9LEMK/IqPvYf+GJSkT3KL0YT15FEDJiCZxXsI1slyZoPkNc+K/a3lONnguQ071+SO\n" +
                        "K22BdMy1jYqXL8CS6HHE1+AO7w8hV++5fO4wF6N0ymM16NgdM9SWFPLZBKDtwHV5yWhdAs2xsH9G1w4y4MuX951O\n" +
                        "3V8VUU5sWsei/o0ikakSHgXWbzew5VKfrogbJNFkC4xkae2y7O3yZ7Ltzv9knjaJ2gJakuUe84uhNxU0qq+oBY7m\n" +
                        "cnP8dDGp78ICoR1Io09wJidpdX2RRcKYKw5BNUK2guKEGWq3O8Kpz/XPFdca5CD7At0uDNaOdBIWnO0Ilfil6pKQ\n" +
                        "bAppAhmePxVsGAJc+W2tNbCrKeZxQXSVbLFVhJ+DVhFrbta0UF059fNj8KMyT7fkWLjooJru2zqhfk0dBkINJVbI\n" +
                        "tfu0hvjsGKNtBr+SjujpofhYNPhQd8BvUcwbfO7G8MP8voNP7YoP6y8dLePGRwqmoG9aQuahh+UfhtsurHbBXqSo\n" +
                        "+hXcZUw0E0mReFyqjU6PTUDX4GJETS2gm/OS91Sqvd9Mccd0zqBRW9z20j1sUmHBtBsDF/ljBEvY=";
        String actual = new String(crypto.decryptRSA(base64.decode(base64encodedEncrypted), rsaPrivateKey));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRSAPublicKeyFromPEM() throws Exception {
        RSAPublicKey actual = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY);
        assertNotNull(actual);
    }

    @Test
    public void testGetRSAPublicKeyFromPEMCarriageReturn() throws Exception {
        RSAPublicKey actual = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY_CARRIAGE_RETURN);
        assertNotNull(actual);
    }

    @Test
    public void testGetRSAPublicKeyFromPEMCarriageReturnNewline() throws Exception {
        RSAPublicKey actual = JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY_CARRIAGE_RETURN_NEWLINE);
        assertNotNull(actual);
    }

    @Test
    public void testGetRSAPrivateKeyFromPEM() throws Exception {
        RSAPrivateKey actual = JCECrypto.getRSAPrivateKeyFromPEM(provider, PRIVATE_KEY);
        assertNotNull(actual);
    }

    @Test
    public void testGetRSAPrivateKeyFromPEMCarriageReturn() throws Exception {
        RSAPrivateKey actual = JCECrypto.getRSAPrivateKeyFromPEM(provider, PRIVATE_KEY_CARRIAGE_RETURN);
        assertNotNull(actual);
    }

    @Test
    public void testGetRSAPrivateKeyFromPEMCarriageReturnNewline() throws Exception {
        RSAPrivateKey actual = JCECrypto.getRSAPrivateKeyFromPEM(provider, PRIVATE_KEY_CARRIAGE_RETURN_NEWLINE);
        assertNotNull(actual);
    }

    @Test
    public void testSha256() throws Exception {
        //noinspection SpellCheckingInspection
        String expected = "e806a291cfc3e61f83b98d344ee57e3e8933cccece4fb45e1481f1f560e70eb1";
        String actual = Hex.toHexString(crypto.sha256("Testing".getBytes()));
        assertEquals(expected, actual);
    }

    @Test
    public void testSha384() throws Exception {
        //noinspection SpellCheckingInspection
        String expected = "2ca8b7b913d970a884fdb61daf74f6b4f868bc2ac20ea75" +
                "83009259f382b14a04be97ea64ba0bab703ca7ea75a932bd5";
        String actual = Hex.toHexString(crypto.sha384("Testing".getBytes()));
        assertEquals(expected, actual);
    }

    @Test
    public void testSha512() throws Exception {
        //noinspection SpellCheckingInspection
        String expected = "64f02697ccd1c0ae741d9e226f957127da7a614d6a18f55f9f2726d2027faac1" +
                "e95e619dac5417eb4898fd6a9fb8aeb9cdd005e913c80e57454cae4b6fc6e5d6";
        String actual = Hex.toHexString(crypto.sha512("Testing".getBytes()));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRsaPublicKeyFingerprintWithPrivateKeyReturnsProperFingerprint() throws Exception {
        assertEquals(PUBLIC_KEY_FINGERPRINT, crypto.getRsaPublicKeyFingerprint(rsaPrivateKey));
    }

    @Test
    public void testGetRsaPublicKeyFingerprintWithPublicKeyReturnsProperFingerprint() throws Exception {
        assertEquals(PUBLIC_KEY_FINGERPRINT, crypto.getRsaPublicKeyFingerprint(rsaPublicKey));
    }

    @Test
    public void testGetPEMFromPublicKey() throws Exception {
        assertEquals(PUBLIC_KEY,
                JCECrypto.getPEMFromRSAPublicKey(JCECrypto.getRSAPublicKeyFromPEM(provider, PUBLIC_KEY)));
    }

    @Test
    public void testGetPEMFromPubicKey1024Key() throws Exception {
        //noinspection SpellCheckingInspection
        final String pem = "-----BEGIN PUBLIC KEY-----\n" +
                "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANd/rO3/EY0rGeT4Sib6qD6AS26SHDUt\n" +
                "sN9nM11H1ajurrZz4ZKCKPG1jdmqvo/tGXvt5mQyvR9WJCg6+uokSfMCAwEAAQ==\n" +
                "-----END PUBLIC KEY-----\n";
        assertEquals(pem,
                JCECrypto.getPEMFromRSAPublicKey(JCECrypto.getRSAPublicKeyFromPEM(provider, pem)));
    }

    @Test
    public void testGetPEMFromPrivateKey() throws Exception {
        assertEquals(PRIVATE_KEY,
                JCECrypto.getPEMFromRSAPrivateKey(JCECrypto.getRSAPrivateKeyFromPEM(provider, PRIVATE_KEY)));
    }
}
