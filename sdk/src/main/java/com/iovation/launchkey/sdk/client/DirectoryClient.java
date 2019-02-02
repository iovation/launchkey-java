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

import com.iovation.launchkey.sdk.domain.directory.Device;
import com.iovation.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.iovation.launchkey.sdk.domain.directory.Session;
import com.iovation.launchkey.sdk.error.*;

import java.util.List;

public interface DirectoryClient extends ServiceManagingClient {

    /**
     * Begin the process of Linking a Subscriber Authenticator Device with an End User based on the Directory User ID.
     * If no Directory User exists for the Directory User ID, the Directory User will be created.
     *
     * @param userId Unique value identifying the End User in the your system. It is the permanent link for the
     * End User between the your application(s) and the iovation LaunchKey API. This will be used for authorization requests
     * as well as managing the End User's Devices.
     * @return A {@link DirectoryUserDeviceLinkData} Information for completing the Device Linking process with the
     * .
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    DirectoryUserDeviceLinkData linkDevice(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError;

    /**
     * Begin the process of Linking a Subscriber Authenticator Device with an End User based on the Directory User ID.
     * If no Directory User exists for the Directory User ID, the Directory User will be created.
     *
     * @param userId Unique value identifying the End User in the your system. It is the permanent link for the
     * End User between the your application(s) and the iovation LaunchKey API. This will be used for authorization requests
     * as well as managing the End User's Devices.
     * @param ttl Number of seconds the linking code returned in the response will be valid.
     * @return A {@link DirectoryUserDeviceLinkData} Information for completing the Device Linking process with the
     * .
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    DirectoryUserDeviceLinkData linkDevice(String userId, Integer ttl) throws PlatformErrorException,
            UnknownEntityException, InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError;

    /**
     * Get a list of Subscriber Authenticator Devices for a Directory User. If not Directory User exists for the
     * Directory User ID, an empty list will be returned.
     *
     * @param userId Unique value identifying the End User in the your system. This value was used to
     * create the Directory User and Link Devices with {@link #linkDevice}.
     * @return A list of {@link Device} objects for the specified user identifier.
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * @throws MarshallingError When the response cannot be marshaled
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    List<Device> getLinkedDevices(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError;

    /**
     * Unlink a users device
     *
     * @param userId Unique value identifying the End User in the your system. This value was used to
     * create the Directory User and Link Devices with {@link #linkDevice}.
     * @param deviceId The unique identifier of the Device you wish to Unlink. It would be obtained via
     * {@link Device#getId} in a {@link Device} returned by {@link #getLinkedDevices(String)}.
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    void unlinkDevice(String userId, String deviceId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError;

    /**
     * Get Service User Sessions for all Services in which a Session was started for the Directory User
     *
     * @param userId Unique value identifying the End User in the your system. This value was used to
     * create the Directory User and Link Devices with {@link #linkDevice}.
     * @return An list of {@link Session} objects for the specified user identifier.
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    List<Session> getAllServiceSessions(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError;

    /**
     * End Service User Sessions for all Services in which a Session was started for the Directory User
     *
     * @param userId Unique value identifying the End User in the your system. This value was used to
     * create the Directory User and Link Devices with {@link #linkDevice}.
     * @throws UnknownEntityException When the Platform API returns a 404 Not Found HTTP Status.
     * @throws InvalidResponseException When the response JWT is missing or does not pass validation, when the response
     * content hash does not match the value in the JWT, or when the JWE in the body fails validation, or the decrypted
     * JWE in the body cannot be parsed or mapped to the expected data.
     * @throws InvalidRequestException When the Platform API returns a 400 Bad Request HTTP Status
     * @throws InvalidCredentialsException When the Platform API returns a 401 Unauthorized or 403 Forbidden HTTP Status
     * @throws PlatformErrorException When the Platform API returns an unexpected HTTP Status
     * @throws CommunicationErrorException When the HTTP client is unable to connect to the Platform API, cannot
     * negotiate TLS with the Platform API, or is disconnected while sending or receiving a message from the
     * Platform API.
     * @throws InvalidStateException When the SDK does not have the proper resource to perform an action. This is most
     * often due to invalid dependencies being provided or algorithms not being supported by the JCE provider.
     * @throws MarshallingError When the response cannot be marshaled
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    void endAllServiceSessions(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError;
}
