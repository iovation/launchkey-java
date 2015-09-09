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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
     * @param identifier Permanent and unique identifier of this user within your application. This identifier will be used to
     *                   authenticate the user as well as pair devices additional devices to the user's account within your white
     *                   label group.
     * @param rocketKey  Rocket Key of your Rocket. This is found on the Keys tab of your Rocket details in the LaunchKey Dashboard.
     * @param secretKey  Base64 encoded secret JSON string containing these attributes:
     *                      secret:   Rocket Secret Key of the rocket whose key is included in the current request.
     *                      stamped:  LaunchKey formatted Date representing the current time of the request.
     */
    public UsersRequest(String identifier, long rocketKey, String secretKey) {
        this.identifier = identifier;
        this.rocketKey = rocketKey;
        this.secretKey = secretKey;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty("app_key")
    public long getRocketKey() {
        return rocketKey;
    }

    @JsonProperty("secret_key")
    public String getSecretKey() {
        return secretKey;
    }
}
