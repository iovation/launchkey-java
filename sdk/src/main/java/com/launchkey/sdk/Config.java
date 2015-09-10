/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk;

import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import org.apache.http.client.HttpClient;

import java.security.PrivateKey;
import java.security.Provider;

/**
 * Configuration object for building LaunchKeyClient objects with the factory
 */
public class Config {

    private final long rocketKey;

    private final String secretKey;

    private PrivateKey privateKey;
    private Crypto crypto;
    private Provider JCEProvider;
    private String rsaPrivateKeyPEM;
    private HttpClient apacheHttpClient;
    private Integer httpClientConnectionTTLSecs;
    private Integer httpMaxClients;
    private Integer pingResponseCacheTTL;
    private String APIBaseURL;
    private PingResponseCache pingResponseCache;

    /**
     *
     * @param rocketKey Rocket Key for the Rocket that will used to make requests
     * @param secretKey Secret Key for the Rocket that will used to make requests
     */
    public Config(long rocketKey, String secretKey) {
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey cannot be null");
        }
        this.rocketKey = rocketKey;
        this.secretKey = secretKey;
    }


    /**
     * Get the Rocket Key for the Rocket that will used to make requests
     * @return Rocket Key for the Rocket that will used to make requests
     */
    public long getRocketKey() {
        return rocketKey;
    }

    /**
     * Get the Secret Key for the Rocket that will used to make requests
     * @return Secret Key for the Rocket that will used to make requests
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Set the crypto service that will be used to encrypt, decrypt, and sign data
     * @param crypto Crypto service that will be used to encrypt, decrypt, and sign data
     * @return
     */
    public Config setCrypto(Crypto crypto) {
        this.crypto = crypto;
        return this;
    }

    /**
     * Get the crypto service that will be used to encrypt, decrypt, and sign data
     * @return Crypto service that will be used to encrypt, decrypt, and sign data
     */
    public Crypto getCrypto() {
        return crypto;
    }

    /**
     * Set the JCE provider to be used to build a {@link com.launchkey.sdk.crypto.JCECrypto} service.  This value will
     * be ignored if a {@link Crypto} service is provided to {@link #setCrypto(Crypto)}
     * @param JCEProvider JCE provider to be used to build a {@link com.launchkey.sdk.crypto.JCECrypto} service
     * @return
     */
    public Config setJCEProvider(Provider JCEProvider) {
        this.JCEProvider = JCEProvider;
        return this;
    }

    /**
     * Get the JCE provider to be used for a {@link com.launchkey.sdk.crypto.JCECrypto} service
     * @return JCE Provider to be used for a {@link com.launchkey.sdk.crypto.JCECrypto} service
     */
    public Provider getJCEProvider() {
        return JCEProvider;
    }

    /**
     * Set the Private Key to be used by the crypto service for decrypting and signing via RSA
     * @param privateKey Private Key to be used by the crypto service for decrypting and signing via RSA
     * @return
     */
    public Config setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    /**
     * Get the Private Key to be used by the crypto service for decrypting and signing via RSA
     * @return Private Key to be used by the crypto service for decrypting and signing via RSA
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Set the Private Key PEM formatted string that will be used to generate a {@link PrivateKey} to be used by the
     * crypto service for decrypting and signing via RSA.  This value will be ignored if a {@link PrivateKey} is
     * provided via {@link #setPrivateKey}.
     * @param rsaPrivateKeyPEM Private Key PEM formatted string that will be used to generate a {@link PrivateKey} to be used by the
     * crypto service for decrypting and signing via RSA
     * @return
     */
    public Config setRSAPrivateKeyPEM(String rsaPrivateKeyPEM) {
        this.rsaPrivateKeyPEM = rsaPrivateKeyPEM;
        return this;
    }

    /**
     * Get the Private Key PEM formatted string that will be used to generate a {@link PrivateKey} to be used by the
     * crypto service for decrypting and signing via RSA
     * @return Private Key PEM formatted string that will be used to generate a {@link PrivateKey} to be used by the
     * crypto service for decrypting and signing via RSA
     */
    public String getRSAPrivateKeyPEM() {
        return rsaPrivateKeyPEM;
    }

    /**
     * Set the Apache HTTP client that will be utilized for making requests of teh LaunchKey API
     * @param apacheHttpClient Apache HTTP client that will be utilized for making requests of the LaunchKey API
     * @return
     */
    public Config setApacheHttpClient(HttpClient apacheHttpClient) {
        this.apacheHttpClient = apacheHttpClient;
        return this;
    }

    /**
     * Get the Apache HTTP client that will be utilized for making requests of teh LaunchKey API
     * @return Apache HTTP client that will be utilized for making requests of teh LaunchKey API
     */
    public HttpClient getApacheHttpClient() {
        return apacheHttpClient;
    }

    /**
     * Set the Time To Live in seconds for HTTP client connections in the connection pool.  This value will be ignored
     * if an {@link HttpClient} is set with {@link #setApacheHttpClient(HttpClient)}
     * @param httpClientConnectionTTLSecs Time To Live in seconds for HTTP client connections in the connection pool.
     * @return
     */
    public Config setHttpClientConnectionTTLSecs(int httpClientConnectionTTLSecs) {
        this.httpClientConnectionTTLSecs = httpClientConnectionTTLSecs;
        return this;
    }

    /**
     * Get the Time To Live in seconds for HTTP client connections in the connection pool.
     * @return Time To Live in seconds for HTTP client connections in the connection pool.
     */
    public Integer getHttpClientConnectionTTLSecs() {
        return httpClientConnectionTTLSecs;
    }

    /**
     * Set the max HTTP client connections in the connection pool.  This value will be ignored
     * if an {@link HttpClient} is set with {@link #setApacheHttpClient(HttpClient)}
     * @param httpMaxClients Max HTTP client connections in the connection pool
     * @return
     */
    public Config setHttpMaxClients(int httpMaxClients) {
        this.httpMaxClients = httpMaxClients;
        return this;
    }

    /**
     * Get the max HTTP client connections in the connection pool.
     * @return Max HTTP client connections in the connection pool
     */
    public Integer getHttpMaxClients() {
        return httpMaxClients;
    }

    /**
     * Set the Ping Response cache Time To Live in milliseconds.  This value will be ignored
     * if an {@link PingResponseCache} is set with {@link #setPingResponseCache(PingResponseCache)}
     * @param pingResponseCacheTTL Set the Ping Response cache Time To Live in milliseconds
     * @return
     */
    public Config setPingResponseCacheTTL(int pingResponseCacheTTL) {
        this.pingResponseCacheTTL = pingResponseCacheTTL;
        return this;
    }

    /**
     * Get the Ping Response cache Time To Live in milliseconds
     * @return Set the Ping Response cache Time To Live in milliseconds
     */
    public Integer getPingResponseCacheTTL() {
        return pingResponseCacheTTL;
    }

    /**
     * Set the base URL for the LaunchKey Engine API. e.g.: https://api.launchkey.com/v1
     * @param APIBaseURL Base URL for the LaunchKey Engine API. e.g.: https://api.launchkey.com/v1
     * @return
     */
    public Config setAPIBaseURL(String APIBaseURL) {
        this.APIBaseURL = APIBaseURL;
        return this;
    }

    /**
     * Get the base URL for the LaunchKey Engine API. e.g.: https://api.launchkey.com/v1
     * @return Base URL for the LaunchKey Engine API. e.g.: https://api.launchkey.com/v1
     */
    public String getAPIBaseURL() {
        return APIBaseURL;
    }

    /**
     * Set the ping response cache to be used for caching LaunchKey Engine API /ping calls
     * @param pingResponseCache Ping response cache to be used for caching LaunchKey Engine API /ping calls
     * @return
     */
    public Config setPingResponseCache(PingResponseCache pingResponseCache) {
        this.pingResponseCache = pingResponseCache;
        return this;
    }

    /**
     * Get the ping response cache to be used for caching LaunchKey Engine API /ping calls
     * @return Ping response cache to be used for caching LaunchKey Engine API /ping calls
     */
    public PingResponseCache getPingResponseCache() {
        return pingResponseCache;
    }
}
