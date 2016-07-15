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

import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.crypto.JCECrypto;
import com.launchkey.sdk.crypto.jwe.Jose4jJWEService;
import com.launchkey.sdk.crypto.jwt.JWTClaims;
import com.launchkey.sdk.crypto.jwt.Jose4jJWTService;
import com.launchkey.sdk.service.application.auth.AuthService;
import com.launchkey.sdk.service.application.auth.V1AuthService;
import com.launchkey.sdk.service.organization.whitelabel.V3WhiteLabelServiceFactory;
import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelServiceFactory;
import com.launchkey.sdk.service.ping.PingService;
import com.launchkey.sdk.service.ping.V1PingService;
import com.launchkey.sdk.service.token.TokenIdService;
import com.launchkey.sdk.transport.v1.ApacheHttpClientTransport;
import com.launchkey.sdk.transport.v1.Transport;
import org.apache.http.client.HttpClient;

import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;

/**
 * Factory for building {@link AppClient} and {@link OrgClient} objects.
 */
public class ClientFactory {
    private final HttpClient httpClient;
    private final Provider provider;
    private final PingResponseCache pingResponseCache;
    private final String apiBaseURL;
    private final String apiIdentifier;
    private final int requestExpireSeconds;
    private final TokenIdService tokenIdService;

    private V1PingService pingService;

    /**
     * @param provider JCE provider
     * @param pingResponseCache Cache to cache Ping responses
     * @param httpClient HTTP client
     * @param tokenIdService Service to generate Token IDs
     * @param apiBaseURL Base URL for the Platform API
     * @param apiIdentifier JWT identifier for the API. Used to send requests with the proper ID and validate
     *                      responses and server sent events.
     * @param requestExpireSeconds The number of seconds until a request JWT should expire.
     */
    public ClientFactory(
            Provider provider, PingResponseCache pingResponseCache, HttpClient httpClient,
            TokenIdService tokenIdService, String apiBaseURL, String apiIdentifier, int requestExpireSeconds
    ) {
        this.provider = provider;
        this.pingResponseCache = pingResponseCache;
        this.httpClient = httpClient;
        this.tokenIdService = tokenIdService;
        this.apiBaseURL = apiBaseURL;
        this.apiIdentifier = apiIdentifier;
        this.requestExpireSeconds = requestExpireSeconds;
    }

    /**
     * Make an {@link AppClient} with the provided data
     * @param appKey Application key from the keys section of the Application's page in Dashboard
     * @param secretKey Secret key from the keys section of the Application's page in Dashboard
     * @param privateKeyPEM PEM formatted string containing the RSA Private Key of the RSA public/private
     *                      key pair whose public key is associated with the Application.
     * @return App client
     */
    public AppClient makeAppClient(long appKey, String secretKey, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        return this.makeAppClient(appKey, secretKey, privateKey);
    }

    /**
     * Make an {@link AppClient} with the provided data
     * @param appKey Application key from the keys section of the Application's page in Dashboard
     * @param secretKey Secret key from the keys section of the Application's page in Dashboard
     * @param privateKey RSA Private Key of the RSA public/private key pair whose public key is associated
     *                   with the Application.
     * @return App client
     */
    public AppClient makeAppClient(long appKey, String secretKey, RSAPrivateKey privateKey) {
        Crypto crypto = new JCECrypto(privateKey, provider);
        Transport transport = new ApacheHttpClientTransport(httpClient, getV1BaseUrl(), crypto);
        PingService pingService = getPingService(crypto);
        AuthService authService = new V1AuthService(transport, crypto, pingService, appKey, secretKey);
        AppClient appClient = new BasicAppClient(authService);
        return appClient;
    }

    /**
     * Make an {@link OrgClient} with the provided data.
     * @param orgKey Organization key from the keys section of the Organizations's page in Dashboard
     * @param privateKeyPEM PEM formatted string containing the RSA Private Key of the RSA public/private
     *                      key pair whose public key is associated with the Organization.
     * @return Org client
     */
    public synchronized OrgClient makeOrgClient(long orgKey, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        return this.makeOrgClient(orgKey, privateKey);
    }

    /**
     * Make an {@link OrgClient} with the provided data.
     * @param orgKey Organization key from the keys section of the Organizations's page in Dashboard
     * @param privateKey RSA Private Key of the RSA public/private key pair whose public key is associated
     *                   with the Organization.
     * @return org Client
     */
    public synchronized OrgClient makeOrgClient(long orgKey, RSAPrivateKey privateKey) {
        com.launchkey.sdk.transport.v3.Transport transport = getV3Transport(
                privateKey,
                "organization: ".concat(String.valueOf(orgKey))
        );
        WhiteLabelServiceFactory wlsFactory = new V3WhiteLabelServiceFactory(
                transport, privateKey, orgKey
        );
        OrgClient orgClient = new BasicOrgClient(wlsFactory);
        return orgClient;
    }

    private com.launchkey.sdk.transport.v3.Transport getV3Transport(RSAPrivateKey privateKey, String entityIdentifier) {
        Crypto crypto = new JCECrypto(privateKey, provider);
        PingService pingService = getPingService(crypto);
        return new com.launchkey.sdk.transport.v3.ApacheHttpTransport(
                httpClient,
                apiBaseURL,
                apiIdentifier,
                new Jose4jJWTService(apiIdentifier, entityIdentifier, privateKey, pingService, requestExpireSeconds),
                new Jose4jJWEService(privateKey, pingService),
                crypto,
                pingService,
                tokenIdService
        );
    }

    private PingService getPingService(Crypto crypto) {
        if (pingService == null) {
            Transport transport = new ApacheHttpClientTransport(httpClient, getV1BaseUrl(), crypto);
            pingService = new V1PingService(
                    pingResponseCache,
                    transport,
                    crypto
            );
        }
        return pingService;
    }

    private RSAPrivateKey makePrivateKeyFromPEM(String privateKeyPEM) {
        return JCECrypto.getRSAPrivateKeyFromPEM(provider, privateKeyPEM);
    }

    private String getV1BaseUrl() {
        String v1BaseUrl;
        if (apiBaseURL.endsWith("/")) {
            v1BaseUrl = apiBaseURL.substring(0, apiBaseURL.length() - 1);
        } else {
            v1BaseUrl = apiBaseURL;
        }
        return v1BaseUrl.concat("/v1");
    }
}
