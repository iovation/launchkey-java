package com.iovation.launchkey.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.client.DirectoryFactory;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.crypto.jwe.Jose4jJWEService;
import com.iovation.launchkey.sdk.crypto.jwt.Jose4jJWTService;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.apachehttp.ApacheHttpTransport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier.EntityType;
import com.iovation.launchkey.sdk.transport.domain.EntityKeyMap;
import org.apache.http.client.HttpClient;

import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for building {@link ServiceFactory},
 * {@link DirectoryFactory}, and
 * {@link OrganizationFactory} objects.
 */
public class FactoryFactory {
    private final HttpClient httpClient;
    private final Cache keyCache;
    private final Provider provider;
    private final String apiBaseURL;
    private final String apiIdentifier;
    private final int requestExpireSeconds;
    private final int offsetTTL;
    private final int currentPublicKeyTTL;
    private final EntityKeyMap entityKeyMap;

    /**
     * @param provider JCE provider
     * @param httpClient HTTP client
     * @param keyCache Caching for public keys from LaunchKey API
     * @param apiBaseURL Base URL for the Platform API
     * @param apiIdentifier JWT identifier for the API. Used to send requests with the proper ID and validate
     * responses and server sent events.
     * @param requestExpireSeconds The number of seconds until a request JWT should expire.
     * @param offsetTTL The number of seconds the API time offset will live before obtaining another using a ping call.
     * @param currentPublicKeyTTL The number of seconds to current public key as reported by a public key call will
     * live before obtaining the value again from the API.
     * @param entityKeyMap Mapping of entity private keys to allow for parsing Server Sent Events from entities
     */
    public FactoryFactory(
            Provider provider, HttpClient httpClient, Cache keyCache,
            String apiBaseURL, String apiIdentifier, int requestExpireSeconds,
            int offsetTTL, int currentPublicKeyTTL, EntityKeyMap entityKeyMap) {
        this.provider = provider;
        this.httpClient = httpClient;
        this.apiBaseURL = apiBaseURL;
        this.apiIdentifier = apiIdentifier;
        this.requestExpireSeconds = requestExpireSeconds;
        this.keyCache = keyCache;
        this.offsetTTL = offsetTTL;
        this.currentPublicKeyTTL = currentPublicKeyTTL;
        this.entityKeyMap = entityKeyMap;
    }

    public ServiceFactory makeServiceFactory(String serviceId, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        String publicKeyFingerprint = getPublicKeyFingerprintFromPrivateKey(privateKey);
        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<>();
        keys.put(publicKeyFingerprint, privateKey);
        return makeServiceFactory(serviceId, keys, publicKeyFingerprint);
    }

    public ServiceFactory makeServiceFactory(String serviceId, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKey) {
        UUID serviceUUID = UUID.fromString(serviceId);
        EntityIdentifier serviceEntity = new EntityIdentifier(EntityType.SERVICE, serviceUUID);
        Transport transport = getTransport(serviceEntity, privateKeys, currentPrivateKey);
        return new ServiceFactory(transport, serviceUUID);
    }

    public DirectoryFactory makeDirectoryFactory(String directoryId, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        String publicKeyFingerprint = getPublicKeyFingerprintFromPrivateKey(privateKey);
        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<>();
        keys.put(publicKeyFingerprint, privateKey);
        return makeDirectoryFactory(directoryId, keys, publicKeyFingerprint);
    }

    public DirectoryFactory makeDirectoryFactory(String directoryId, Map<String, RSAPrivateKey> privateKeys,
                                                 String currentPrivateKey) {
        if (directoryId == null) throw new IllegalArgumentException("Argument directoryId cannot be null");
        UUID directoryUUID = UUID.fromString(directoryId);
        EntityIdentifier directoryEntity = new EntityIdentifier(EntityType.DIRECTORY, directoryUUID);
        Transport transport = getTransport(directoryEntity, privateKeys, currentPrivateKey);
        return new DirectoryFactory(transport, directoryUUID);
    }

    public synchronized OrganizationFactory makeOrganizationFactory(String organizationId, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        String publicKeyFingerprint = getPublicKeyFingerprintFromPrivateKey(privateKey);
        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<>();
        keys.put(publicKeyFingerprint, privateKey);
        return makeOrganizationFactory(organizationId, keys, publicKeyFingerprint);
    }

    public synchronized OrganizationFactory makeOrganizationFactory(String organizationId,
                                                                    Map<String, RSAPrivateKey> privateKeys,
                                                                    String currentPrivateKey) {
        if (organizationId == null) throw new IllegalArgumentException("Argument organizationId cannot be null");
        UUID organizationUUID = UUID.fromString(organizationId);
        EntityIdentifier organizationEntity = new EntityIdentifier(EntityType.ORGANIZATION, organizationUUID);
        Transport transport = getTransport(organizationEntity, privateKeys, currentPrivateKey);
        return new OrganizationFactory(transport, organizationUUID);
    }

    private Transport getTransport(
            EntityIdentifier entityIdentifier, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKeyId) {
        for (Map.Entry<String, RSAPrivateKey> entry : privateKeys.entrySet()) {
            entityKeyMap.addKey(entityIdentifier, entry.getKey(), entry.getValue());
        }
        return new ApacheHttpTransport(
                httpClient,
                new JCECrypto(provider),
                getObjectMapper(),
                keyCache,
                apiBaseURL,
                entityIdentifier,
                new Jose4jJWTService(apiIdentifier, privateKeys, currentPrivateKeyId, requestExpireSeconds, provider.getName()),
                new Jose4jJWEService(privateKeys.get(currentPrivateKeyId), provider.getName()),
                offsetTTL,
                currentPublicKeyTTL,
                entityKeyMap
        );
    }

    private ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    private RSAPrivateKey makePrivateKeyFromPEM(String privateKeyPEM) {
        return JCECrypto.getRSAPrivateKeyFromPEM(provider, privateKeyPEM);
    }

    private String getPublicKeyFingerprintFromPrivateKey(RSAPrivateKey privateKey) {
        return JCECrypto.getRsaPublicKeyFingerprint(provider, privateKey);
    }
}
