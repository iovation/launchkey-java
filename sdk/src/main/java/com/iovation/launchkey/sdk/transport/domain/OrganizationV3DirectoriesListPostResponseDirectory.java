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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganizationV3DirectoriesListPostResponseDirectory {
    private final UUID id;
    private final String name;
    private final boolean active;
    private final List<UUID> serviceIds;
    private final List<UUID> sdkKeys;
    private final String androidKey;
    private final String iosCertificateFingerprint;
    private final Boolean denialContextInquiryEnabled;
    private final URI webhookUrl;

    @JsonCreator
    public OrganizationV3DirectoriesListPostResponseDirectory(
            @JsonProperty("id") UUID id, @JsonProperty("name") String name, @JsonProperty("active") boolean active,
            @JsonProperty("service_ids") List<UUID> serviceIds, @JsonProperty("sdk_keys") List<UUID> sdkKeys,
            @JsonProperty("android_key") String androidKey,
            @JsonProperty("ios_certificate_fingerprint") String iosCertificateFingerprint,
            @JsonProperty("denial_context_inquiry_enabled") Boolean denialContextInquiryEnabled,
            @JsonProperty("webhook_url") URI webhookUrl) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.serviceIds = serviceIds;
        this.sdkKeys = sdkKeys;
        this.androidKey = androidKey;
        this.iosCertificateFingerprint = iosCertificateFingerprint;
        this.denialContextInquiryEnabled = denialContextInquiryEnabled;
        this.webhookUrl = webhookUrl;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<UUID> getServiceIds() {
        return serviceIds;
    }

    public List<UUID> getSdkKeys() {
        return sdkKeys;
    }

    public String getAndroidKey() {
        return androidKey;
    }

    public String getIosCertificateFingerprint() {
        return iosCertificateFingerprint;
    }

    public Boolean isDenialContextInquiryEnabled() {
        return denialContextInquiryEnabled;
    }

    public URI getWebhookUrl() {
        return webhookUrl;
    }
}
