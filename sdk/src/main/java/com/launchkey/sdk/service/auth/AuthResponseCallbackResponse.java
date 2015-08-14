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

package com.launchkey.sdk.service.auth;

public class AuthResponseCallbackResponse implements CallbackResponse {

    private final AuthResponse authResponse;

    public AuthResponseCallbackResponse(AuthResponse authResponse) {
        this.authResponse = authResponse;
    }

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
