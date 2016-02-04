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

package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.InvalidCallbackException;
import com.launchkey.sdk.service.error.LaunchKeyException;

import java.util.Map;

public interface AuthService {
    /**
     * Authorize a transaction for the provided username.  This auth method would be utilized if you are using LaunchKey
     * as a secondary factor for user login or authorizing a single transaction within your application.  This will NOT
     * begin a user session.
     *
     * @param username LaunchKey username, user push ID, or white label user identifier for the user being authenticated
     * @return Unique identifier for tracking status of the authorization request
     * @see AuthService#logout(String)
     * @see AuthService#getAuthResponse(String)
     * @see AuthResponse#getAuthRequestId()
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     */
    String authorize(String username) throws LaunchKeyException;

    /**
     * Request a login for the provided username.
     *
     * @param username LaunchKey username, user push ID, or white label user identifier for the user being authenticated
     * @return Unique identifier for tracking status of the authorization request
     * @throws LaunchKeyException when an error occurs in the request
     */
    String login(String username) throws LaunchKeyException;

    /**
     * Request that the session started with {@link AuthService#login(String)} be terminated
     *
     * @param authRequestId Unique identifier returned by {@link AuthService#login(String)}
     * @throws LaunchKeyException when an error occurs in the request
     */
    void logout(String authRequestId) throws LaunchKeyException;

    /**
     * Request the response for a previous {@link AuthService#login(String)} or {@link AuthService#authorize(String)}
     * call.
     * @param authRequestId Unique identifier returned by {@link AuthService#login(String)}
     * @return Null is returned if the user has not responded otherwise an AuthResponse is returned with the data
     * for that decision
     * @throws LaunchKeyException when an error occurs in the request
     */
    AuthResponse getAuthResponse(String authRequestId) throws LaunchKeyException;

    /**
     * Handle a server side event callback
     *
     * In the event of a DeOrbit callback, be sure to call {@link AuthService#logout(String)} when you complete the
     * process of ending the user's session in your implementation.  This will remove the corresponding rocket from
     * the orbit list on all of the the user's mobile devices.
     *
     * @param callbackData A hash map of key value pairs from the query string of a Server Sent Event callback
     * @param signatureTimeThreshold The maximum number of seconds after the "launchkey_time" provided with a
     *                               server sent event that will be allowed to be processed without error.  If the
     *                               differential from the current system time is greater than the threshold, a
     *                               InvalidCallbackException will be thrown.
     * @return Returns a {@link DeOrbitCallbackResponse} when the server sent event was initialed from the user
     * de-orbiting, logging out, from a paired mobile device or an {@link AuthResponseCallbackResponse} when the
     * server sent event was initiated by the user responding to a {@link AuthService#login(String)} or
     * {@link AuthService#authorize(String)} request.
     * @throws InvalidCallbackException When there is an issue with the callback data
     * @throws LaunchKeyException when an error occurs in the request
     */
    CallbackResponse handleCallback(Map<String, String> callbackData, int signatureTimeThreshold) throws LaunchKeyException;
}
