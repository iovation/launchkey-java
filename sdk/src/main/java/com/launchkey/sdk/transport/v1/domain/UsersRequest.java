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

package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Request data for a "users" call.
 *
 * This object will be JSON encoded and signed rather than signing the secretKey like other requests
 */
@JsonPropertyOrder({"app_key", "secret_key", "identifier"})
public class UsersRequest {

    /**
     * Permanent and unique identifier of this user within your application. This identifier will be used to
     * authenticate the user as well as pair devices additional devices to the user's account within your white
     * label group.
     */
    private final String identifier;

    /**
     * Application Key of your Application. This is found on the Keys tab of your Application details in the Dashboard.
     */
    private final long appKey;

    /**
     * Base64 encoded secret JSON string containing these attributes:
     *      secret:   Secret Key of the Application whose key is included in the current request.
     *      stamped:  LaunchKey formatted Date representing the current time of the request.
     */
    private final String secretKey;

    /**
     * @param identifier Permanent and unique identifier of this user within your application. This identifier will be used to
     *                   authenticate the user as well as pair devices additional devices to the user's account within your white
     *                   label group.
     * @param appKey  Application Key of your Application. This is found on the Keys tab of your Application details in the Dashboard.
     * @param secretKey  Base64 encoded secret JSON string containing these attributes:
     *                      secret:   Secret Key of the Application whose key is included in the current request.
     *                      stamped:  LaunchKey formatted Date representing the current time of the request.
     */
    public UsersRequest(String identifier, long appKey, String secretKey) {
        this.identifier = identifier;
        this.appKey = appKey;
        this.secretKey = secretKey;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("app_key")
    public long getAppKey() {
        return appKey;
    }

    /**
     * @deprecated Use {@link #getAppKey()}
     * @return Application Key
     */
    @Deprecated
    @JsonIgnore
    public long getRocketKey() {
        return getAppKey();
    }

    @JsonProperty("secret_key")
    public String getSecretKey() {
        return secretKey;
    }
}
