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

package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response data from a "poll" call
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PollResponse {
    /**
     *  Base64 encoded RSA encrypted JSON string. Once Base64 decoded, decrypt the result with the private key of RSA
     *  public/private key pair associated with the Application whose Application Key was included in the request. The
     *  resulting JSON will have the following attributes:
     *      response:        The users response to the authorization request. true if approved and false if denied
     *      app_pins:           (optional) Up to 5 response tokens used for user validation. The functionality has been
     *                          deprecated and will be removed in future releases of the API
     *      auth_request:   Request-specific string used to match auth_request value returned from auths POST
     *      device_id:      Unique identifier for the device the user utilized to respond to the auth request
     */
    private final String auth;

    /**
     * Hashed user identifier to track a specific user across applications. This value will be used by the
     * Remote De-Orbit Event to identify the de-orbiting user.
     */
    private final String userHash;

    /**
     * Optional - A string that uniquely identifies the user across the entire Organization to which the Application whose
     * Application Key was included in the request belongs. This will be returned if, and only if, the Application belongs to an
     * Organization.
     */
    private final String organizationUser;

    /**
     * Optional - A value uniquely and permanently identifying the User associated with the auth_request within
     * the Application whose Application Key was included in the request belongs. This value may be used in place of a username
     * or White Label User identifier for authorization requests. This will be returned if, and only if, the
     * originating request passed a form control with the name user_push_id and a value of 1.
     */
    private final String userPushId;

    /**
     * @param auth Base64 encoded RSA encrypted JSON string. Once Base64 decoded, decrypt the result with the private
     *             key of RSA public/private key pair associated with the Application whose Application Key was included in the
     *             request. The resulting JSON will have the following attributes:
     *                  response:      The users response to the authorization request. true if approved and false
     *                                     if denied
     *                  app_pins:      (optional) Up to 5 response tokens used for user validation. The functionality
     *                                     has been deprecated and will be removed in future releases of the API
     *                  auth_request:  Request-specific string used to match auth_request value returned from auths POST
     *                  device_id:     Unique identifier for the device the user utilized to respond to the auth request
     * @param userHash Hashed user identifier to track a specific user across applications. This value will be used by
     *                 the Remote De-Orbit Event to identify the de-orbiting user.
     * @param organizationUser A string that uniquely identifies the user across the entire Organization to which the
     *                         Application whose Application Key was included in the request belongs. This will be returned if,
     *                         and only if, the Application belongs to an Organization.
     * @param userPushId A value uniquely and permanently identifying the User associated with the auth_request within
     *                   the Application whose Application Key was included in the request belongs. This value may be used in
     *                   place of a username or White Label User identifier for authorization requests. This will be
     *                   returned if, and only if, the originating request passed a form control with the name
     *                   user_push_id and a value of 1.
     */
    @JsonCreator
    public PollResponse(
            @JsonProperty("auth") String auth,
            @JsonProperty("user_hash") String userHash,
            @JsonProperty("organization_user") String organizationUser,
            @JsonProperty("user_push_id") String userPushId) {
        this.auth = auth;
        this.organizationUser = organizationUser;
        this.userHash = userHash;
        this.userPushId = userPushId;
    }

    /**
     * @return Base64 encoded RSA encrypted JSON string. Once Base64 decoded, decrypt the result with the private
     * key of RSA public/private key pair associated with the Application whose Application Key was included in the
     * request. The resulting JSON will have the following attributes:
     * response:      The users response to the authorization request. true if approved and false if denied
     * app_pins:      (optional) Up to 5 response tokens used for user validation. The functionality
     *                    has been deprecated and will be removed in future releases of the API
     * auth_request:  Request-specific string used to match auth_request value returned from auths POST
     * device_id:     Unique identifier for the device the user utilized to respond to the auth request
     */
    public String getAuth() {
        return auth;
    }

    /**
     * @return Hashed user identifier to track a specific user across applications. This value will be used by the
     * Remote De-Orbit Event to identify the de-orbiting user.
     */
    public String getUserHash() {
        return userHash;
    }

    /**
     * @return Optional - A string that uniquely identifies the user across the entire Organization to which the Application
     * whose Application Key was included in the request belongs. This will be returned if, and only if, the Application belongs
     * to an Organization.  Will return null if not supplied.
     */
    public String getOrganizationUser() {
        return organizationUser;
    }

    /**
     * @return Optional - A value uniquely and permanently identifying the User associated with the auth_request within
     * the Application whose Application Key was included in the request belongs. This value may be used in place of a username
     * or White Label User identifier for authorization requests. This will be returned if, and only if, the
     * originating request passed a form control with the name user_push_id and a value of 1.  Will return null if not
     * supplied.
     */
    public String getUserPushId() {
        return userPushId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PollResponse)) return false;

        PollResponse that = (PollResponse) o;

        if (auth != null ? !auth.equals(that.auth) : that.auth != null) return false;
        if (userHash != null ? !userHash.equals(that.userHash) : that.userHash != null) return false;
        if (organizationUser != null ? !organizationUser.equals(that.organizationUser) : that.organizationUser != null) return false;
        return !(userPushId != null ? !userPushId.equals(that.userPushId) : that.userPushId != null);

    }

    @Override
    public int hashCode() {
        int result = auth != null ? auth.hashCode() : 0;
        result = 31 * result + (userHash != null ? userHash.hashCode() : 0);
        result = 31 * result + (organizationUser != null ? organizationUser.hashCode() : 0);
        result = 31 * result + (userPushId != null ? userPushId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PollResponse{" +
                "auth='" + auth + '\'' +
                ", userHash='" + userHash + '\'' +
                ", organizationUser='" + organizationUser + '\'' +
                ", userPushId='" + userPushId + '\'' +
                '}';
    }
}
