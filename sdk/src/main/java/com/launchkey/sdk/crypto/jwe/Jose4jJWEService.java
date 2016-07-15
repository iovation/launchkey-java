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

import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.service.ping.PingService;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.security.interfaces.RSAPrivateKey;

public class Jose4jJWEService implements JWEService {
    private final RSAPrivateKey privateKey;
    private final PingService pingService;

    /**
     * @param privateKey  RSA Private Key of the RSA public/private key pair that will be used to decrypt the
     *                    Content Encryption Key (CEK) when decrypting.
     * @param pingService Ping service to obtain the the RSA Public Key of the RSA public/private key pair of the
     *                    Platform API which will be used to encrypt the Content Encryption Key (CEK) when encrypting.
     */
    public Jose4jJWEService(RSAPrivateKey privateKey, PingService pingService) {
        this.privateKey = privateKey;
        this.pingService = pingService;
    }

    @Override public String decrypt(String data) throws JWEFailure {
        String decrypted;
        JsonWebEncryption jwe = new JsonWebEncryption();
        jwe.setKey(privateKey);
        try {
            jwe.setCompactSerialization(data);
            decrypted = jwe.getPlaintextString();
        } catch (JoseException e) {
            throw new JWEFailure("An error occurred attempting to decrypt a JWE", e);
        }
        return decrypted;
    }

    @Override public String encrypt(String data) throws JWEFailure {
        String encrypted;
        JsonWebEncryption jwe = new JsonWebEncryption();
        try {
            Key publicKey = pingService.getPublicKey();
            jwe.setKey(publicKey);
            jwe.setPlaintext(data);
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
            encrypted = jwe.getCompactSerialization();
        } catch (JoseException e) {
            throw new JWEFailure("An error occurred attempting to decrypt a JWE", e);
        } catch (BaseException e) {
            throw new JWEFailure("An error occurred which attempting to retrieve the Platform Public Key", e);
        }
        return encrypted;
    }
}
