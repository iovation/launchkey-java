/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Data for an "auths" request
 */
public class AuthsRequest {

    /**
     * LaunchKey username, user push ID, or white label user identifier for the user being authenticated
     */
    private final String username;

    /**
     * Rocket Key of your Rocket. This is found on the Keys tab of your Rocket details in the LaunchKey Dashboard.
     */
    private final long rocketKey;

    /**
     * Base64 encoded secret JSON string containing these attributes:
     *      secret:   Rocket Secret Key of the rocket whose key is included in the current request.
     *      stamped:  LaunchKey formatted Date representing the current time of the request.
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
     * @param username LaunchKey username, user push ID, or white label user identifier for the user being authenticated
     * @param rocketKey Rocket Key of your Rocket. This is found on the Keys tab of your Rocket details in the LaunchKey Dashboard.
     * @param secretKey Base64 encoded secret JSON string containing these attributes:
     *                      secret:   Rocket Secret Key of the rocket whose key is included in the current request.
     *                      stamped:  LaunchKey formatted Date representing the current time of the request.
     * @param signature Base64 encoded RSA Signature of the base64 decoded secretKey value.
     * @param session Should this authentication request be designated as a session. Set the value to 1 for yes and 0 for no.
     * @param userPushID Request a User Push ID be returned in the AuthsResponse by setting this value to 1.
     */
    public AuthsRequest(String username, long rocketKey, String secretKey, String signature, int session, int userPushID) {
        this.username = username;
        this.rocketKey = rocketKey;
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
    }

    /**
     * Get the LaunchKey username, user push ID, or white label user identifier for the user being authenticated
     * @return LaunchKey username, user push ID, or white label user identifier
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the Rocket Key of the Rocket associate with this request
     * @return Rocket Key
     */
    public long getRocketKey() {
        return rocketKey;
    }

    /**
     * Get the Base64 encoded secret JSON string containing these attributes:
     *      secret:   Rocket Secret Key of the rocket whose key is included in the current request.
     *      stamped:  LaunchKey formatted Date representing the current time of the request.
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

    @Override
    public String toString() {
        return "AuthsRequest{" +
                "username='" + username + '\'' +
                ", rocketKey=" + rocketKey +
                ", secretKey='" + secretKey + '\'' +
                ", signature='" + signature + '\'' +
                ", session=" + session +
                ", userPushID=" + userPushID +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthsRequest)) return false;

        AuthsRequest that = (AuthsRequest) o;

        if (rocketKey != that.rocketKey) return false;
        if (session != that.session) return false;
        if (userPushID != that.userPushID) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (secretKey != null ? !secretKey.equals(that.secretKey) : that.secretKey != null) return false;
        return !(signature != null ? !signature.equals(that.signature) : that.signature != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (int) (rocketKey ^ (rocketKey >>> 32));
        result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        result = 31 * result + session;
        result = 31 * result + userPushID;
        return result;
    }
}
