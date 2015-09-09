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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Data for an "logs" request
 */
public class LogsRequest {
    /**
     * Authenticate or Revoke
     */
    private final String action;

    /**
     * Granted or Denied
     */
    private final boolean status;

    /**
     * Unique ID of the authentication request
     */
    private final String authRequest;

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
     * @param authRequest The unique ID of the authentication request. This value should have been returned by a
     *                     success "auths" request.
     *                     @see AuthsResponse
     * @param rocketKey Rocket Key of your Rocket. This is found on the Keys tab of your Rocket details in the LaunchKey Dashboard.
     * @param secretKey Base64 encoded secret JSON string containing these attributes:
     *                      secret:   Rocket Secret Key of the rocket whose key is included in the current request.
     *                      stamped:  LaunchKey formatted Date representing the current time of the request.
     * @param signature Base64 encoded RSA Signature of the base64 decoded secretKey value.
     */
    public LogsRequest(String action, boolean status, String authRequest, long rocketKey, String secretKey, String signature) {
        if (action == null || (!action.equals("Authenticate") && !action.equals("Revoke"))) {
            throw new IllegalArgumentException("action must be Authenticate or Revoke");
        }
        this.action = action;
        this.status = status;
        this.authRequest = authRequest;
        this.rocketKey = rocketKey;
        this.secretKey = secretKey;
        this.signature = signature;
    }

    /**
     * Get the action being logged (Authenticate or Revoke)
     * @return Authenticate or Revoke
     */
    public String getAction() {
        return action;
    }

    /**
     * Get the status of the action being logged (Granted or Denied)
     * @returnGranted or Denied
     */
    public String getStatus() {
        return status ? "true" : "false";
    }

    /**
     * Get the unique ID of the authentication request. This value should have been returned by a successful
     * "auths" request.
     * @see AuthsResponse
     * @return Unique ID
     */
    public String getAuthRequest() {
        return authRequest;
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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogsRequest)) return false;

        LogsRequest that = (LogsRequest) o;

        if (status != that.status) return false;
        if (rocketKey != that.rocketKey) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (authRequest != null ? !authRequest.equals(that.authRequest) : that.authRequest != null) return false;
        if (secretKey != null ? !secretKey.equals(that.secretKey) : that.secretKey != null) return false;
        return !(signature != null ? !signature.equals(that.signature) : that.signature != null);

    }

    @Override public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (authRequest != null ? authRequest.hashCode() : 0);
        result = 31 * result + (int) (rocketKey ^ (rocketKey >>> 32));
        result = 31 * result + (secretKey != null ? secretKey.hashCode() : 0);
        result = 31 * result + (signature != null ? signature.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogsRequest{" +
                "action='" + action + '\'' +
                ", status='" + status + '\'' +
                ", authRequest='" + authRequest + '\'' +
                ", rocketKey=" + rocketKey +
                ", secretKey='" + secretKey + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
