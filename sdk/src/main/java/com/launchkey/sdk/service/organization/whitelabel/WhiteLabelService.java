/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.organization.whitelabel;

import com.launchkey.sdk.domain.Device;
import com.launchkey.sdk.domain.WhiteLabelDeviceLinkData;
import com.launchkey.sdk.error.*;

import java.util.List;

/**
 * Interface for services providing functionality around White Label Groups
 */
public interface WhiteLabelService {

    /**
     * Link a device with a White Label user.  If the user does not exist in the White Label Group, the user will
     * be created.
     *
     * @param identifier Permanent and unique identifier of this user within your application.
     * This identifier will be used authenticate the user as well as link additional devices to
     * the user's account within your white label group.
     * @return A {@link WhiteLabelDeviceLinkData} object containing information for completing the device link process.
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
    WhiteLabelDeviceLinkData linkDevice(String identifier) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidRequestException, InvalidCredentialsException,
            CommunicationErrorException;

    /**
     * Get a list of devices for a White Label user.
     *
     * @param identifier Permanent and unique identifier of this user within your application. This value was used to
     * create the user and link devices with {@link #linkDevice}.
     * @return An list of {@link Device} objects for the specified user identifier.
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
    List<Device> getLinkedDevices(String identifier) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidRequestException, InvalidCredentialsException,
            CommunicationErrorException;

    /**
     * Unlink a users device
     *
     * @param identifier Permanent and unique identifier of this user within your application. This value was used to
     * create the user and link devices with {@link #linkDevice}.
     * @param deviceName The name of the device you wish to unlink.
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
    void unlinkDevice(String identifier, String deviceName) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidRequestException, InvalidCredentialsException,
            CommunicationErrorException;
}
