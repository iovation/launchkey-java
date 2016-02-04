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

package com.launchkey.sdk;

import com.launchkey.sdk.cache.LocalMemoryPingResponseCache;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.service.auth.AuthService;
import com.launchkey.sdk.service.auth.V1AuthService;
import com.launchkey.sdk.service.whitelabel.V1WhiteLabelService;
import com.launchkey.sdk.service.whitelabel.WhiteLabelService;
import com.launchkey.sdk.transport.v1.ApacheHttpClientTransport;
import com.launchkey.sdk.transport.v1.Transport;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.security.PrivateKey;
import java.security.Provider;
import java.util.concurrent.TimeUnit;

/**
 * Client for interacting with the LaunchKey Engine API.
 */
public class LaunchKeyClient {
    public static final String DEFAULT_API_BASE_URL = "https://api.launchkey.com/v1";
    public static final int DEFAULT_HTTP_CLIENT_TTL_SECS = 30;
    public static final int DEFAULT_HTTP_CLIENT_MAX_CLIENTS = 200;
    private static final int DEFAULT_PING_CACHE_TTL_SECS = 300;
    private final AuthService auth;

    private final WhiteLabelService whiteLabel;

    /**
     * LaunchKey client with all services
     * @param auth auth service
     * @param whiteLabel white label service
     */
    public LaunchKeyClient(AuthService auth, WhiteLabelService whiteLabel) {
        this.auth = auth;
        this.whiteLabel = whiteLabel;
    }

    /**
     * Get an auth service
     * @return auth service
     */
    public AuthService auth() {
        return auth;
    }

    /**
     * Get a white label service
     * @return white label service
     */
    public WhiteLabelService whiteLabel() {
        return whiteLabel;
    }

    /**
     * Build a LaunchKey Client from the provided config data
     * @param config Config object for the factory
     * @return a LaunchKey client
     */
    public static LaunchKeyClient factory(Config config) {
        long rocketKey = config.getRocketKey();

        String secretKey = config.getSecretKey();

        Crypto crypto = getCrypto(config);

        Transport transport = getTransport(config, crypto);

        PingResponseCache pingResponseCache = getPingResponseCache(config);


        AuthService authService = new V1AuthService(transport, crypto, pingResponseCache, rocketKey, secretKey);
        WhiteLabelService whiteLabelService = new V1WhiteLabelService(
                transport, crypto, pingResponseCache, rocketKey, secretKey
        );
        return new LaunchKeyClient(authService, whiteLabelService);
    }

    /**
     * Build a LaunchKey client using the provided Rocket Key, Secret Key, Private Key and JCE Provider
     * @param rocketKey Rocket Key for the Rocket that will used to make requests
     * @param secretKey Secret Key for the Rocket that will used to make requests
     * @param privateKeyPEM Private Key PEM formatted string that will be used to generate a {@link PrivateKey} to
     *                      be used by the crypto service for decrypting and signing via RSA
     * @param provider JCE provider to be used to build a {@link com.launchkey.sdk.crypto.JCECrypto} service
     * @return a LaunchKey client
     */
    public static LaunchKeyClient factory(long rocketKey, String secretKey, String privateKeyPEM, Provider provider) {
        Config config = new Config(rocketKey, secretKey);
        config.setRSAPrivateKeyPEM(privateKeyPEM);
        config.setJCEProvider(provider);
        return factory(config);
    }

    private static PingResponseCache getPingResponseCache(Config config) {
        PingResponseCache pingResponseCache;
        if (config.getPingResponseCache() == null) {
            int ttl = config.getPingResponseCacheTTL() == null ? DEFAULT_PING_CACHE_TTL_SECS * 1000 : config.getPingResponseCacheTTL();
            pingResponseCache = new LocalMemoryPingResponseCache(ttl);
        } else {
            pingResponseCache = config.getPingResponseCache();
        }
        return pingResponseCache;
    }

    private static Transport getTransport(Config config, Crypto crypto) {
        HttpClient httpClient;
        if (config.getApacheHttpClient() != null) {
            httpClient = config.getApacheHttpClient();
        } else {
            int ttlSecs = config.getHttpClientConnectionTTLSecs() == null
                    ? DEFAULT_HTTP_CLIENT_TTL_SECS
                    : config.getHttpClientConnectionTTLSecs();

            int maxClients = config.getHttpMaxClients() == null
                    ? DEFAULT_HTTP_CLIENT_MAX_CLIENTS
                    : config.getHttpMaxClients();
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                    ttlSecs,
                    TimeUnit.SECONDS
            );
            connectionManager.setMaxTotal(maxClients);
            connectionManager.setDefaultMaxPerRoute(maxClients); // Set max per route as there is only one route
            httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        }

        String baseUrl = config.getAPIBaseURL() == null ? DEFAULT_API_BASE_URL : config.getAPIBaseURL();
        return new ApacheHttpClientTransport(httpClient, baseUrl, crypto);
    }

    private static PrivateKey getPrivateKey(Config config, Provider jceProvider) {
        PrivateKey privateKey;
        if (config.getPrivateKey() != null) {
            privateKey = config.getPrivateKey();
        } else if (config.getRSAPrivateKeyPEM() != null) {
            privateKey = JCECrypto.getRSAPrivateKeyFromPEM(jceProvider, config.getRSAPrivateKeyPEM());
        } else {
            throw new IllegalArgumentException(
                    "You must specify either an RSA private key or RSA private key PEM" +
                            " string int he config in the config"
            );
        }
        return privateKey;
    }

    private static Crypto getCrypto(Config config) {
        Crypto crypto;
        if (config.getCrypto() != null) {
            return config.getCrypto();
        } else if (config.getJCEProvider() != null) {
            PrivateKey privateKey = getPrivateKey(config, config.getJCEProvider());
            crypto = new JCECrypto(privateKey, config.getJCEProvider());
        } else {
            throw new IllegalArgumentException("No crypto or JCE Provider specified in config");
        }
        return crypto;
    }
}
