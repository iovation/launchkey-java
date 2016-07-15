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

import com.launchkey.sdk.cache.LocalMemoryPingResponseCache;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.service.token.TokenIdService;
import com.launchkey.sdk.service.token.UUIDTokenIdService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.security.Provider;
import java.security.Security;
import java.util.concurrent.TimeUnit;

/**
 * Builder for building a {@link ClientFactory}
 */
public class ClientFactoryBuilder {

    private TokenIdService tokenIdService = null;
    private TokenIdService tokenIdServiceInstance = null;
    private PingResponseCache pingResponseCache = null;
    private PingResponseCache pingResponseCacheInstance = null;
    private Provider jceProvider = null;
    private Provider jceProviderInstance = null;
    private HttpClient httpClient = null;
    private HttpClient httpClientInstance = null;

    private String apiBaseURL = "https://api.launchkey.com";
    private String apiIdentifier = "application:1000000000";
    private Integer httpClientMaxClients = 200;
    private Integer httpClientConnectionTTLSecs = 30;
    private Integer requestExpireSeconds = 300;
    private int pingResponseCacheTTL = 300;

    /**
     * Build a factory based on the currently configured information
     * @return Client Factory
     */
    public ClientFactory build() {
        return new ClientFactory(
                getJceProvider(),
                getPingResponseCache(),
                getHttpClient(),
                getTokenIdService(),
                getApiBaseURL(),
                getApiIdentifier(),
                getRequestExpireSeconds()
        );

    }

    /**
     * Set the number of seconds in which the JWT will expire for a request
     *
     * @param requestExpireSeconds Number of seconds in which the JWT will expire for a request
     * @return this
     */
    public ClientFactoryBuilder setRequestExpireSeconds(int requestExpireSeconds) {
        this.requestExpireSeconds = requestExpireSeconds;
        return this;
    }

    /**
     * Set the JCE provider to be used to perform cryptography
     *
     * @param provider JCE provider to be used to build a {@link JCECrypto} service
     * @return this
     */
    public ClientFactoryBuilder setJCEProvider(Provider provider) {
        this.jceProvider = provider;
        return this;
    }

    /**
     * Set the Apache HTTP client that will be utilized for making requests of the Platform API
     *
     * @param httpClient Apache HTTP client that will be utilized for making requests of the Platform API
     * @return this
     */
    public ClientFactoryBuilder setHttpClient(HttpClient httpClient) {
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
    public ClientFactoryBuilder setHttpClientConnectionTTLSecs(int httpClientConnectionTTLSecs) {
        if (this.httpClientConnectionTTLSecs != httpClientConnectionTTLSecs) {
            this.httpClientConnectionTTLSecs = httpClientConnectionTTLSecs;
            this.httpClientInstance = null;
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
    public ClientFactoryBuilder setHttpMaxClients(int httpMaxClients) {
        if (this.httpClientMaxClients != httpMaxClients) {
            this.httpClientMaxClients = httpMaxClients;
            this.httpClientInstance = null;
        }
        return this;
    }

    /**
     * Set the ping response cache to be used for caching Platform API /ping calls
     *
     * @param pingResponseCache Ping response cache to be used for caching Platform API /ping calls
     * @return this
     */
    public ClientFactoryBuilder setPingResponseCache(PingResponseCache pingResponseCache) {
        this.pingResponseCache = pingResponseCache;
        return this;
    }

    /**
     * Set the Ping Response cache Time To Live in milliseconds.  This value will be ignored
     * if an {@link PingResponseCache} is set with {@link #setPingResponseCache(PingResponseCache)}
     *
     * @param pingResponseCacheTTL Set the Ping Response cache Time To Live in milliseconds
     * @return this
     */
    public ClientFactoryBuilder setPingResponseCacheTTL(int pingResponseCacheTTL) {
        if (this.pingResponseCacheTTL != pingResponseCacheTTL) {
            this.pingResponseCacheTTL = pingResponseCacheTTL;
            this.pingResponseCacheInstance = null;
        }
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
    public ClientFactoryBuilder setAPIBaseURL(String apiBaseURL) {
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
    public ClientFactoryBuilder setAPIdentifier(String apiIdentifier) {
        this.apiIdentifier = apiIdentifier;
        return this;
    }

    /**
     * Set the {@link TokenIdService} that will be utilized by the factory
     * @param tokenIdService {@link TokenIdService} that will be utilized by the factory
     * @return this
     */
    public ClientFactoryBuilder setTokenIdService(TokenIdService tokenIdService) {
        this.tokenIdService = tokenIdService;
        return this;
    }

    private PingResponseCache getPingResponseCache() {
        PingResponseCache cache;
        if (pingResponseCache == null) {
            if (pingResponseCacheInstance == null) {
                pingResponseCacheInstance = new LocalMemoryPingResponseCache(pingResponseCacheTTL);
            }
            cache = pingResponseCacheInstance;
        } else {
            cache = pingResponseCache;
        }
        return cache;
    }

    private TokenIdService getTokenIdService() {
        TokenIdService service;
        if (tokenIdService == null) {
            if (tokenIdServiceInstance == null) {
                tokenIdServiceInstance = new UUIDTokenIdService();
            }
            service = tokenIdServiceInstance;
        } else {
            service = tokenIdService;
        }
        return service;
    }

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
}
