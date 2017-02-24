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

package com.launchkey.sdk;

import com.launchkey.sdk.cache.Cache;
import com.launchkey.sdk.cache.HashCache;
import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.transport.domain.EntityIdentifier;
import com.launchkey.sdk.transport.domain.EntityIdentifier.EntityType;
import com.launchkey.sdk.transport.domain.EntityKeyMap;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.security.Provider;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Builder for building a {@link FactoryFactory}
 */
public class FactoryFactoryBuilder {

    private Provider jceProvider = null;
    private Provider jceProviderInstance = null;
    private HttpClient httpClient = null;
    private Cache keyCache = null;

    private String apiBaseURL = "https://api.launchkey.com";
    private String apiIdentifier = "lka";
    private Integer httpClientMaxClients = 200;
    private Integer httpClientConnectionTTLSecs = 30;
    private Integer requestExpireSeconds = 5;
    private int offsetTTL = 3600;
    private int currentPublicKeyTTL = 300;
    private EntityKeyMap entityKeyMap = new EntityKeyMap();

    /**
     * Build a factory based on the currently configured information
     *
     * @return Client Factory Factory
     */
    public FactoryFactory build() {
        return new FactoryFactory(
                getJceProvider(),
                getHttpClient(),
                getKeyCache(),
                getApiBaseURL(),
                getApiIdentifier(),
                getRequestExpireSeconds(),
                offsetTTL,
                currentPublicKeyTTL,
                entityKeyMap
        );

    }

    /**
     * Set the number of seconds in which the JWT will expire for a request
     *
     * @param requestExpireSeconds Number of seconds in which the JWT will expire for a request
     * @return this
     */
    public FactoryFactoryBuilder setRequestExpireSeconds(int requestExpireSeconds) {
        this.requestExpireSeconds = requestExpireSeconds;
        return this;
    }

    /**
     * Set the JCE provider to be used to perform cryptography
     *
     * @param provider JCE provider to be used to build a {@link JCECrypto} getServiceService
     * @return this
     */
    public FactoryFactoryBuilder setJCEProvider(Provider provider) {
        this.jceProvider = provider;
        return this;
    }

    /**
     * Set the Apache HTTP client that will be utilized for making requests of the Platform API
     *
     * @param httpClient Apache HTTP client that will be utilized for making requests of the Platform API
     * @return this
     */
    public FactoryFactoryBuilder setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    /**
     * Set the Time To Live in seconds for HTTP client connections in the connection pool.  This value will be ignored
     * if an {@link HttpClient} is set with {@link #setHttpClient(HttpClient)}
     *
     * @param httpClientConnectionTTLSecs Time To Live in seconds for HTTP client connections in the connection pool.
     * @return this
     */
    public FactoryFactoryBuilder setHttpClientConnectionTTLSecs(int httpClientConnectionTTLSecs) {
        if (this.httpClientConnectionTTLSecs != httpClientConnectionTTLSecs) {
            this.httpClientConnectionTTLSecs = httpClientConnectionTTLSecs;
        }
        return this;
    }

    /**
     * Set the max HTTP client connections in the connection pool.  This value will be ignored
     * if an {@link HttpClient} is set with {@link #setHttpClient(HttpClient)}
     *
     * @param httpMaxClients Max HTTP client connections in the connection pool
     * @return this
     */
    public FactoryFactoryBuilder setHttpMaxClients(int httpMaxClients) {
        if (this.httpClientMaxClients != httpMaxClients) {
            this.httpClientMaxClients = httpMaxClients;
        }
        return this;
    }

    public FactoryFactoryBuilder setKeyCache(Cache keyCache) {
        this.keyCache = keyCache;
        return this;
    }

    public FactoryFactoryBuilder setOffsetTTL(int offsetTTL) {
        this.offsetTTL = offsetTTL;
        return this;
    }

    public FactoryFactoryBuilder setCurrentPublicKeyTTL(int currentPublicKeyTTL) {
        this.currentPublicKeyTTL = currentPublicKeyTTL;
        return this;
    }

    private String getApiBaseURL() {
        return apiBaseURL;
    }

    /**
     * Set the base URL for the Platform API. e.g.: https://api.example.com
     *
     * @param apiBaseURL Base URL for the Platform API. e.g.: https://api.example.com
     * @return this
     */
    public FactoryFactoryBuilder setAPIBaseURL(String apiBaseURL) {
        this.apiBaseURL = apiBaseURL;
        return this;
    }

    private String getApiIdentifier() {
        return apiIdentifier;
    }

    /**
     * Set the base URL for the Platform API. e.g.: application:1000000000
     *
     * @param apiIdentifier Identifier for Platform API. e.g.: application:1000000000
     * @return this
     */
    public FactoryFactoryBuilder setAPIdentifier(String apiIdentifier) {
        this.apiIdentifier = apiIdentifier;
        return this;
    }

    public FactoryFactoryBuilder addServicePrivateKey(String serviceId, RSAPrivateKey privateKey, String pulicKeyFingerprint) {
        addEntityPrivateKey(EntityType.SERVICE, serviceId, privateKey, pulicKeyFingerprint);
        return this;
    }

    public FactoryFactoryBuilder addDirectoryPrivateKey(String directoryId, RSAPrivateKey privateKey, String pulicKeyFingerprint) {
        addEntityPrivateKey(EntityType.DIRECTORY, directoryId, privateKey, pulicKeyFingerprint);
        return this;
    }

    public FactoryFactoryBuilder addOrganizationPrivateKey(String organizationId, RSAPrivateKey privateKey, String pulicKeyFingerprint) {
        addEntityPrivateKey(EntityType.ORGANIZATION, organizationId, privateKey, pulicKeyFingerprint);
        return this;
    }

    private void addEntityPrivateKey(EntityType type, String id, RSAPrivateKey privateKey, String pulicKeyFingerprint) {
        entityKeyMap.addKey(
                new EntityIdentifier(type, UUID.fromString(id)),
                pulicKeyFingerprint,
                privateKey
        );
    }

    @SuppressWarnings("Duplicates")
    private HttpClient getHttpClient() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                    httpClientConnectionTTLSecs,
                    TimeUnit.SECONDS
            );
            connectionManager.setMaxTotal(httpClientMaxClients);
            // Set max per route as there is only one route
            connectionManager.setDefaultMaxPerRoute(httpClientMaxClients);
            httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        }
        return httpClient;
    }

    private Integer getRequestExpireSeconds() {
        return requestExpireSeconds;
    }

    private Provider getJceProvider() {
        Provider provider;
        if (jceProvider == null) {
            if (jceProviderInstance == null) {
                jceProviderInstance = Security.getProviders()[0];
            }
            provider = jceProviderInstance;
        } else {
            provider = jceProvider;
        }
        return provider;
    }

    private Cache getKeyCache() {
        if (keyCache == null) {
            keyCache = new HashCache();
        }
        return keyCache;
    }
}
