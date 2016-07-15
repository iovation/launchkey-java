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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;

/**
 * Crypto provider utilizing the Java Cryptography Extension
 */
public class JCECrypto implements Crypto {

    private static final String RSA_CRYPTO_CIPHER = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    public static final String RSA_SIGNING_ALGO = "SHA256withRSA";
    private static final String AES_CRYPTO_CIPHER = "AES/CBC/NoPadding";

    private final Provider provider;
    private final PrivateKey privateKey;
    private static final Base64 BASE_64 = new Base64(0);

    /**
     * @param privateKey Private Key
     * @param provider   Crypto Provider
     */
    public JCECrypto(PrivateKey privateKey, Provider provider) {
        this.provider = provider;
        this.privateKey = privateKey;

        // Test out the provider and key
        try {
            Cipher rsaDecryptCipher = Cipher.getInstance(RSA_CRYPTO_CIPHER, provider);
            rsaDecryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            getSha256withRSA();
            Cipher.getInstance(AES_CRYPTO_CIPHER, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Provider does not provide required cipher: RSA/ECB/OAEPWithSHA1", e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("Provider does not provide required padding: MGF1", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid private key provided", e);
        }
    }

    @Override
    public byte[] encryptRSA(byte[] message, PublicKey publicKey) {
        return processRSA(message, publicKey, Cipher.ENCRYPT_MODE);
    }


    @Override
    public byte[] decryptRSA(byte[] message) {
        return processRSA(message, privateKey, Cipher.DECRYPT_MODE);
    }

    @Override
    public byte[] sign(byte[] message) {
        try {
            Signature signature = getSha256withRSA();
            signature.initSign(privateKey);
            signature.update(message);
            return signature.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm SHA256withRSA is not available", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("publicKey is not a valid RSA public key", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("An error occurred processing the signature", e);
        }
    }

    @Override
    public boolean verifySignature(byte[] signature, byte[] message, PublicKey publicKey) {
        try {
            Signature sig = getSha256withRSA();
            sig.initVerify(publicKey);
            sig.update(message);
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Algorithm SHA256withRSA is not available", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("publicKey is not a valid RSA public key", e);
        } catch (SignatureException e) {
            throw new IllegalArgumentException("An error occurred processing the signature", e);
        }
    }


    @Override
    public byte[] decryptAES(byte[] message, byte[] key, byte[] iv) throws GeneralSecurityException {
        return processAES(Cipher.DECRYPT_MODE, message, key, iv);
    }

    @Override
    public RSAPublicKey getRSAPublicKeyFromPEM(String publicKey) {
        return getRSAPublicKeyFromPEM(provider, publicKey);
    }

    @Override public byte[] sha256(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256", this.provider);
        byte[] response = digest.digest(input);
        return response;
    }

    /**
     * Get an RSA private key utilizing the provided provider and PEM formatted string
     *
     * @param provider Provider to generate the key
     * @param pem      PEM formatted key string
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
     * @param pem      PEM formatted key string
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

    private Signature getSha256withRSA() throws NoSuchAlgorithmException {
        return Signature.getInstance(RSA_SIGNING_ALGO, provider);
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

    private byte[] processAES(int mode, byte[] message, byte[] key, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(AES_CRYPTO_CIPHER, provider);
        cipher.init(mode, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        byte[] out = cipher.doFinal(message);
        return new String(out).trim().getBytes();
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
}
