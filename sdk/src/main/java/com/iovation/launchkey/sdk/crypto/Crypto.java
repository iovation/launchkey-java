/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Interface for providing cryptography
 */
public interface Crypto {

    /**
     * Encrypt the provided message with the provided public key.  The message will be encrypted with the
     * RSA/ECB/OAEP with SHA1 cipher and MGF1 padding.
     *
     * @param message Message to be encrypted
     * @param publicKey Public key with which to encrypt the message
     * @return encrypted message
     */
    byte[] encryptRSA(byte[] message, PublicKey publicKey);

    /**
     * Decrypt the provided message with the private key.  The message should be encrypted with the
     * RSA/ECB/OAEP with SHA1 cipher and MGF1 padding.
     *
     * @param message Message to decrypt
     * @param privateKey Private key with which to encrypt the message
     * @return decrypted message
     */
    byte[] decryptRSA(byte[] message, PrivateKey privateKey);

    /**
     * Get a public key from the provided PEM formatted string
     *
     * @param publicKey PEM formatted public key string
     * @return RSA Public Key object
     */
    RSAPublicKey getRSAPublicKeyFromPEM(String publicKey);

    /**
     * Generate a 256 bit hash with the Secure Hash Algorithm
     * @param input Data to hash
     * @return Hashed data
     * @throws NoSuchAlgorithmException When the provider does not support SHA-256
     */
    byte[] sha256(byte[] input) throws NoSuchAlgorithmException;

    /**
     * Generate a 384 bit hash with the Secure Hash Algorithm
     * @param input Data to hash
     * @return Hashed data
     * @throws NoSuchAlgorithmException When the provider does not support SHA-256
     */
    byte[] sha384(byte[] input) throws NoSuchAlgorithmException;

    /**
     * Generate a 512 bit hash with the Secure Hash Algorithm
     * @param input Data to hash
     * @return Hashed data
     * @throws NoSuchAlgorithmException When the provider does not support SHA-256
     */
    byte[] sha512(byte[] input) throws NoSuchAlgorithmException;

    String getRsaPublicKeyFingerprint(RSAPublicKey key) throws IllegalArgumentException;

    String getRsaPublicKeyFingerprint(RSAPrivateKey key) throws IllegalArgumentException;
}
