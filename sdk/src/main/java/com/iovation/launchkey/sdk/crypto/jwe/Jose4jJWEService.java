/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.crypto.jwe;

import com.iovation.launchkey.sdk.crypto.Jose4jService;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.LinkedHashMap;
import java.util.Map;

public class Jose4jJWEService extends Jose4jService implements JWEService {
    private final RSAPrivateKey privateKey;
    private final Map<String, RSAPrivateKey> privateKeys;
    private final String provider;

    /**
     * @deprecated Please use @{@link #Jose4jJWEService(Map, String)}
     * @param privateKey  RSA Private Key of the RSA public/private key pair that will be used to decrypt the
     *                    Content Encryption Key (CEK) when decrypting.
     */
    @Deprecated
    public Jose4jJWEService(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
        this.privateKeys = null;
        this.provider = null;
    }

    /**
     * * @deprecated Please use @{@link #Jose4jJWEService(Map, String)}
     * @param privateKey  RSA Private Key of the RSA public/private key pair that will be used to decrypt the
     *                    Content Encryption Key (CEK) when decrypting.
     * @param jceProvider Name of the JCE provider to use for encryption/decryption
     */
    @Deprecated
    public Jose4jJWEService(RSAPrivateKey privateKey, String jceProvider) {
        this.privateKey = privateKey;
        this.privateKeys = null;
        this.provider = jceProvider;
    }

    /**
     * @param privateKeys Mapped list of RSA Private Key by key ID of the RSA public/private key pairs that will be
     * used to generate digital encoding and decoding as well as obtaining the RSA Public Key of the RSA public/private
     * key pair of the Platform API which will be used to verify digital signatures when decoding.
     * @param jceProvider Name of the JCE provider to use for encryption/decryption
     */
    public Jose4jJWEService(
        Map<String, RSAPrivateKey> privateKeys,
        String jceProvider
    ) {
        this.privateKey = null;
        this.privateKeys = privateKeys;
        this.provider = jceProvider;
    }

    private RSAPrivateKey getCurrentEncryptionPrivateKey(String data) throws JWEFailure {
        if (this.privateKeys == null || this.privateKeys.isEmpty()) {
            // Base case for the sake of backwards compatibility...
            if (this.privateKey == null) {
                throw new JWEFailure("No keys were passed to the JWEService.", new Exception());
            }

            return this.privateKey;
        }

        RSAPrivateKey currentEncryptionPrivateKey;
        JsonWebEncryption jwe = getJsonWebEncryption();
        String currentEncryptionKeyId;

        try {
            jwe.setCompactSerialization(data);
            currentEncryptionKeyId = jwe.getKeyIdHeaderValue();
        } catch (JoseException e) {
            throw new JWEFailure("An error occurred attempting to fetch current encryption key ID", e);
        }

        currentEncryptionPrivateKey = this.privateKeys.get(currentEncryptionKeyId);

        if (currentEncryptionPrivateKey == null) {
            throw new JWEFailure("No keys exist that match the key ID provided.", new Exception());
        }

        return currentEncryptionPrivateKey;
    }

    @Override public String decrypt(String data) throws JWEFailure {
        return decrypt(data, this.getCurrentEncryptionPrivateKey(data));
    }


    @Override
    public String decrypt(String data, PrivateKey privateKey) throws JWEFailure {
        String decrypted;
        JsonWebEncryption jwe = getJsonWebEncryption();
        jwe.setKey(privateKey);
        try {
            jwe.setCompactSerialization(data);
            decrypted = jwe.getPlaintextString();
        } catch (JoseException e) {
            throw new JWEFailure("An error occurred attempting to decrypt a JWE", e);
        }
        return decrypted;
    }

    @Override public String encrypt(String data, PublicKey publicKey, String keyId, String contentType) throws JWEFailure {
        String encrypted;
        JsonWebEncryption jwe = getJsonWebEncryption();
        try {
            jwe.setKey(publicKey);
            jwe.setPlaintext(data);
            jwe.setKeyIdHeaderValue(keyId);
            jwe.setContentTypeHeaderValue(contentType);
            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512);
            encrypted = jwe.getCompactSerialization();
        } catch (JoseException e) {
            throw new JWEFailure("An error occurred attempting to encrypt a JWE", e);
        }
        return encrypted;
    }

    @Override
    public Map<String, String> getHeaders(String data) throws JWEFailure {
        try {
            JsonWebEncryption jwe = getJsonWebEncryption();
            jwe.setCompactSerialization(data);
            String headersJSON = jwe.getHeaders().getFullHeaderAsJsonString();
            Map<String, Object> objectMap = JsonUtil.parseJson(headersJSON);
            Map<String, String> headers = new LinkedHashMap<>(objectMap.size());
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                headers.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            return headers;
        } catch (Exception e) {
            throw new JWEFailure("Unable to parse data for JWE Header!", e);
        }
    }

    protected JsonWebEncryption getJsonWebEncryption() {
        JsonWebEncryption jwe =  new JsonWebEncryption();
        jwe.setProviderContext(getProviderContext(provider));
        return jwe;
    }
}
