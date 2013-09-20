package com.launchkey.sdk.crypto;

import com.launchkey.sdk.Util;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Crypto {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        //Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    /**
     *
     * @return RSA cipher with OAEP padding
     * @throws Exception
     */
    public static Cipher getRSACipher() throws Exception {
        return Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
    }

    /**
     * Strips the headers from the public key
     * @param key
     * @return
     */
    public static String stripPublicKeyHeaders(String key) {
        //strip the headers from the key string
        StringBuilder strippedKey = new StringBuilder();
        String lines[] = key.split("\n");
        for(String line : lines) {
            if(!line.contains("BEGIN PUBLIC KEY") && !line.contains("END PUBLIC KEY")) {
                strippedKey.append(line.trim());
            }
        }
        return strippedKey.toString().trim();
    }

    /**
     * Turns a String representing a public key into a PublicKey object
     * @return PublicKey representation of string public key
     * @throws Exception
     */
    public static PublicKey getRSAPublicKeyFromString(String apiKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        byte[] publicKeyBytes = Util.base64Decode(apiKey.getBytes("UTF-8"));
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * Turns a String representing a private key into a PrivateKey object
     * @return PrivateKey representation of string private key
     * @throws Exception
     */
    public static PrivateKey getRSAPrivateKeyFromString(String key) throws Exception {
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Util.base64Decode(key.getBytes("UTF-8")));
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        return factory.generatePrivate(privateKeySpec);
    }

    /**
     * Decrypts a byte[] with the private key
     * @return byte[] of the decrypted message
     * @throws Exception
     **/
    public static byte[] decryptWithPrivateKey(byte[] message, String privateKey) throws Exception {
        PrivateKey pKey = getRSAPrivateKeyFromString(privateKey);
        Cipher rsaCipher = getRSACipher();
        rsaCipher.init(Cipher.DECRYPT_MODE, pKey);
        return rsaCipher.doFinal(message);
    }
}
