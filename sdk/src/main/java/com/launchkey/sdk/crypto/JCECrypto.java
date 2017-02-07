/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.crypto;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

/**
 * Crypto provider utilizing the Java Cryptography Extension
 */
public class JCECrypto implements Crypto {

    private static final String RSA_CRYPTO_CIPHER = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    private static final Base64 BASE_64 = new Base64(0);

    private final Provider provider;

    /**
     * @param provider Crypto Provider
     */
    public JCECrypto(Provider provider) {
        this.provider = provider;
    }

    @Override
    public byte[] encryptRSA(byte[] message, PublicKey publicKey) {
        return processRSA(message, publicKey, Cipher.ENCRYPT_MODE);
    }

    @Override
    public byte[] decryptRSA(byte[] message, PrivateKey privateKey) {
        return processRSA(message, privateKey, Cipher.DECRYPT_MODE);
    }

    @Override
    public RSAPublicKey getRSAPublicKeyFromPEM(String publicKey) {
        return getRSAPublicKeyFromPEM(provider, publicKey);
    }

    @Override
    public byte[] sha256(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256", provider);
        byte[] response = digest.digest(input);
        return response;
    }

    @Override
    public String getRsaPublicKeyFingerprint(RSAPublicKey key) throws IllegalArgumentException {
        return this.getRsaPublicKeyFingerprint(provider, key);
    }

    @Override
    public String getRsaPublicKeyFingerprint(RSAPrivateKey key) throws IllegalArgumentException {
        return this.getRsaPublicKeyFingerprint(provider, key);
    }

    /**
     * Get an RSA private key utilizing the provided provider and PEM formatted string
     *
     * @param provider Provider to generate the key
     * @param pem PEM formatted key string
     * @return RSA private key
     */
    public static RSAPrivateKey getRSAPrivateKeyFromPEM(Provider provider, String pem) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", provider);
            return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(getKeyBytesFromPEM(pem)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm SHA256withRSA is not available", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid PEM provided", e);
        }
    }

    /**
     * Get an RSA public key utilizing the provided provider and PEM formatted string
     *
     * @param provider Provider to generate the key
     * @param pem PEM formatted key string
     * @return RSA public key
     */
    public static RSAPublicKey getRSAPublicKeyFromPEM(Provider provider, String pem) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", provider);
            return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(getKeyBytesFromPEM(pem)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm SHA256withRSA is not available", e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid PEM provided", e);
        }
    }

    public static String getRsaPublicKeyFingerprint(Provider provider, RSAPrivateKey key) throws IllegalArgumentException {
        try {
            RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(
                    key.getModulus(),
                    ((RSAPrivateCrtKey) key).getPublicExponent()
            );

            KeyFactory keyFactory = KeyFactory.getInstance("RSA", provider);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
            return getRsaPublicKeyFingerprint(provider, publicKey);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Invalid key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Provider does not provide required cipher: RSA/ECB/OAEPWithSHA1", e);
        }
    }

    public static String getRsaPublicKeyFingerprint(Provider provider, RSAPublicKey key) throws IllegalArgumentException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            StringBuilder buf = new StringBuilder();
            char[] hex = Hex.encodeHex(md5.digest(key.getEncoded()));
            for (int i = 0; i < hex.length; i += 2) {
                if (buf.length() > 0) buf.append(':');
                buf.append(hex, i, 2);
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Cannot generate fingerprint as MD5 is not available.", e);
        }
    }

    private static byte[] getKeyBytesFromPEM(String pem) {
        StringBuilder strippedKey = new StringBuilder(pem.length());
        Scanner scanner = new Scanner(pem);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.matches(".*(BEGIN|END) (RSA )?(PUBLIC|PRIVATE) KEY.*")) {
                strippedKey.append(line.trim());
            }
        }
        return BASE_64.decode(strippedKey.toString().getBytes());
    }

    private byte[] processRSA(byte[] message, Key key, int mode) {
        try {
            Cipher rsaDecryptCipher = Cipher.getInstance(RSA_CRYPTO_CIPHER, provider);
            rsaDecryptCipher.init(mode, key);
            return rsaDecryptCipher.doFinal(message);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException("Block size of message was not valid", e);
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException("Padding for message was bad", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Provider does not provide required cipher: RSA/ECB/OAEPWithSHA1", e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("Provider does not provide required padding: MGF1", e);
        }
    }
}
