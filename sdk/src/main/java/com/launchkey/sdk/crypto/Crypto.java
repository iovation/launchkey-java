/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
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

import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Interface for providing cryptography
 */
public interface Crypto {

    /**
     * Encrypt the provided message with the provided public key.  The message will be encrypted with the
     * RSA/ECB/OAEP with SHA1 cipher and MGF1 padding.
     *
     * @param message   Message to be encrypted
     * @param publicKey Public key with which to encrypt the message
     * @return encrypted message
     */
    byte[] encryptRSA(byte[] message, PublicKey publicKey);

    /**
     * Decrypt the provided message with the private key.  The message should be encrypted with the
     * RSA/ECB/OAEP with SHA1 cipher and MGF1 padding.
     *
     * @param message Message to decrypt
     * @return decrypted message
     */
    byte[] decryptRSA(byte[] message);

    /**
     * Sign the provided message with the private key and return the signature.  The signature will be generated with
     * SHA256 with RSA.
     *
     * @param message Message to sign
     * @return signature
     */
    byte[] sign(byte[] message);

    /**
     * Verify that the provided signature was created with the private key paired with the provided public key
     * using the provided message.  The signature must have been generated with SHA256 with RSA.
     *
     * @param signature Signature to verify
     * @param message   Message to verify against
     * @param publicKey Public key paired with the private key used tpo generate the signature
     * @return true if valid an false if not valid
     */
    boolean verifySignature(byte[] signature, byte[] message, PublicKey publicKey);

    /**
     * Decrypt AES/CBC
     *
     * @param message Message to sign
     * @param key     Key for crypto
     * @param iv      Initialization Vector (IV) for crypto
     * @return decrypted message
     * @throws GeneralSecurityException When an error occurred decrypting the message
     */
    byte[] decryptAES(byte[] message, byte[] key, byte[] iv) throws GeneralSecurityException;

    /**
     * Get a public key from the provided PEM formatted string
     *
     * @param publicKey PEM formatted public key string
     * @return RSA Public Key object
     */
    RSAPublicKey getRSAPublicKeyFromPEM(String publicKey);
}
