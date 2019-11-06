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

import com.iovation.launchkey.sdk.domain.service.AdvancedAuthorizationResponse;

import java.util.Objects;

/**
 * Object created by parsing an service response from a Server Sent Event
 */
public class AdvancedAuthorizationResponseWebhookPackage implements WebhookPackage {

    private final AdvancedAuthorizationResponse authorizationResponse;

    /**
     * @param authorizationResponse The response sent in the callback
     */
    public AdvancedAuthorizationResponseWebhookPackage(AdvancedAuthorizationResponse authorizationResponse) {
        this.authorizationResponse = authorizationResponse;
    }

    /**
     * Get the response sent in the callback
     * @return The response sent in the callback
     */
    public AdvancedAuthorizationResponse getAuthorizationResponse() {
        return authorizationResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdvancedAuthorizationResponseWebhookPackage)) return false;

        AdvancedAuthorizationResponseWebhookPackage that = (AdvancedAuthorizationResponseWebhookPackage) o;

        return Objects.equals(authorizationResponse, that.authorizationResponse);

    }

    @Override
    public int hashCode() {
        return Objects.hash(authorizationResponse);
    }

    @Override
    public String toString() {
        return "AdvancedAuthorizationResponseWebhookPackage{" +
                "authorizationResponse=" + authorizationResponse +
                '}';
    }
}
