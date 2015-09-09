/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1;

import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.LaunchKeyException;
import com.launchkey.sdk.transport.v1.domain.*;

/**
 * Interface for interacting with the LaunchKey API v1
 */
public interface Transport {

    /**
     * Send a ping request
     * @param request Ping request data
     * @return Ping response
     * @throws LaunchKeyException When an error occurs processing the request
     */
    PingResponse ping(PingRequest request) throws LaunchKeyException;

    /**
     * Send an auths request
     * @param request Auths request data
     * @return Auths response
     * @throws LaunchKeyException When an error occurs processing the request
     */
    AuthsResponse auths(AuthsRequest request) throws LaunchKeyException;

    /**
     * Send a poll request
     * @param request Poll request data
     * @return Poll response data
     * @throws LaunchKeyException When an error occurs processing the request
     */
    PollResponse poll(PollRequest request) throws LaunchKeyException;

    /**
     * Perform a logs request
     * @param request Logs request data
     * @throws LaunchKeyException When an error occurs processing the request
     */
    void logs(LogsRequest request) throws LaunchKeyException;

    /**
     * Perform a users request
     * @param request Users request data
     * @return Users response data
     * @throws LaunchKeyException When an error occurs processing the request
     */
    UsersResponse users(UsersRequest request) throws LaunchKeyException;
}
