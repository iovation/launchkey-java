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

import com.iovation.launchkey.sdk.domain.service.*;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.error.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("DuplicateThrows")
public interface ServiceClient extends WebhookHandlingClient {
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
     * @throws AuthorizationInProgress When an authorization request already exists for the provided user
     * @deprecated in favor of {@link #createAuthorizationRequest(String, String, AuthPolicy, String, Integer)}
     */
    @Deprecated
    String authorize(String user, String context, AuthPolicy policy)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
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
     * @throws AuthorizationInProgress When an authorization request already exists for the provided user
     * @see AuthorizationResponse#getAuthorizationRequestId()
     * @deprecated in favor of {@link #createAuthorizationRequest(String, String)}
     */
    @Deprecated
    String authorize(String user, String context)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
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
     * @throws AuthorizationInProgress When an authorization request already exists for the provided user
     * @see AuthorizationResponse#getAuthorizationRequestId()
     * @deprecated in favor of {@link #createAuthorizationRequest(String)}
     */
    @Deprecated
    String authorize(String user)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Create an authorization request for the provided user.  This will NOT
     * begin a user session.
     *
     * @param userIdentifier LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param context Arbitrary string of data up to 400 characters to be presented to the End User during
     * authorization to provide context regarding the individual authorization request
     * @param policy Authorization policy override for this authorization request. The policy can only increase the security
     * level any existing policy in the Service Profile. It can never reduce the security level of the Service Profile's
     * policy.
     * @param title String of data up to 200 characters to be presented to the End User during
     * authorization as the title of the individual authorization request
     * @param ttl Time for this authorization request to be valid. If no value is provided, the system default will be used.
     * @param pushTitle Title for push notification. This feature is only available for Directory Services. This value
     * will not have any effect if 3rd party push notification is implemented.
     * @param pushBody Body of push notification. This feature is only available for Directory Services. This value will
     * not have any effect if 3rd party push notification is implemented.
     * @param denialReasons List of denial reasons to present to the user if they deny the request. This list must
     * include at least two items. At least one of the items must have a fraud value of false and at least one of the
     * items must have a fraud value of true. If null is provided, the defaults will be used. If a list is provided
     * and denial context inquiry is not enabled for the Directory, this request will error. This feature is only
     * available for Directory Services.
     * @return Information regarding the authorization request.
     * @throws CommunicationErrorException If there was an error communicating with the LaunchKey API
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     * @throws AuthorizationInProgress When an authorization request already exists for the provided userIdentifier
     */
    AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy,
                                                    String title, Integer ttl, String pushTitle, String pushBody,
                                                    List<DenialReason> denialReasons)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Create an authorization request for the provided user.  This will NOT
     * begin a user session.
     *
     * @param userIdentifier LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param context Arbitrary string of dataServiceV3AuthsPostRequestTest up to 400 characters to be presented to the End User during
     * authorization to provide context regarding the individual authorization request
     * @param policy Authorization policy override for this authorization request. The policy can only increase the security
     * level any existing policy in the Service Profile. It can never reduce the security level of the Service Profile's
     * policy.
     * @param title String of data up to 200 characters to be presented to the End User during
     * authorization as the title of the individual authorization request
     * @param ttl Time for this authorization request to be valid. If no value is provided, the system default will be used.
     * @return Information regarding the authorization request.
     * @throws CommunicationErrorException If there was an error communicating with the LaunchKey API
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     * @throws AuthorizationInProgress When an authorization request already exists for the provided userIdentifier
     */
    AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy, String title, Integer ttl)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Create an authorization request for the provided user.  This will NOT
     * begin a user session.
     *
     * @param userIdentifier LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param context Arbitrary string of data up to 400 characters to be presented to the End User during
     * authorization to provide context regarding the individual authorization request
     * @param policy Authorization policy override for this authorization request. The policy can only increase the security
     * level any existing policy in the Service Profile. It can never reduce the security level of the Service Profile's
     * policy.
     * @return Information regarding the authorization request.
     * @throws CommunicationErrorException If there was an error communicating with the LaunchKey API
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     * @throws AuthorizationInProgress When an authorization request already exists for the provided userIdentifier
     */
    AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context, AuthPolicy policy)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;


