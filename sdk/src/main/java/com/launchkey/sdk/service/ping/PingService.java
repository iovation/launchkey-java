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

package com.launchkey.sdk.service.ping;

import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.error.PlatformErrorException;
import com.launchkey.sdk.error.InvalidStateException;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

/**
 * Service for providing interaction with the Platform API ping service
 */
public interface PingService {

    /**
     * Get the current RSA Public key from the Platform API. This value may cached.
     *
     * @return RSA Public Key for the Platform API
     * @throws CommunicationErrorException When errors occur connecting to the Platform API
     * @throws InvalidResponseException When the response received from the Platform API is not parsable.
     * @throws InvalidStateException When the stats of the SDK client does no allow for the request to be made to the
     * Platform API.
     * @throws PlatformErrorException When the platform API returns a error status code.
     */
    RSAPublicKey getPublicKey()
            throws CommunicationErrorException, InvalidResponseException, InvalidStateException, PlatformErrorException;

    /**
     * Get the current date/time as reported by the Platform API. This value is used for Platform API calls toi ensure
     * that Platform API calls using timestamps do not fail due to the system time not being in sync with the
     * Platform API.
     *
     * @return Current date/time of the  Platform API
     * @throws CommunicationErrorException When errors occur connecting to the Platform API
     * @throws InvalidResponseException When the response received from the Platform API is not parsable.
     * @throws InvalidStateException When the stats of the SDK client does no allow for the request to be made to the
     * Platform API.
     * @throws PlatformErrorException When the platform API returns a error status code.
     */
    Date getPlatformTime()
            throws CommunicationErrorException, InvalidResponseException, InvalidStateException, PlatformErrorException;

}
