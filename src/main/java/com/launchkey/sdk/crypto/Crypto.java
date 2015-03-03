package com.launchkey.sdk.crypto;

import com.launchkey.sdk.Util;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.*;
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
     * Strips the headers from the key string
     * @param key
     * @return
     */
    public static String stripKeyHeaders(String key) {
        //strip the headers from the key string
        StringBuilder strippedKey = new StringBuilder();
        String lines[] = key.split("\n");
        for(String line : lines) {
            if(!line.matches(".*(BEGIN|END) (RSA )?(PUBLIC|PRIVATE) KEY.*")) {
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
        String strippedKey = stripKeyHeaders(apiKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
        byte[] publicKeyBytes = Util.base64Decode(strippedKey.getBytes("UTF-8"));
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * Turns a String representing a private key into a PrivateKey object
     * @return PrivateKey representation of string private key
     * @throws Exception
     */
    public static PrivateKey getRSAPrivateKeyFromString(String key) throws Exception {
        String strippedKey = stripKeyHeaders(key);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Util.base64Decode(strippedKey.getBytes("UTF-8")));
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        return factory.generatePrivate(privateKeySpec);
    }

    /**
     * Decrypts a byte[] using RSA with the private key
     * @return byte[] of the decrypted message
     * @throws Exception
     * @deprecated Deprecated as of SDK 1.2.0, replace by {@link #decryptRSA(byte[], String)}
     **/
    @Deprecated
    public static byte[] decryptWithPrivateKey(byte[] message, String privateKey) throws Exception {
        return decryptRSA(message, privateKey);
    }

    /**
     * Signs with the private key
     *
     * @return signature on the bytes[]
     * @throws Exception
     */
    public static byte[] signWithPrivateKey(byte[] bytes, String privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initSign(Crypto.getRSAPrivateKeyFromString(privateKey));
        signature.update(bytes);
        return signature.sign();
    }

    /**
     * Verify
     * @param publicKeyIn
     * @param signatureIn
     * @param data
     * @return
     * @throws Exception
     */
    public static boolean verifySignature(String publicKeyIn, byte[] signatureIn, byte[] data) throws Exception {
        PublicKey publicKey = getRSAPublicKeyFromString(publicKeyIn);
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(signatureIn);
    }

    /**
     * Encrypts a byte[] using RSA with the public key
     *
     * @return padded byte[]
     * @throws Exception
     */
    public static byte[] encryptRSA(byte[] message, String publicKey) throws Exception {
        String strippedKey = Crypto.stripKeyHeaders(publicKey);
        byte[] paddedMessage = pad16Bytes(message);
        PublicKey apiPublicKey =
                Crypto.getRSAPublicKeyFromString(strippedKey);
        Cipher rsaCipher = Crypto.getRSACipher();
        rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey);
        return rsaCipher.doFinal(paddedMessage);
    }

    /**
     * Decrypts a byte[] using RSA with the private key
     * @return byte[] of the decrypted message
     * @throws Exception
     **/
    public static byte[] decryptRSA(byte[] message, String privateKey) throws Exception {
        PrivateKey pKey = getRSAPrivateKeyFromString(privateKey);
        Cipher rsaCipher = getRSACipher();
        rsaCipher.init(Cipher.DECRYPT_MODE, pKey);
        return rsaCipher.doFinal(message);
    }

    /**
     * Decrypts a byte[] using AES with the provided key and IV
     * @param message
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] decryptAES(byte[] message, byte[] key, byte[] iv) throws Exception {
        return processAES(false, message, key, iv);
    }

    /**
     * Process AES encryption/decryption on the byte[] message with the provided key and IV
     * @param encrypt True for encryption and false for decryption
     * @param message
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    private static byte[] processAES(boolean encrypt, byte[] message, byte[] key, byte[] iv) throws Exception {
        CBCBlockCipher aesCipher = new CBCBlockCipher(new AESEngine());
        CipherParameters cipherParameters = new ParametersWithIV(new KeyParameter(key), iv);
        aesCipher.init(encrypt, cipherParameters);

        ByteBuffer out = ByteBuffer.allocate(message.length);
        byte[] buffer = new byte[aesCipher.getBlockSize()];
        int offset = 0;
        while (offset < message.length) {
            offset += aesCipher.processBlock(message, offset, buffer, 0);
            out.put(buffer);
        }
        return new String(out.array()).trim().getBytes();
    }

    /**
     * Pad a byte[] into multiples of 16 bytes
     *
     * @return padded byte[]
     * @throws Exception
     */
    private static byte[] pad16Bytes(byte[] bytes) throws Exception {
        if(bytes.length % 16 != 0) {
            int bytesToPad = 16 - (bytes.length % 16);
            byte[] paddedBytes = new byte[bytes.length + bytesToPad];
            System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);
            for(int i = bytes.length; i < paddedBytes.length; i++) {
                paddedBytes[i] = " ".getBytes()[0];
            }
            return paddedBytes;
        }
        return bytes;
    }
}