    /**
     * Create an authorization request for the provided user.  This will NOT
     * begin a user session.
     *
     * @param userIdentifier LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @param context Arbitrary string of data up to 400 characters to be presented to the End User during
     * authorization to provide context regarding the individual authorization request
     * level any existing policy in the Service Profile. It can never reduce the security level of the Service Profile's
     * policy.
     * @return Information regarding the authorization request.
     * @throws CommunicationErrorException If there was an error communicating with the LaunchKey API
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     * @throws AuthorizationInProgress When an authorization request already exists for the provided userIdentifier
     */
    AuthorizationRequest createAuthorizationRequest(String userIdentifier, String context)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;


    /**
     * Create an authorization request for the provided user.  This will NOT
     * begin a user session.
     *
     * @param userIdentifier LaunchKey Username, User Push ID, or Directory User ID for the End User
     * @return Information regarding the authorization request.
     * @throws CommunicationErrorException If there was an error communicating with the LaunchKey API
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * the signature of the response
     */
    AuthorizationRequest createAuthorizationRequest(String userIdentifier)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Cancel an existing authorization request
     * @param authorizationRequestId Identifier of the authorization request to cancel
     * @throws EntityNotFound When the authorization request does not exist.
     * @throws AuthorizationRequestCanceled When the authorization request has been previously canceled.
     * @throws AuthorizationResponseExists When the authorization request has been responded to and can no longer
     * be canceled.
     */
    void cancelAuthorizationRequest(String authorizationRequestId)
            throws EntityNotFound, AuthorizationRequestCanceled, AuthorizationResponseExists,
            CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;

    /**
     * Request the response for a previous authorization call.
     *
     * @param authorizationRequestId Unique identifier returned by {@link #authorize(String)}
     * @return Null is returned if the user has not responded otherwise an AuthResponse is returned with the data for
     * that decision
     * @throws AuthorizationRequestTimedOutError When the user did not respond to the authorization request in the
     * allotted time.
     * @throws AuthorizationRequestCanceled When the authorization request has been canceled.
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     * @deprecated Use {@link #getAdvancedAuthorizationResponse(String)} instead
     */
    @Deprecated
    AuthorizationResponse getAuthorizationResponse(String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, AuthorizationRequestTimedOutError, NoKeyFoundException,
            AuthorizationRequestCanceled;

    /**
     * Request the response for a previous authorization call.
     *
     * @param authorizationRequestId Unique identifier returned by {@link #authorize(String)}
     * @return Null is returned if the user has not responded otherwise an AuthResponse is returned with the data for
     * that decision
     * @throws AuthorizationRequestTimedOutError When the user did not respond to the authorization request in the
     * allotted time.
     * @throws AuthorizationRequestCanceled When the authorization request has been canceled.
     * @throws NoKeyFoundException When the Entity and Key ID identifying the public key used to encrypt authorization
     * response is not found in the known keys mapping.
     * @throws CommunicationErrorException If there was an error communicating with the endpoint
     * @throws MarshallingError If there was an error marshalling the request or un-marshalling the response
     * @throws InvalidRequestException When the LaunchKey API responds with an error in the request data
     * @throws InvalidResponseException When the response received cannot be processed
     * @throws InvalidCredentialsException When the credentials supplied are not valid
     * @throws CryptographyError When there is an error encrypting and signing the request or decrypting and verifying
     */
    AdvancedAuthorizationResponse getAdvancedAuthorizationResponse(String authorizationRequestId)
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError, AuthorizationRequestTimedOutError, NoKeyFoundException,
            AuthorizationRequestCanceled;

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
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
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
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
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
            throws CommunicationErrorException, MarshallingError, InvalidResponseException,
            InvalidCredentialsException, CryptographyError;
}
