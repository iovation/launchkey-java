/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.domain.webhook;

import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;

/**
 * Object created by parsing an service response from a Server Sent Event
 * @deprecated Use {@link AdvancedAuthorizationResponseWebhookPackage}
 */
@Deprecated
public class AuthorizationResponseWebhookPackage implements WebhookPackage {

    private final AuthorizationResponse authorizationResponse;

    /**
     * @param authorizationResponse The response sent in hte callback
     */
    public AuthorizationResponseWebhookPackage(AuthorizationResponse authorizationResponse) {
        this.authorizationResponse = authorizationResponse;
    }

    /**
     * Get the response sent in hte callback
     * @return The response sent in hte callback
     */
    public AuthorizationResponse getAuthorizationResponse() {
        return authorizationResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizationResponseWebhookPackage)) return false;

        AuthorizationResponseWebhookPackage that = (AuthorizationResponseWebhookPackage) o;

        return !(authorizationResponse != null ? !authorizationResponse.equals(that.authorizationResponse) : that.authorizationResponse != null);

    }

    @Override
    public int hashCode() {
        return getAuthorizationResponse() != null ? getAuthorizationResponse().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AuthorizationResponseWebhookPackage{" +
                "authorizationResponse=" + authorizationResponse +
                '}';
    }
}
