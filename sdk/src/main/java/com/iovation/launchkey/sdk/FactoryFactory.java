package com.iovation.launchkey.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.apachehttp.ApacheHttpTransport;
import com.iovation.launchkey.sdk.transport.domain.EntityKeyMap;
import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.client.DirectoryFactory;
import com.iovation.launchkey.sdk.client.OrganizationFactory;
import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.crypto.jwe.Jose4jJWEService;
import com.iovation.launchkey.sdk.crypto.jwt.Jose4jJWTService;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier.EntityType;
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
        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<String, RSAPrivateKey>();
        keys.put(publicKeyFingerprint, privateKey);
        return makeServiceFactory(serviceId, keys, publicKeyFingerprint);
    }

    public ServiceFactory makeServiceFactory(String directoryId, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKey) {
        UUID serviceUUID = UUID.fromString(directoryId);
        EntityIdentifier serviceEntity = new EntityIdentifier(EntityType.SERVICE, serviceUUID);
        Transport transport = getTransport(serviceEntity, privateKeys, currentPrivateKey);
        ServiceFactory serviceFactory = new ServiceFactory(transport, serviceUUID);
        return serviceFactory;
    }

    public DirectoryFactory makeDirectoryFactory(String directoryId, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        String publicKeyFingerprint = getPublicKeyFingerprintFromPrivateKey(privateKey);
        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<String, RSAPrivateKey>();
        keys.put(publicKeyFingerprint, privateKey);
        return makeDirectoryFactory(directoryId, keys, publicKeyFingerprint);
    }

    public DirectoryFactory makeDirectoryFactory(String serviceId, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKey) {
        UUID directoryUUID = UUID.fromString(serviceId);
        EntityIdentifier directoryEntity = new EntityIdentifier(EntityType.DIRECTORY, directoryUUID);
        Transport transport = getTransport(directoryEntity, privateKeys, currentPrivateKey);
        DirectoryFactory directoryFactory = new DirectoryFactory(transport, directoryUUID);
        return directoryFactory;
    }

    public synchronized OrganizationFactory makeOrganizationFactory(String organizationId, String privateKeyPEM) {
        RSAPrivateKey privateKey = makePrivateKeyFromPEM(privateKeyPEM);
        String publicKeyFingerprint = getPublicKeyFingerprintFromPrivateKey(privateKey);
        Map<String, RSAPrivateKey> keys = new ConcurrentHashMap<String, RSAPrivateKey>();
        keys.put(publicKeyFingerprint, privateKey);
        return makeOrganizationFactory(organizationId, keys, publicKeyFingerprint);
    }

    public synchronized OrganizationFactory makeOrganizationFactory(String organizationId, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKey) {
        UUID organizationUUID = UUID.fromString(organizationId);
        EntityIdentifier organizationEntity = new EntityIdentifier(EntityType.ORGANIZATION, organizationUUID);
        Transport transport = getTransport(organizationEntity, privateKeys, currentPrivateKey);
        OrganizationFactory organizationFactory = new OrganizationFactory(transport, organizationUUID);
        return organizationFactory;
    }

    private Transport getTransport(
            EntityIdentifier entityIdentifier, Map<String, RSAPrivateKey> privateKeys, String currentPrivateKeyId) {
        for (Map.Entry<String, RSAPrivateKey> entry : privateKeys.entrySet()) {
            entityKeyMap.addKey(entityIdentifier, entry.getKey(), entry.getValue());
        }
        return new ApacheHttpTransport(
                httpClient,
                new JCECrypto(provider),
                new ObjectMapper(),
                keyCache,
                apiBaseURL,
                entityIdentifier,
                new Jose4jJWTService(apiIdentifier, privateKeys, currentPrivateKeyId, requestExpireSeconds),
                new Jose4jJWEService(privateKeys.get(currentPrivateKeyId)),
                offsetTTL,
                currentPublicKeyTTL,
                entityKeyMap
        );
    }

    private RSAPrivateKey makePrivateKeyFromPEM(String privateKeyPEM) {
        return JCECrypto.getRSAPrivateKeyFromPEM(provider, privateKeyPEM);
    }

    private String getPublicKeyFingerprintFromPrivateKey(RSAPrivateKey privateKey) {
        return JCECrypto.getRsaPublicKeyFingerprint(provider, privateKey);
    }
}
