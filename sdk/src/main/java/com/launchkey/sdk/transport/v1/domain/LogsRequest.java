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
     * Application Key of your Application. This is found on the Keys tab of your Application details in the Dashboard.
     */
    private final long appKey;

    /**
     * Base64 encoded secret JSON string containing these attributes:
     * secret:   Secret Key of the Application whose key is included in the current request.
     * stamped:  Platform formatted Date representing the current time of the request.
     */
    private final String secretKey;

    /**
     * Base64 encoded RSA Signature of the base64 decoded secretKey value.
     */
    private final String signature;

    /**
     * @param action      Action to log -- Currently must be Authenticate or Revoke
     * @param status      Status of the action -- true or false as a string
     * @param authRequest The unique ID of the authentication request. This value should have been returned by a
     *                    success "auths" request.
     * @param appKey      Application Key of your Application. This is found on the Keys tab of your Application
     *                    details in the Dashboard.
     * @param secretKey   Base64 encoded secret JSON string containing these attributes:
     *                    secret:   Secret Key of the Application whose key is included in the current request.
     *                    stamped:  Platform formatted Date representing the current time of the request.
     * @param signature   Base64 encoded RSA Signature of the base64 decoded secretKey value.
     * @throws IllegalArgumentException when arguments have unacceptable values
     */
    public LogsRequest(
            String action, boolean status, String authRequest, long appKey, String secretKey, String signature
    ) {
        if (action == null || (!action.equals("Authenticate") && !action.equals("Revoke"))) {
            throw new IllegalArgumentException("action must be Authenticate or Revoke");
        }
        this.action = action;
        this.status = status;
        this.authRequest = authRequest;
        this.appKey = appKey;
        this.secretKey = secretKey;
        this.signature = signature;
    }

    /**
     * Get the action being logged (Authenticate or Revoke)
     *
     * @return Authenticate or Revoke
     */
    public String getAction() {
        return action;
    }

    /**
     * Get the status of the action being logged (Granted or Denied)
     *
     * @return Granted or Denied
     */
    public String getStatus() {
        return status ? "true" : "false";
    }

    /**
     * Get the unique ID of the authentication request. This value should have been returned by a successful
     * "auths" request.
     *
     * @return Unique ID for the auth request
     */
    public String getAuthRequest() {
        return authRequest;
    }

    /**
     * Get the Application Key of the Application associated with this request
     *
     * @return Application Key
     */
    public long getAppKey() {
        return appKey;
    }

    /**
     * Get the Base64 encoded secret JSON string containing these attributes:
     * secret:   Secret Key of the Application whose key is included in the current request.
     * stamped:  Platform formatted Date representing the current time of the request.
     *
     * @return Base64 encoded secret JSON string
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Get the Base64 encoded RSA Signature of the base64 decoded secretKey value.
     *
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
        if (appKey != that.appKey) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;
        if (authRequest != null ? !authRequest.equals(that.authRequest) : that.authRequest != null) return false;
        if (secretKey != null ? !secretKey.equals(that.secretKey) : that.secretKey != null) return false;
        return !(signature != null ? !signature.equals(that.signature) : that.signature != null);

    }

    @Override public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (status ? 1 : 0);
        result = 31 * result + (authRequest != null ? authRequest.hashCode() : 0);
        result = 31 * result + (int) (appKey ^ (appKey >>> 32));
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
                ", appKey=" + appKey +
                ", secretKey='" + secretKey + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
