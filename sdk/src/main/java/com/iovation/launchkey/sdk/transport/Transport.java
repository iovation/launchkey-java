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

package com.iovation.launchkey.sdk.transport;

import com.iovation.launchkey.sdk.error.*;
import com.iovation.launchkey.sdk.transport.domain.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface Transport {
    /**
     * Get the current system time from the API
     *
     * @return Ping response transport object.
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    PublicV3PingGetResponse publicV3PingGet()
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * Get a public key via fingerprint or the current key if fingerprint is null.
     *
     * @param publicKeyFingerprint MD5 fingerprint of the public key to be retrieved. If null, current key is assumed.
     * @return Public Key
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    PublicV3PublicKeyGetResponse publicV3PublicKeyGet(String publicKeyFingerprint)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * Create an authorization request
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Service entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServiceV3AuthsPostResponse serviceV3AuthsPost(ServiceV3AuthsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * @param authRequestId Identifier for the authorization request as returned by
     * {@link #serviceV3AuthsPost(ServiceV3AuthsPostRequest, EntityIdentifier)}
     * @param subject Service entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @throws AuthorizationRequestTimedOutError When the user did not respond to the authorization request in the
     * allotted time.
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     */
    ServiceV3AuthsGetResponse serviceV3AuthsGet(UUID authRequestId, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException, AuthorizationRequestTimedOutError,
            NoKeyFoundException;

    /**
     * Begin a user service session which will optionally be associated with an authorization request.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Service entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void serviceV3SessionsPost(ServiceV3SessionsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * End a user service session. If a session does not exists, no error is raised.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Service entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void serviceV3SessionsDelete(ServiceV3SessionsDeleteRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * Begin the device linking process for a specific Directory.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    DirectoryV3DevicesPostResponse directoryV3DevicesPost(DirectoryV3DevicesPostRequest request,
                                                          EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * Get a list of devices for a Directory User based on the provided request data.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    DirectoryV3DevicesListPostResponse directoryV3DevicesListPost(
            DirectoryV3DevicesListPostRequest request,
            EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * Unlink a device for a Directory User based on the provided request data.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3devicesDelete(
            DirectoryV3DevicesDeleteRequest request,
            EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;

    /**
     * Get all Service Sessions for a Directory User based on the provided request data.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    DirectoryV3SessionsListPostResponse directoryV3SessionsListPost(DirectoryV3SessionsListPostRequest request,
                                                                    EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * End all Service Sessions for a Directory User based on the provided request data.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3SessionsDelete(
            DirectoryV3SessionsDeleteRequest request,
            EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidCredentialsException;


    /**
     * Create a Service for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicesPostResponse directoryV3ServicesPost(ServicesPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Update a Service for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3ServicesPatch(ServicesPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get a list of specific Services for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicesListPostResponse directoryV3ServicesListPost(ServicesListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Services for a Directory
     *
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicesGetResponse directoryV3ServicesGet(EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Add a key for a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    KeysPostResponse directoryV3ServiceKeysPost(ServiceKeysPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Public Keys for a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    KeysListPostResponse directoryV3ServiceKeysListPost(ServiceKeysListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Remove a key from a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3ServiceKeysDelete(ServiceKeysDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Remove a key from a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3ServiceKeysPatch(ServiceKeysPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Set the default Policy for a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3ServicePolicyPut(ServicePolicyPutRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get the default Policy of a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The default Policy for the Directory Service
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicePolicy directoryV3ServicePolicyItemPost(ServicePolicyItemPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Delete the default Policy of a Directory Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void directoryV3ServicePolicyDelete(ServicePolicyDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Process a server sent event (SSE)
     *
     * @param headers Request headers
     * @param body Request body
     * @return The decrypted callback domain entity
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the authorization response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     *
     * @deprecated This method is deprecated and replaced with the overloaded method
     * {@link #handleServerSentEvent(Map, String, String, String)}
     */
    @Deprecated
    ServerSentEvent handleServerSentEvent(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException;

    /**
     * Process a server sent event (SSE)
     *
     * @param headers Request headers
     * @param method Request method
     * @param path Request path
     * @param body Request body
     * @return The decrypted callback domain entity
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the authorization response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     */
    ServerSentEvent handleServerSentEvent(Map<String, List<String>> headers, String method, String path, String body)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException;

    /**
     * Create a Directory for an Organization
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    OrganizationV3DirectoriesPostResponse organizationV3DirectoriesPost(OrganizationV3DirectoriesPostRequest request,
                                                                        EntityIdentifier subject)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Update an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3DirectoriesPatch(OrganizationV3DirectoriesPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Directories of an Organization
     *
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    OrganizationV3DirectoriesGetResponse organizationV3DirectoriesGet(EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get a list of Directories based on the provided Directory IDs
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    OrganizationV3DirectoriesListPostResponse organizationV3DirectoriesListPost(
            OrganizationV3DirectoriesListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;


    /**
     * Add a key to a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    KeysPostResponse organizationV3DirectoryKeysPost(OrganizationV3DirectoryKeysPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Public Keys for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    KeysListPostResponse organizationV3DirectoryKeysListPost(OrganizationV3DirectoryKeysListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Update a key for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3DirectoryKeysPatch(OrganizationV3DirectoryKeysPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Remove a key from a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3DirectoryKeysDelete(OrganizationV3DirectoryKeysDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Create an Authenticator SDK Key for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    OrganizationV3DirectorySdkKeysPostResponse organizationV3DirectorySdkKeysPost(
            OrganizationV3DirectorySdkKeysPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Remove an Authenticator SDK Key for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3DirectorySdkKeysDelete(OrganizationV3DirectorySdkKeysDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Authenticator SDK Keys for a Directory
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    OrganizationV3DirectorySdkKeysListPostResponse organizationV3DirectorySdkKeysListPost(OrganizationV3DirectorySdkKeysListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;


    /**
     * Create a Service for an Organization
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicesPostResponse organizationV3ServicesPost(ServicesPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Update a Service for an Organization
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3ServicesPatch(ServicesPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get a list of specific Services for an Organization
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicesListPostResponse organizationV3ServicesListPost(ServicesListPostRequest request,
                                                            EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Services for an Organization
     *
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicesGetResponse organizationV3ServicesGet(EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get all Public Keys for an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    KeysListPostResponse organizationV3ServiceKeysListPost(ServiceKeysListPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;


    /**
     * Add a key to an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The response from the LaunchKey API
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    KeysPostResponse organizationV3ServiceKeysPost(ServiceKeysPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Create a key for an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3ServiceKeysPatch(ServiceKeysPatchRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Remove a key from an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3ServiceKeysDelete(ServiceKeysDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;


    /**
     * Set the default Policy for an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3ServicePolicyPut(ServicePolicyPutRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Get the default Policy of an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @return The default Policy for the Organization Service
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServicePolicy organizationV3ServicePolicyItemPost(ServicePolicyItemPostRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;

    /**
     * Delete the default Policy of an Organization Service
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Organization entity for the subject
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void organizationV3ServicePolicyDelete(ServicePolicyDeleteRequest request, EntityIdentifier subject)
            throws CryptographyError, InvalidResponseException, CommunicationErrorException, MarshallingError,
            InvalidCredentialsException;
}