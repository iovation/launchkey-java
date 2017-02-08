package com.launchkey.sdk.crypto;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JCECryptoTest {
    @SuppressWarnings("SpellCheckingInspection")
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


    private static final String PRIVATE_KEY_CARRIAGE_RETURN = PRIVATE_KEY.replace('\n', '\r');
    private static final String PRIVATE_KEY_CARRIAGE_RETURN_NEWLINE = PRIVATE_KEY.replace("\n", "\r\n");

    @SuppressWarnings("SpellCheckingInspection")
    private static final String PUBLIC_KEY =
            ("-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq2izh7NEarDdrdZLrpli\n" +
                    "zezZG/JzW14XQ74IXkjEqkvxhZ1s6joGtvoxX+P0QRyWrhgtiUnN3DSRAa0QLsZ0\n" +
                    "ZKk5a2AMGqu0/6eoJGwXSHcLreLEfqdd8+zlvDrbWISekTLoecLttwKSIcP2bcq1\n" +
                    "nAKz+ZNKMPvB/lm/dHXOqlnybo0J7efUkbd81fHrMOZNLKRYXx3Zx8zsQFf2ee/y\n" +
                    "pnnw5lKwX+9IBAT/679eGUlh8HfTSG6JQaNezyRG1cOd+pO6hKxff6Q2GVXqHsrI\n" +
                    "ac4AlR80AEaBeiuFYxjHpruS6BRcyW8UvqX0l9rKMDAWNAtMWt2egYAe6XOEXIWO\n" +
                    "iQIDAQAB\n" +
                    "-----END PUBLIC KEY-----\n");

    private static final String PUBLIC_KEY_FINGERPRINT = "39:bc:93:66:34:af:e5:15:b4:a1:33:58:81:dc:68:2c";

    private static final String PUBLIC_KEY_CARRIAGE_RETURN = PUBLIC_KEY.replace('\n', '\r');
    private static final String PUBLIC_KEY_CARRIAGE_RETURN_NEWLINE = PUBLIC_KEY.replace("\n", "\r\n");

    private Base64 base64;
    private BouncyCastleProvider provider;
    private JCECrypto crypto;
    private RSAPublicKey rsaPublicKey;
    private RSAPrivateKey rsaPrivateKey;

    @Before
    public void setUp() throws Exception {
        base64 = new Base64(0);
        provider = new BouncyCastleProvider();
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
                "Jny/38IhsWDpeFigUC0f+H4sYwlwY/8iGvrvfUNGh7rZCiiSf8oIC7Kx6WUCl/jY9S+OXmYmGKls\n" +
                        "YUn2yBYYp+5cYyO6CyKNJkhNFkWjWcbb9Q0u9pxOz8Q/2YhRvHCNZWaXtLxtmQQljoiF4m0sHGSf\n" +
                        "CUf45pCCQAU6QInN1w9S51SMRP1weTyC8WROeg8vObeMXc+DzZ4c6WCTILmjgVjB4rnQb/43EUxe\n" +
                        "RXvaj9crUPrgaXiu+yvRnhEM40Fw4B26p8t6k6Sb27SIuAOWhmusZkf+JZoWF2yU6JeMfgXbhbjk\n" +
                        "9Q6a1Yhav4vBvYouoXRfRwEsiwyZflXfXzgHqA==\n";
        String actual = new String(crypto.decryptRSA(base64.decode(base64encodedEncrypted), rsaPrivateKey));
        assertEquals(expected, actual);
    }

    @Test
    public void testEncryptRSA() throws Exception {
        String expected = "This is the expected unencrypted value";
        String actual = new String(crypto.decryptRSA(crypto.encryptRSA(expected.getBytes(), rsaPublicKey), rsaPrivateKey));
        assertEquals(expected, actual);
    }

    @Test
    public void testDecryptAuthTypeDataWithNewLines() throws Exception {
        String expected = "{\"auth_request\": \"AuthRequest\", \"action\": \"True\", \"app_Pins\": \"\", \"device_id\": \"DeviceId\"}";
        @SuppressWarnings("SpellCheckingInspection")
        String base64encodedEncrypted =
                "MW7DckKE5IXFWkUN5liJeVo27Jhaq+XSeJSHci1/Qa0dvbhr4YRybxg2DiGlWLVZdZzPr5JaOzO8\n" +
                        "gBEOLJMMVtojFzttShacd6u6llw+trHaaYqL1so9QpyZ7OQJke4MP8lFx/83vi/jL4bOiMGBKaQB\n" +
                        "lgl4Z/z9/z6hiEbutDdmh8lcAaCQnGbPYoH174oXvIXHdVhMD9ajNVb4gWqWlGzz/xih2hQS9DoR\n" +
                        "87tsVDUtqZjnN1qgiO3nzxZw2RrBSBPnZWtpcHs24a7R/AHnL+tKrFcIDbADiMIM3+Mao73ZWSf7\n" +
                        "kTXLdICAqZOuCqYZcU4xdr9Wy/R2tWKOlPm9rw==\n";
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
        String expected = "e806a291cfc3e61f83b98d344ee57e3e8933cccece4fb45e1481f1f560e70eb1";
        String actual = Hex.toHexString(crypto.sha256("Testing".getBytes()));
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
}
