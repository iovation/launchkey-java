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

package com.launchkey.sdk.transport;

import com.launchkey.sdk.error.*;
import com.launchkey.sdk.transport.domain.*;

import java.util.UUID;

public interface Transport {
    /**
     * Get the current system time from the API
     * @return Ping response transport object.
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    PublicV3PingGetResponse publicV3PingGet()
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     * Get a public key via fingerprint or the current key if fingerprint is null.
     * @param publicKeyFingerprint MD5 fingerprint of the public key to be retrieved. If null, current key is assumed.
     * @return Public Key
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    PublicV3PublicKeyGetResponse publicV3PublicKeyGet(String publicKeyFingerprint)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     * Create an authorization request
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Service entity for the subject
     * @return
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    ServiceV3AuthsPostResponse serviceV3AuthsPost(ServiceV3AuthsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     *
     * @param authRequestId Identifier for the authorization request as returned by
     * {@link #serviceV3AuthsPost(ServiceV3AuthsPostRequest, EntityIdentifier)}
     * @param subject Service entity for the subject
     * @return
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    ServiceV3AuthsGetResponse serviceV3AuthsGet(UUID authRequestId, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException, AuthorizationRequestTimedOutError;

    /**
     * Begin a user service session which will optionally be associated with an authorization request.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Service entity for the subject
     * @throws CommunicationErrorException When an error occurred during communication with the API.
     * @throws InvalidResponseException When the response fom the API is either a non-20x error or the response is not parsable.
     */
    void serviceV3SessionsPost(ServiceV3SessionsPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     * End a user service session. If a session does not exists, no error is raised.
     *
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Service entity for the subject
     * @throws CommunicationErrorException When an error occurred during communication with the API.
     * @throws InvalidResponseException When the response fom the API is either a non-20x error or the response is not parsable.
     */
    void serviceV3SessionsDelete(ServiceV3SessionsDeleteRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     * Begin the device linking process for a specific Directory.
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    DirectoryV3DevicesPostResponse directoryV3DevicesPost(DirectoryV3DevicesPostRequest request, EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     * Get a list of devices for a Directory User based on the provided request data.
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @return
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    DirectoryV3DevicesListPostResponse directoryV3DevicesListPost(
            DirectoryV3DevicesListPostRequest request,
            EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;

    /**
     * Unlink a device for a Directory User based on the provided request data.
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    void directoryV3devicesDelete(
            DirectoryV3DevicesDeleteRequest request,
            EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;


    /**
     * End all Sevice Sessions for a Directory User based on the provided request data.
     * @param request Transport object with information that will be marshaled for the request.
     * @param subject Directory entity for the subject
     * @throws CommunicationErrorException
     * @throws InvalidResponseException
     * @throws MarshallingError
     * @throws CryptographyError
     * @throws InvalidRequestException
     * @throws InvalidCredentialsException
     */
    void directoryV3SessionsDelete(
            DirectoryV3SessionsDeleteRequest request,
            EntityIdentifier subject)
            throws CommunicationErrorException, InvalidResponseException, MarshallingError, CryptographyError,
            InvalidRequestException, InvalidCredentialsException;
}
