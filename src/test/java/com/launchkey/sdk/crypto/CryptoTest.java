package com.launchkey.sdk.crypto;

import com.launchkey.sdk.TestAbstract;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class CryptoTest extends TestAbstract {

    @Test
    public void testGetRSACipherHasCorrectAlgorithm() throws Exception {
        String expected = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
        String actual = Crypto.getRSACipher().getAlgorithm();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRSACipherHasCorrectProvider() throws Exception {
        String expected = "BC";
        String actual = Crypto.getRSACipher().getProvider().getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testStripPublicKeyHeaders() throws Exception {
        String expected = "HelloWorld";
        String actual = Crypto.stripKeyHeaders("-----BEGIN PUBLIC KEY-----\nHello\nWorld\n-----END PUBLIC KEY-----\n");
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRSAPublicKeyFromString() throws Exception {
        PublicKey actual;
            actual = Crypto.getRSAPublicKeyFromString(PUBLIC_KEY);
        assertNotNull(actual);
    }

    @Test
    public void testGetRSAPrivateKeyFromString() throws Exception {
        PrivateKey actual;
        actual = Crypto.getRSAPrivateKeyFromString(PRIVATE_KEY);
        assertNotNull(actual);
    }

    @Test
    public void testDecryptWithPrivateKey() throws Exception {
        String expected = "This is the expected unencrypted value";
        String base64encodedEncrypted =
                "Jny/38IhsWDpeFigUC0f+H4sYwlwY/8iGvrvfUNGh7rZCiiSf8oIC7Kx6WUCl/jY9S+OXmYmGKls\n" +
                        "YUn2yBYYp+5cYyO6CyKNJkhNFkWjWcbb9Q0u9pxOz8Q/2YhRvHCNZWaXtLxtmQQljoiF4m0sHGSf\n" +
                        "CUf45pCCQAU6QInN1w9S51SMRP1weTyC8WROeg8vObeMXc+DzZ4c6WCTILmjgVjB4rnQb/43EUxe\n" +
                        "RXvaj9crUPrgaXiu+yvRnhEM40Fw4B26p8t6k6Sb27SIuAOWhmusZkf+JZoWF2yU6JeMfgXbhbjk\n" +
                        "9Q6a1Yhav4vBvYouoXRfRwEsiwyZflXfXzgHqA==\n";
        String actual = new String(Crypto.decryptWithPrivateKey(BASE_64.decode(base64encodedEncrypted), PRIVATE_KEY));
        assertEquals(expected, actual);
    }

    @Test
    public void testDecryptRSAWithPrivateKey() throws Exception {
        String expected = "This is the expected unencrypted value";
        String base64encodedEncrypted =
                "Jny/38IhsWDpeFigUC0f+H4sYwlwY/8iGvrvfUNGh7rZCiiSf8oIC7Kx6WUCl/jY9S+OXmYmGKls\n" +
                        "YUn2yBYYp+5cYyO6CyKNJkhNFkWjWcbb9Q0u9pxOz8Q/2YhRvHCNZWaXtLxtmQQljoiF4m0sHGSf\n" +
                        "CUf45pCCQAU6QInN1w9S51SMRP1weTyC8WROeg8vObeMXc+DzZ4c6WCTILmjgVjB4rnQb/43EUxe\n" +
                        "RXvaj9crUPrgaXiu+yvRnhEM40Fw4B26p8t6k6Sb27SIuAOWhmusZkf+JZoWF2yU6JeMfgXbhbjk\n" +
                        "9Q6a1Yhav4vBvYouoXRfRwEsiwyZflXfXzgHqA==\n";
        String actual = new String(Crypto.decryptRSA(BASE_64.decode(base64encodedEncrypted), PRIVATE_KEY));
        assertEquals(expected, actual);
    }

    @Test
    public void testDecryptAuthTypeDataEncryptedWithPublicKeyAndBase64encodedWithNewLines() throws Exception {
        String expected = "{\"auth_request\": \"AuthRequest\", \"action\": \"True\", \"app_Pins\": \"\", \"device_id\": \"DeviceId\"}";
        String base64encodedEncrypted =
                "MW7DckKE5IXFWkUN5liJeVo27Jhaq+XSeJSHci1/Qa0dvbhr4YRybxg2DiGlWLVZdZzPr5JaOzO8\n" +
                "gBEOLJMMVtojFzttShacd6u6llw+trHaaYqL1so9QpyZ7OQJke4MP8lFx/83vi/jL4bOiMGBKaQB\n" +
                "lgl4Z/z9/z6hiEbutDdmh8lcAaCQnGbPYoH174oXvIXHdVhMD9ajNVb4gWqWlGzz/xih2hQS9DoR\n" +
                "87tsVDUtqZjnN1qgiO3nzxZw2RrBSBPnZWtpcHs24a7R/AHnL+tKrFcIDbADiMIM3+Mao73ZWSf7\n" +
                "kTXLdICAqZOuCqYZcU4xdr9Wy/R2tWKOlPm9rw==\n";
        String actual = new String(Crypto.decryptRSA(BASE_64.decode(base64encodedEncrypted), PRIVATE_KEY));
        assertEquals(expected, actual);
    }

    @Test
    public void testVerifySignatureReturnsTrueWhenSignatureValid() throws Exception {
        String base64encodedEncrypted =
                "Jny/38IhsWDpeFigUC0f+H4sYwlwY/8iGvrvfUNGh7rZCiiSf8oIC7Kx6WUCl/jY9S+OXmYmGKls\n" +
                "YUn2yBYYp+5cYyO6CyKNJkhNFkWjWcbb9Q0u9pxOz8Q/2YhRvHCNZWaXtLxtmQQljoiF4m0sHGSf\n" +
                "CUf45pCCQAU6QInN1w9S51SMRP1weTyC8WROeg8vObeMXc+DzZ4c6WCTILmjgVjB4rnQb/43EUxe\n" +
                "RXvaj9crUPrgaXiu+yvRnhEM40Fw4B26p8t6k6Sb27SIuAOWhmusZkf+JZoWF2yU6JeMfgXbhbjk\n" +
                "9Q6a1Yhav4vBvYouoXRfRwEsiwyZflXfXzgHqA==\n";
        String base64encodedSignature = "BKOVrXZJVOobOQHpmgPnUpggaYtlZBuKsNv300MTg1fykvD7K6/HKl" +
                "v27aJUOrtyPzVur+Jad5nz6JHhSrUy5dCVOyeGRnQ4nhrlvkhOcBn4/ctz2l6ZGK6bzOOR7gmUl/3Z" +
                "nAtHqaTWNlFIlhOe+JEtaMEEvc2fB5rh87ibDGUI9ZtYENoEDkaN7UUq121qZWVCg7Nj3z0+yLhEji" +
                "rNYgs8tI5CzNIySX85qRLI83EJrelMNWskqKvy/lhr5GasQMZUTEPbtjXz7AunqZRVAkRw/LAoQu2J" +
                "ZXnJiJYhtRw/bZmU94Ah6GAW3bNmvEAZns2fs+A3KdfY52DpwEWwuw==";
        boolean actual = Crypto.verifySignature(
                PUBLIC_KEY,
                BASE_64.decode(base64encodedSignature),
                BASE_64.decode(base64encodedEncrypted)
        );
        assertTrue(actual);
    }

    @Test
    public void testVerifySignatureReturnsFalseWhenSignatureInvalid() throws Exception {
        String base64encodedEncrypted =
                "Jny/38IhsWDpeFigUC0f+H4sYwlwY/8iGvrvfUNGh7rZCiiSf8oIC7Kx6WUCl/jY9S+OXmYmGKls\n" +
                "YUn2yBYYp+5cYyO6CyKNJkhNFkWjWcbb9Q0u9pxOz8Q/2YhRvHCNZWaXtLxtmQQljoiF4m0sHGSf\n" +
                "CUf45pCCQAU6QInN1w9S51SMRP1weTyC8WROeg8vObeMXc+DzZ4c6WCTILmjgVjB4rnQb/43EUxe\n" +
                "RXvaj9crUPrgaXiu+yvRnhEM40Fw4B26p8t6k6Sb27SIuAOWhmusZkf+JZoWF2yU6JeMfgXbhbjk\n" +
                "9Q6a1Yhav4vBvYouoXRfRwEsiwyZflXfXzgHqA==\n";
        String base64encodedSignature =
                "Jny/38IhsWDpeFigUC0f+H4sYwlwY/8iGvrvfUNGh7rZCiiSf8oIC7Kx6WUCl/jY9S+OXmYmGKls\n" +
                "YUn2yBYYp+5cYyO6CyKNJkhNFkWjWcbb9Q0u9pxOz8Q/2YhRvHCNZWaXtLxtmQQljoiF4m0sHGSf\n" +
                "CUf45pCCQAU6QInN1w9S51SMRP1weTyC8WROeg8vObeMXc+DzZ4c6WCTILmjgVjB4rnQb/43EUxe\n" +
                "RXvaj9crUPrgaXiu+yvRnhEM40Fw4B26p8t6k6Sb27SIuAOWhmusZkf+JZoWF2yU6JeMfgXbhbjk\n" +
                "9Q6a1Yhav4vBvYouoXRfRwEsiwyZflXfXzgHqA==\n";
        boolean actual = Crypto.verifySignature(
                PUBLIC_KEY, BASE_64.decode(base64encodedSignature),
                BASE_64.decode(base64encodedEncrypted)
        );
        assertFalse(actual);
    }

    @Test
    public void testDecryptAes() throws Exception {
        String expected = "This is the expected unencrypted value";

        String base64encodedEncrypted = "Uc7ZMWqCc6TfQU/KTdl1KHEkTIWQWSC+uuSyMU5Kg088E32HLePvHkwwxTdqzhgH";
        String actual = new String(Crypto.decryptAES(
                BASE_64.decode(base64encodedEncrypted),
                "myciphermyciphermyciphermycipher".getBytes(),
                "iviviviviviviviv".getBytes()
        ));
        assertEquals(expected, actual);
    }

    @Test
    public void testSignJSONObject() throws Exception {
        byte[] dataToSign = "{\"text\": \"This is the data to sign\"}".getBytes();
        byte[] signature = Crypto.signWithPrivateKey(dataToSign, PRIVATE_KEY);
        assertTrue("Signature did not verify", Crypto.verifySignature(PUBLIC_KEY, signature, dataToSign));
    }
}