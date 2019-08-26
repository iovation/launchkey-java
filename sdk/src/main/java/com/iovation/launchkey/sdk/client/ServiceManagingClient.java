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
import com.iovation.launchkey.sdk.domain.policy.PolicyAdapter;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.error.*;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ServiceManagingClient {
    /**
     * Create a new Service
     *
     * @param name Name of the Service. This value will be visible to the user during the Auth Request as well as in
     * the Session list.
     * @param description Description of the
     * @param icon The icon to use with the service
     * @param callbackURL The callback URL for webhooks related to the Service.
     * @param active Should the Service created be active
     * @return ID of the Service created
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
    UUID createService(String name, String description, URI icon, URI callbackURL, Boolean active)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Update a Service
     *
     * @param serviceId ID of the Service you wish to update
     * @param name Name of the Service. This value will be visible to the user during the Auth Request as well as in
     * the Session list.
     * @param description Description of the
     * @param icon The icon to use with the service
     * @param callbackURL The callback URL for webhooks related to the Service.
     * @param active Should the Service created be active
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
    void updateService(UUID serviceId, String name, String description, URI icon, URI callbackURL, Boolean active)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Get a Service
     *
     * @param serviceId ID of the Service you wish to get
     * @return Service with the ID provided
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
    Service getService(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;


    /**
     * Get a list of Services
     *
     * @param serviceIds List of IDs of the Services you wish to get
     * @return Services with the IDs provided
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
    List<Service> getServices(List<UUID> serviceIds)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;


    /**
     * Get all Services
     *
     * @return All Services
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
    List<Service> getAllServices()
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Get a list of Public Keys for a Service
     *
     * @param serviceId ID of the Service you which you wish to retrieve Public Keys
     * @return Public Keys for the Service whose ID was provided
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
    List<PublicKey> getServicePublicKeys(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Add a Public Key for a Service
     *
     * @param serviceId ID of the Service for which the Public Key belongs
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
    String addServicePublicKey(UUID serviceId, RSAPublicKey publicKey, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Update a Public Key for a Service
     *
     * @param serviceId ID of the Service for which the Public Key belongs
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
    void updateServicePublicKey(UUID serviceId, String keyId, Boolean active, Date expires)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Remove a Public Key from a Service. You may not remove the only Public Key from a Service. You may use
     * {@link #updateServicePublicKey(UUID, String, Boolean, Date)} to mark the key as inactive if it should no longer
     * be able to be used.
     *
     * @param serviceId ID of the Service for which the Public Key belongs
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
    void removeServicePublicKey(UUID serviceId, String keyId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Get the dDefault authorization policy for the Service
     *
     * @param serviceId ID of the Service you which you wish to retrieve Public Keys
     * @return Public Keys for the Service whose ID was provided
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
    PolicyAdapter getServicePolicy(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Update the default authorization policy for a Service
     *
     * @param serviceId ID of the Service for which the Public Key belongs
     * @param policy Default authorization policy for the Service
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
    void setServicePolicy(UUID serviceId, PolicyAdapter policy)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;

    /**
     * Remove the dDefault authorization policy for a Service
     *
     * @param serviceId ID of the Service for which the Public Key belongs
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
    void removeServicePolicy(UUID serviceId)
            throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError,
            CryptographyError;
}
