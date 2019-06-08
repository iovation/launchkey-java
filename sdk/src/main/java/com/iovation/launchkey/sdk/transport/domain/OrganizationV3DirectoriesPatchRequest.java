/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.net.URI;
import java.util.UUID;

@JsonPropertyOrder({"directory_id", "active", "android_key", "ios_p12", "denial_context_inquiry_enabled", "webhook_url"})
public class OrganizationV3DirectoriesPatchRequest {
    private final UUID directoryId;
    private final Boolean active;
    private final String androidKey;
    private final String iosP12;
    private final Boolean denialContextInquiryEnabled;
    private final URI webhookUrl;

    public OrganizationV3DirectoriesPatchRequest(UUID directoryId, Boolean active, String androidKey,
                                                 String iosP12, Boolean denialContextInquiryEnabled,
                                                 URI webhookUrl) {
        this.directoryId = directoryId;
        this.active = active;
        this.androidKey = androidKey;
        this.iosP12 = iosP12;
        this.denialContextInquiryEnabled = denialContextInquiryEnabled;
        this.webhookUrl = webhookUrl;
    }

    @JsonProperty("directory_id")
    public UUID getDirectoryId() {
        return directoryId;
    }

    @JsonProperty("active")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean isActive() {
        return active;
    }

    @JsonProperty("android_key")
    public String getAndroidKey() {
        return androidKey;
    }

    @JsonProperty("ios_p12")
    public String getIosP12() {
        return iosP12;
    }

    @JsonProperty("denial_context_inquiry_enabled")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean isDenialContextInquiryEnabled() {
        return denialContextInquiryEnabled;
    }

    @JsonProperty("webhook_url")
    public URI getWebhookUrl() {
        return webhookUrl;
    }
}
