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

package com.launchkey.sdk.domain.auth;

/**
 * Object created by parsing an auth response from a Server Sent Event
 */
public class AuthResponseCallbackResponse implements CallbackResponse {

    private final AuthResponse authResponse;

    /**
     * @param authResponse The response sent in hte callback
     */
    public AuthResponseCallbackResponse(AuthResponse authResponse) {
        this.authResponse = authResponse;
    }

    /**
     * Get the response sent in hte callback
     * @return The response sent in hte callback
     */
    public AuthResponse getAuthResponse() {
        return authResponse;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthResponseCallbackResponse)) return false;

        AuthResponseCallbackResponse that = (AuthResponseCallbackResponse) o;

        return !(authResponse != null ? !authResponse.equals(that.authResponse) : that.authResponse != null);

    }

    @Override public int hashCode() {
        return authResponse != null ? authResponse.hashCode() : 0;
    }

    @Override public String toString() {
        return "AuthResponseCallbackResponse{" +
                "authResponse=" + authResponse +
                '}';
    }
}
