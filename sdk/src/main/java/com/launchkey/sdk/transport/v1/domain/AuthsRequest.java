/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain;

import com.launchkey.sdk.transport.v1.domain.Policy.Policy;

/**
 * Data for an "auths" request
 */
public class AuthsRequest {

    /**
     * Platform username, user push ID, or white label user identifier for the user being authenticated
     */
    private final String username;

    /**
     * Application Key of your Application. This is found on the Keys tab of your Application details in the Dashboard.
     */
    private final long appKey;

    /**
     * Base64 encoded secret JSON string containing these attributes:
     *      secret:   Secret Key of the Application whose key is included in the current request.
     *      stamped:  Platform formatted Date representing the current time of the request.
     */
    private final String secretKey;

    /**
     * Base64 encoded RSA Signature of the base64 decoded secretKey value.
     */
    private final String signature;

    /**
     * Should this authentication request be designated as a session. Set the value to 1 for yes and 0 for no.
     */
    private final int session;

    /**
     * Request a User Push ID be returned in the AuthsResponse by setting this value to 1.
     */
    private final int userPushID;

    /**
     * Arbitrary string of data up to 400 characters to be presented to the user during authorization to
     * provide context regarding the individual request
     */
    private final String context;

    /**
     * Policy object setting the policy override for this request. The policy can only increase the security level any
     * existing policy on the server. It can never reduce the security level of the policy.
     */
    private final Policy policy;

    /**
     * @param username Platform username, user push ID, or white label user identifier for the user being authenticated
     * @param appKey Application Key of your Application. This is found on the Keys tab of your Application details
     *                  in the Dashboard.
     * @param secretKey Base64 encoded secret JSON string containing these attributes:
     *                      secret:   Secret Key of the Application whose key is included in the current request.
     *                      stamped:  Platform formatted Date representing the current time of the request.
     * @param signature Base64 encoded RSA Signature of the base64 decoded secretKey value.
     * @param session Should this authentication request be designated as a session. Set the value to 1 for yes and 0 for no.
     * @param userPushID Request a User Push ID be returned in the AuthsResponse by setting this value to 1.
     * @param context  Arbitrary string of data up to 400 characters to be presented to the user during authorization to
     *                 provide context regarding the individual request
     * @param policy Policy object setting the policy override for this request. The policy can only increase the security level any
     * existing policy on the server. It can never reduce the security level of the policy.
     */
    public AuthsRequest(String username, long appKey, String secretKey, String signature, int session, int userPushID, String context, Policy policy) {
        this.username = username;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.signature = signature;
        if (session != 0 && session != 1) {
            throw new IllegalArgumentException("session must be 0 or 1");
        }
        this.session = session;
        if (userPushID != 0 && userPushID != 1) {
            throw new IllegalArgumentException("userPushID must be 0 or 1");
        }
        this.userPushID = userPushID;
        this.context = context;
        this.policy = policy;
    }

    /**
     * Get the Platform username, user push ID, or white label user identifier for the user being authenticated
     * @return Platform username, user push ID, or white label user identifier
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the Application Key of the Application associate with this request
     * @return Application Key
     */
    public long getAppKey() {
        return appKey;
    }

    /**
     * Get the Base64 encoded secret JSON string containing these attributes:
     *      secret:   Secret Key of the Application whose key is included in the current request.
     *      stamped:  Platform formatted Date representing the current time of the request.
     * @return Base64 encoded secret JSON string
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     *  Get the Base64 encoded RSA Signature of the base64 decoded secretKey value.
     * @return Base64 encoded RSA Signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Should this authentication request be designated as a session. 1 for yes and 0 for no.
     * @return 1 for yes and 0 for no.
     */
    public int getSession() {
        return session;
    }

    /**
     * Should the response for this request include a User Push ID be returned. 1 for yes and 0 for no.
     * @return 1 for yes and 0 for no.
     */
    public int getUserPushID() {
        return userPushID;
    }

    /**
     * Get the request context
     * @return Arbitrary string of data up to 400 characters to be presented to the user during authorization to
     * provide context regarding the individual request
     */
    public String getContext() {
        return context;
    }

    /**
     * Get the policy for the request
     * @return Policy object setting the policy override for this request. The policy can only increase the
     * security level any existing policy on the server. It can never reduce the security level of the policy.
     */
    public Policy getPolicy() {
        return policy;
    }

    @Override
    public String toString() {
        return "AuthsRequest{" +
                "username='" + username + '\'' +
                ", appKey=" + appKey +
                ", secretKey='" + secretKey + '\'' +
                ", signature='" + signature + '\'' +
                ", session=" + session +
                ", userPushID=" + userPushID +
                ", context=" + context +
                ", policy=" + policy +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthsRequest)) return false;

        AuthsRequest that = (AuthsRequest) o;

        if (appKey != that.appKey) return false;
        if (session != that.session) return false;
        if (userPushID != that.userPushID) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (secretKey != null ? !secretKey.equals(that.secretKey) : that.secretKey != null) return false;
        if (signature != null ? !signature.equals(that.signature) : that.signature != null) return false;
        if (context != null ? !context.equals(that.context) : that.context != null) return false;
        return !(policy != null ? !policy.equals(that.policy) : that.policy != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (int) (appKey ^ (appKey >>> 32));
        result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        result = 31 * result + (policy != null ? policy.hashCode() : 0);
        result = 31 * result + session;
        result = 31 * result + userPushID;
        return result;
    }
}
