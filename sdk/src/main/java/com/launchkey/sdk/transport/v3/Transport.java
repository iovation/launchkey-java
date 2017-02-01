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

package com.launchkey.sdk.transport.v3;

import com.launchkey.sdk.error.*;
import com.launchkey.sdk.transport.v3.domain.*;

/**
 * Interface for interacting with the Platform API v3
 */
public interface Transport {

    /**
     * Link a device with a White Label User
     *
     * @param request The request data
     * @return Information required to complete the device link
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
     */
    WhiteLabelDeviceAddResponse whiteLabelUserDeviceAdd(WhiteLabelDeviceAddRequest request)
            throws PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidStateException,
            InvalidResponseException, InvalidCredentialsException, CommunicationErrorException;

    /**
     * Unlink a device from a White Label User
     *
     * @param request The request data
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
     */
    void whiteLabelUserDeviceDelete(WhiteLabelDeviceDeleteRequest request)
            throws PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidStateException,
            InvalidResponseException, InvalidCredentialsException, CommunicationErrorException;

    /**
     * Get a list of devices for a White Label User
     *
     * @param request The request data
     * @return List of devices
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
     */
    Device[] whiteLabelUserDeviceList(WhiteLabelDeviceListRequest request)
            throws PlatformErrorException, UnknownEntityException, InvalidRequestException, InvalidStateException,
            InvalidResponseException, InvalidCredentialsException, CommunicationErrorException;


}

