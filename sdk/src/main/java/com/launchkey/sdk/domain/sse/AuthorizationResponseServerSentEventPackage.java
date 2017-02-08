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

package com.launchkey.sdk.domain.sse;

import com.launchkey.sdk.domain.service.AuthorizationResponse;

/**
 * Object created by parsing an service response from a Server Sent Event
 */
public class AuthorizationResponseServerSentEventPackage implements ServerSentEventPackage {

    private final AuthorizationResponse authorizationResponse;

    /**
     * @param authorizationResponse The response sent in hte callback
     */
    public AuthorizationResponseServerSentEventPackage(AuthorizationResponse authorizationResponse) {
        this.authorizationResponse = authorizationResponse;
    }

    /**
     * Get the response sent in hte callback
     * @return The response sent in hte callback
     */
    public AuthorizationResponse getAuthorizationResponse() {
        return authorizationResponse;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizationResponseServerSentEventPackage)) return false;

        AuthorizationResponseServerSentEventPackage that = (AuthorizationResponseServerSentEventPackage) o;

        return !(authorizationResponse != null ? !authorizationResponse.equals(that.authorizationResponse) : that.authorizationResponse != null);

    }

    @Override
    public int hashCode() {
        return getAuthorizationResponse() != null ? getAuthorizationResponse().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AuthorizationResponseServerSentEventPackage{" +
                "authorizationResponse=" + authorizationResponse +
                '}';
    }
}
