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

package com.launchkey.sdk.client;

import com.launchkey.sdk.domain.service.AuthPolicy;
import com.launchkey.sdk.domain.service.AuthorizationResponse;
import com.launchkey.sdk.domain.sse.AuthorizationResponseServerSentEventPackage;
import com.launchkey.sdk.domain.sse.ServerSentEventPackage;
import com.launchkey.sdk.domain.sse.ServiceUserSessionServerSentEventPackage;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.service.DirectoryService;
import com.launchkey.sdk.service.ServiceService;

import java.util.List;
import java.util.Map;

public interface ServiceClient {
    /**
     * Authorize a transaction for the provided user.  This getServiceService method would be utilized if you are using this
     * as a secondary factor for user login or authorizing a single transaction within your application.  This will NOT
     * begin a user session.
     *
     * @param user LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param context Arbitrary string of data up to 400 characters to be presented to the End User during
     * authorization to provide context regarding the individual request
     * @param policy Authorization policy override for this authorization. The policy can only increase the security
     * level any existing policy in the Service Profile. It can never reduce the security level of the Service Profile's
     * policy.
     * @return Unique identifier for tracking status of the authorization request
     * @throws CommunicationErrorException If there was an error communicating with the LaunchKey API
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    public String authorize(String user, String context, AuthPolicy policy)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Authorize a transaction for the provided user.  This getServiceService method would be utilized if you are using this
     * as a secondary factor for user login or authorizing a single transaction within your application.  This will NOT
     * begin a user session.
     *
     * @param user LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param context Arbitrary string of data up to 400 characters to be presented to the End User during
     * authorization to provide context regarding the individual request
     * @return Unique identifier for tracking status of the authorization request
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @see AuthorizationResponse#getAuthorizationRequestId()
     */
    String authorize(String user, String context)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Authorize a transaction for the provided username.  This getServiceService method would be utilized if you are using this
     * as a secondary factor for user login or authorizing a single transaction within your application.  This will NOT
     * begin a user session.
     *
     * @param user LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @return Unique identifier for tracking status of the authorization request
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @see AuthorizationResponse#getAuthorizationRequestId()
     */
    String authorize(String user)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Request the response for a previous athorization call.
     *
     * @param authorizationRequestId Unique identifier returned by {@link #authorize(String)}
     * @return Null is returned if the user has not responded otherwise an AuthResponse is returned with the data for
     * that decision
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    AuthorizationResponse getAuthorizationResponse(String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, AuthorizationRequestTimedOutError, NoKeyFoundException;

    /**
     * Request to start a Service Session for the End User which was derived from a authorization request
     *
     * @param user LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param authorizationRequestId Unique identifier returned by {@link #authorize(String)}
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void sessionStart(String user, String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Request to start a Service Session for the End User
     *
     * @param user LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void sessionStart(String user)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Request to end a Service Session for the End User
     *
     * @param user LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    void sessionEnd(String user)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Handle a server side event callback
     * <p>
     * In the event of a DeOrbit callback, be sure to call {@link ServiceService#sessionEnd(String)} when you complete the
     * process of ending the user's session in your implementation.  This will remove the corresponding Application from
     * the authorization list on all of the the user's mobile devices.
     *
     * @param headers A generic map of response headers. These will be used to access and validate the JWT
     * @return Returns a {@link ServiceUserSessionServerSentEventPackage} when the server sent event was initialed from the
     * user logging out from a linked mobile device, a {@link DirectoryService#endAllServiceSessions(String)} request, or an
     * {@link AuthorizationResponseServerSentEventPackage} when the server sent event was initiated by the user
     * responding to a {@link ServiceService#authorize(String)} request.
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    ServerSentEventPackage handleServerSentEvent(Map<String, List<String>> headers, String body)
            throws CommunicationErrorException, MarshallingError, InvalidRequestException, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, NoKeyFoundException;
}
