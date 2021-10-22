/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.domain.KeyType;
import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.organization.Directory;
import com.iovation.launchkey.sdk.domain.policy.LegacyPolicy;
import com.iovation.launchkey.sdk.domain.policy.Policy;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.logging.Logger;

public class BasicOrganizationClient extends ServiceManagingBaseClient implements OrganizationClient {
    private final Transport transport;
    private final EntityIdentifier organization;

    public BasicOrganizationClient(UUID organizationId, Transport transport) {
        this.transport = transport;
        this.organization = new EntityIdentifier(EntityIdentifier.EntityType.ORGANIZATION, organizationId);
    }

    @Override
    public UUID createDirectory(String name)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        final OrganizationV3DirectoriesPostRequest request = new OrganizationV3DirectoriesPostRequest(name);
        OrganizationV3DirectoriesPostResponse response = transport.organizationV3DirectoriesPost(request, organization);
        return response.getId();
    }

    @Override
    public void updateDirectory(UUID directoryId, Boolean active, String androidKey, String iosP12,
                                Boolean denialContextInquiryEnabled, URI webhookUrl)
        throws CommunicationErrorException, MarshallingError, InvalidResponseException,
                InvalidCredentialsException, CryptographyError {
        final OrganizationV3DirectoriesPatchRequest request =
                new OrganizationV3DirectoriesPatchRequest(directoryId, active, androidKey, iosP12,
                        denialContextInquiryEnabled, webhookUrl);
        transport.organizationV3DirectoriesPatch(request, organization);
    }

    @Override
    public void updateDirectory(UUID directoryId, Boolean active, String androidKey, String iosP12,
                                Boolean denialContextInquiryEnabled)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError {
        updateDirectory(directoryId, active, androidKey, iosP12, denialContextInquiryEnabled, null);
    }

    @Override
    public void updateDirectory(UUID directoryId, Boolean active, String androidKey, String iosP12) throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException, InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        updateDirectory(directoryId, active, androidKey, iosP12, null);
    }

    @Override
    public Directory getDirectory(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final OrganizationV3DirectoriesListPostRequest request =
                new OrganizationV3DirectoriesListPostRequest(Collections.singletonList(directoryId));
        final OrganizationV3DirectoriesListPostResponse response =
                transport.organizationV3DirectoriesListPost(request, organization);
        OrganizationV3DirectoriesListPostResponseDirectory directory = response.getDirectories().get(0);
        return new Directory(directory.getId(), directory.getName(), directory.isActive(), directory.getServiceIds(),
                directory.getSdkKeys(), directory.getAndroidKey(), directory.getIosCertificateFingerprint(),
                directory.isDenialContextInquiryEnabled(), directory.getWebhookUrl());

    }

    @Override
    public List<Directory> getDirectories(List<UUID> directoryIds)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final OrganizationV3DirectoriesListPostRequest request = new OrganizationV3DirectoriesListPostRequest(
                directoryIds);
        final OrganizationV3DirectoriesListPostResponse response =
                transport.organizationV3DirectoriesListPost(request, organization);
        List<Directory> directories = new ArrayList<>();
        for (OrganizationV3DirectoriesListPostResponseDirectory directory : response.getDirectories()) {
            directories.add(new Directory(directory.getId(), directory.getName(), directory.isActive(),
                    directory.getServiceIds(), directory.getSdkKeys(), directory.getAndroidKey(),
                    directory.getIosCertificateFingerprint(), directory.isDenialContextInquiryEnabled(),
                    directory.getWebhookUrl()));
        }
        return directories;
    }

    @Override
    public List<Directory> getAllDirectories()
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        OrganizationV3DirectoriesGetResponse response = transport.organizationV3DirectoriesGet(organization);
        List<Directory> directories = new ArrayList<>();
        for (OrganizationV3DirectoriesGetResponseDirectory responseDirectory : response.getDirectories()) {
            directories.add(new Directory(responseDirectory.getId(), responseDirectory.getName(),
                    responseDirectory.isActive(), responseDirectory.getServiceIds(), responseDirectory.getSdkKeys(),
                    responseDirectory.getAndroidKey(), responseDirectory.getIosCertificateFingerprint(),
                    responseDirectory.isDenialContextInquiryEnabled(), responseDirectory.getWebhookUrl()));
        }
        return directories;
    }

    @Override
    public UUID generateAndAddDirectorySdkKey(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final OrganizationV3DirectorySdkKeysPostRequest request =
                new OrganizationV3DirectorySdkKeysPostRequest(directoryId);
        final OrganizationV3DirectorySdkKeysPostResponse response =
                transport.organizationV3DirectorySdkKeysPost(request, organization);
        return response.getSdkKey();
    }

    @Override
    public void removeDirectorySdkKey(UUID directoryId, UUID sdkKey)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final OrganizationV3DirectorySdkKeysDeleteRequest request =
                new OrganizationV3DirectorySdkKeysDeleteRequest(directoryId, sdkKey);
        transport.organizationV3DirectorySdkKeysDelete(
                request, organization);
    }

    @Override
    public List<UUID> getAllDirectorySdkKeys(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final OrganizationV3DirectorySdkKeysListPostRequest request =
                new OrganizationV3DirectorySdkKeysListPostRequest(directoryId);
        final OrganizationV3DirectorySdkKeysListPostResponse response =
                transport.organizationV3DirectorySdkKeysListPost(request, organization);
        return response.getSdkKeys();
    }

    @Override
    public List<PublicKey> getDirectoryPublicKeys(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        OrganizationV3DirectoryKeysListPostRequest request =
                new OrganizationV3DirectoryKeysListPostRequest(directoryId);
        final KeysListPostResponse response =
                transport.organizationV3DirectoryKeysListPost(request, organization);
        List<PublicKey> publicKeys = new ArrayList<>();
        for (KeysListPostResponsePublicKey publicKey : response.getPublicKeys()) {
            publicKeys.add(new PublicKey(publicKey.getId(), publicKey.isActive(), publicKey.getCreated(),
                    publicKey.getExpires(), publicKey.getKeyType()));
        }
        return publicKeys;
    }

    @Override
    public String addDirectoryPublicKey(UUID directoryId, RSAPublicKey publicKey, Boolean active, Date expires,
                                        KeyType key_type)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        String publicKeyPEM = JCECrypto.getPEMFromRSAPublicKey(publicKey);
        final OrganizationV3DirectoryKeysPostRequest request =
                new OrganizationV3DirectoryKeysPostRequest(directoryId, publicKeyPEM, expires, active, key_type);
        final KeysPostResponse response = transport.organizationV3DirectoryKeysPost(request, organization);
        return response.getId();
    }

    @Override
    public String addDirectoryPublicKey(UUID directoryId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        String publicKeyPEM = JCECrypto.getPEMFromRSAPublicKey(publicKey);
        final OrganizationV3DirectoryKeysPostRequest request =
                new OrganizationV3DirectoryKeysPostRequest(directoryId, publicKeyPEM, expires, active);
        final KeysPostResponse response = transport.organizationV3DirectoryKeysPost(request, organization);
        return response.getId();
    }

    @Override
    public void updateDirectoryPublicKey(UUID directoryId, String keyId, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        transport.organizationV3DirectoryKeysPatch(
                new OrganizationV3DirectoryKeysPatchRequest(directoryId, keyId, expires, active), organization);
    }

    @Override
    public void removeDirectoryPublicKey(UUID directoryId, String keyId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        final OrganizationV3DirectoryKeysDeleteRequest request = new OrganizationV3DirectoryKeysDeleteRequest(directoryId, keyId);
        transport.organizationV3DirectoryKeysDelete(request, organization);
    }

    @Override
    public UUID createService(String name, String description, URI icon, URI callbackURL, Boolean active)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesPostRequest request = new ServicesPostRequest(name, icon, description, callbackURL, active);
        final ServicesPostResponse response = transport.organizationV3ServicesPost(request, organization);
        return response.getId();
    }

    @Override
    public void updateService(UUID serviceId, String name, String description, URI icon, URI callbackURL, Boolean active)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesPatchRequest request = new ServicesPatchRequest(serviceId, name, description, icon, callbackURL, active);
        transport.organizationV3ServicesPatch(request, organization);

    }

    @Override
    public Service getService(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesListPostRequest request = new ServicesListPostRequest(Collections.singletonList(serviceId));
        final ServicesListPostResponse response = transport.organizationV3ServicesListPost(request, organization);
        ServicesListPostResponseService service = response.getServices().get(0);
        return new Service(service.getId(), service.getName(), service.getDescription(), service.getIcon(),
                service.getCallbackURL(), service.isActive());
    }

    @Override
    public List<Service> getServices(List<UUID> serviceIds)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesListPostRequest request = new ServicesListPostRequest(serviceIds);
        final ServicesListPostResponse response = transport.organizationV3ServicesListPost(request, organization);
        List<Service> services = new ArrayList<>();
        for (ServicesListPostResponseService service : response.getServices()) {
            services.add(new Service(service.getId(), service.getName(), service.getDescription(), service.getIcon(),
                    service.getCallbackURL(), service.isActive()));
        }
        return services;
    }

    @Override
    public List<Service> getAllServices()
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServicesGetResponse response = transport.organizationV3ServicesGet(organization);
        List<Service> services = new ArrayList<>();
        for (ServicesGetResponseService service : response.getServices()) {
            services.add(new Service(service.getId(), service.getName(), service.getDescription(), service.getIcon(),
                    service.getCallbackURL(), service.isActive()));
        }
        return services;
    }

    @Override
    public List<PublicKey> getServicePublicKeys(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        ServiceKeysListPostRequest request = new ServiceKeysListPostRequest(serviceId);
        final KeysListPostResponse response =
                transport.organizationV3ServiceKeysListPost(request, organization);
        List<PublicKey> publicKeys = new ArrayList<>();
        for (KeysListPostResponsePublicKey publicKey : response.getPublicKeys()) {
            publicKeys.add(new PublicKey(publicKey.getId(), publicKey.isActive(), publicKey.getCreated(),
                    publicKey.getExpires(), publicKey.getKeyType()));
        }
        return publicKeys;
    }

    @Override
    public String addServicePublicKey(UUID serviceId, RSAPublicKey publicKey, Boolean active, Date expires,
                                      KeyType key_type)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        String publicKeyPEM = JCECrypto.getPEMFromRSAPublicKey(publicKey);
        final ServiceKeysPostRequest request = new ServiceKeysPostRequest(serviceId, publicKeyPEM, expires, active,
                key_type);
        final KeysPostResponse response = transport.organizationV3ServiceKeysPost(request, organization);
        return response.getId();
    }

    @Override
    public String addServicePublicKey(UUID serviceId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        String publicKeyPEM = JCECrypto.getPEMFromRSAPublicKey(publicKey);
        final ServiceKeysPostRequest request = new ServiceKeysPostRequest(serviceId, publicKeyPEM, expires, active);
        final KeysPostResponse response = transport.organizationV3ServiceKeysPost(request, organization);
        return response.getId();
    }

    @Override
    public void updateServicePublicKey(UUID serviceId, String keyId, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        transport.organizationV3ServiceKeysPatch(new ServiceKeysPatchRequest(serviceId, keyId, expires, active),
                organization);

    }

    @Override
    public void removeServicePublicKey(UUID serviceId, String keyId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        final ServiceKeysDeleteRequest request = new ServiceKeysDeleteRequest(serviceId, keyId);
        transport.organizationV3ServiceKeysDelete(request, organization);
    }

    @Override
    @Deprecated
    public ServicePolicy getServicePolicy(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        Policy policy = null;
        try {
            policy = getAdvancedServicePolicy(serviceId);
        } catch (InvalidPolicyAttributes | UnknownFenceTypeException | UnknownPolicyException e) {
            throw new InvalidResponseException("Cannot parse received policy.",e.getCause(),e.getErrorCode());
        }
        if (!(policy instanceof LegacyPolicy)) {
            Logger.getLogger("com.iovation.launchkey.sdk").severe("Received new policy type using deprecated method. Please update to use getAdvancedServicePolicy instead");
            return null;
        }
        return getServicePolicyFromLegacyPolicy((LegacyPolicy)(policy));
    }

    @Override
    @Deprecated
    public void setServicePolicy(UUID serviceId, ServicePolicy policy) throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        try {
            setAdvancedServicePolicy(serviceId, getLegacyPolicyFromServicePolicy(policy));
        } catch (UnknownFenceTypeException | UnknownPolicyException e) {
            throw new UnknownEntityException("Attempted to set an invalid policy",e.getCause(),e.getErrorCode());
        }
    }

    @Override
    public Policy getAdvancedServicePolicy(UUID serviceId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException, CommunicationErrorException,
            MarshallingError, CryptographyError, InvalidPolicyAttributes, UnknownFenceTypeException, UnknownPolicyException {

        com.iovation.launchkey.sdk.transport.domain.Policy transportPolicy =
                transport.organizationV3ServicePolicyItemPost(new ServicePolicyItemPostRequest(serviceId), organization);
        return getDomainPolicyFromTransportPolicy(transportPolicy, false);
    }

    @Override
    public void setAdvancedServicePolicy(UUID serviceId, Policy policy) throws PlatformErrorException,
            UnknownEntityException, InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError, UnknownFenceTypeException, UnknownPolicyException {
        com.iovation.launchkey.sdk.transport.domain.Policy transportPolicy = getTransportPolicyFromDomainPolicy(policy, false);
        transport.organizationV3ServicePolicyPut(new ServicePolicyPutRequest(serviceId, transportPolicy), organization);
    }

    @Override
    public void removeServicePolicy(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError {
        transport.organizationV3ServicePolicyDelete(new ServicePolicyDeleteRequest(serviceId), organization);
    }
}
