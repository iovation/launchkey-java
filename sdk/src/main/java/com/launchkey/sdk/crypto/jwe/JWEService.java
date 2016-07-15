/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.crypto.jwe;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * A service for encrypting and decrypting data with the JSON Web Encryption standard.
 * @see <a href="https://tools.ietf.org/html/rfc7516">JSON Web Encryption RFC</a>
 */
public interface JWEService {
    /**
     * Decrypt the provided JWE data
     * @param data JWE data
     * @return Decrypted value
     * @throws JWEFailure When an issue arises while attempting to decrypt the data
     */
    String decrypt(String data) throws JWEFailure;

    /**
     * Encrypt the data and serialize compact serialization
     * @param data Data to encrypt
     * @return Compact serialized JWE
     * @throws JWEFailure When an issue arises while attempting to encrypt the data
     */
    String encrypt(String data) throws JWEFailure;
}
