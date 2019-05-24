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

import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.organization.Directory;
import com.iovation.launchkey.sdk.error.*;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OrganizationClient extends ServiceManagingClient {
    /**
     * Create a new Directory
     *
     * @param name Name of the Service. This value will be visible to the user during the Auth Request as well as in
     * the Session list.
     * @return ID of the Directory created
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    UUID createDirectory(String name)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Update a Directory
     *
     * @param directoryId ID of the Directory you wish to update
     * @param active Should the Directory be active
     * @param androidKey GCM push key
     * @param iosP12 APNS push certificate in .P12 format that has been Base64 Encoded
     * @param denialContextInquiryEnabled Should the user be prompted for denial context when they deny authorization
     * requests for any and all child services.
     * @param callbackUrl Callback URL for Directory level webhooks
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    void updateDirectory(UUID directoryId, Boolean active, String androidKey, String iosP12,
                         Boolean denialContextInquiryEnabled, URI callbackUrl)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
                         InvalidCredentialsException, CommunicationErrorException, MarshallingError,
                         CryptographyError;

    /**
     * Update a Directory
     *
     * @param directoryId ID of the Directory you wish to update
     * @param active Should the Directory be active
     * @param androidKey GCM push key
     * @param iosP12 APNS push certificate in .P12 format that has been Base64 Encoded
     * @param denialContextInquiryEnabled Should the user be prompted for denial context when they deny authorization
     * requests for any and all child services.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     * @deprecated Please use {@link #updateDirectory(UUID, Boolean, String, String, Boolean, URI)}
     */
    @Deprecated
    void updateDirectory(UUID directoryId, Boolean active, String androidKey, String iosP12, Boolean denialContextInquiryEnabled)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;


    /**
     * Update a Directory
     *
     * @param directoryId ID of the Directory you wish to update
     * @param active Should the Directory be active
     * @param androidKey GCM push key
     * @param iosP12 APNS push certificate in .P12 format that has been Base64 Encoded
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     * @deprecated Please use {@link #updateDirectory(UUID, Boolean, String, String, Boolean, URI)}
     */
    @Deprecated
    void updateDirectory(UUID directoryId, Boolean active, String androidKey, String iosP12)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Get a Directory
     *
     * @param directoryId ID of the Directory you wish to get
     * @return Directory with the ID provided
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    Directory getDirectory(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;


    /**
     * Get a list of Directories
     *
     * @param directoryIds List of IDs of the Services you wish to get
     * @return Directories with the IDs provided
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    List<Directory> getDirectories(List<UUID> directoryIds)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Get all Directories
     *
     * @return All Directories for the Organization
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    List<Directory> getAllDirectories()
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Request the Platform API to generate a new Authenticator SDK Key and add to the Directory identified by the
     * provided Directory ID. One generated and added, it will be returned as the response.
     *
     * @param directoryId ID of the Directory you wish to generate and add an Authenticator SDK Key
     * @return The new Authenticator SDK Key that was generated and added to the Directory
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    UUID generateAndAddDirectorySdkKey(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Request the provided Authenticator SDK Key from the Directory identified by the provided Directory ID.
     *
     * @param directoryId ID of the Directory you wish to generate and add an Authenticator SDK Key
     * @param sdkKey The Authenticator SDK Key to remove
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    void removeDirectorySdkKey(UUID directoryId, UUID sdkKey)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Get all Authenticator SDK Keys for the Directory identified by the provided Directory ID.
     *
     * @param directoryId ID of the Directory you wish to generate and add an Authenticator SDK Key
     * @return All Authenticator SDK Keys for the Directory
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    List<UUID> getAllDirectorySdkKeys(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;
    /**
     * Get a list of Public Keys for a Directory
     *
     * @param directoryId ID of the Directory for which you wish to retrieve Public Keys
     * @return Public Keys for the Directory whose ID was provided
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    List<PublicKey> getDirectoryPublicKeys(UUID directoryId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Add a Public Key for a Directory
     *
     * @param directoryId ID of the Directory for which you wish to add a Public Key
     * @param publicKey RSA Public key to be added
     * @param active Will the Public Key be active upon creation
     * @param expires When will the Public Key expire
     * @return Key ID for the created key. This will be used to identify the key in subsequent
     * calls for this Public Key.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    String addDirectoryPublicKey(UUID directoryId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Update a Public Key for a Directory
     *
     * @param directoryId ID of the Directory for which you wish to update the Public Key
     * @param keyId MD5 fingerprint of the RSA public key used to identify the Public Key
     * @param active Will the Public Key be active
     * @param expires When will the Public Key expire
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    void updateDirectoryPublicKey(UUID directoryId, String keyId, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Remove a Public Key from a Service. You may not remove the only Public Key from a Service. You may use
     * {@link #updateServicePublicKey(UUID, String, Boolean, Date)} to mark the key as inactive if it should no longer
     * be able to be used.
     *
     * @param directoryId ID of the Directory for which you wish to remove the Public Key
     * @param keyId MD5 fingerprint of the RSA public key used to identify the Public Key
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    void removeDirectoryPublicKey(UUID directoryId, String keyId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;
}