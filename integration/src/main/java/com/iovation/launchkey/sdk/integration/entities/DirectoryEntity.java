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

package com.iovation.launchkey.sdk.integration.entities;

import com.iovation.launchkey.sdk.domain.organization.Directory;

import java.net.URI;
import java.util.*;

public class DirectoryEntity {

    private final UUID id;
    private final String name;
    private final Boolean active;
    private final List<UUID> serviceIds;
    private final List<UUID> sdkKeys;
    private final String androidKey;
    private final String iosCertificateFingerprint;
    private final String iosCertificate;
    private final List<PublicKeyEntity> publicKeys;
    private final Boolean denialContextInquiryEnabled;
    private final URI webhookUrl;

    public DirectoryEntity(UUID id, String name, Boolean active, List<UUID> serviceIds, List<UUID> sdkKeys,
                           String androidKey,
                           String iosCertificateFingerprint, String iosCertificate,
                           Boolean denialContextInquiryEnabled,
                           URI webhookUrl) {
        if (id == null) throw new IllegalArgumentException("Argument \"id\" cannot be null.");
        this.id = id;
        this.name = name;
        this.active = active;
        this.serviceIds = serviceIds != null ? serviceIds : new ArrayList<UUID>();
        this.sdkKeys = sdkKeys != null ? sdkKeys : new ArrayList<UUID>();
        this.androidKey = androidKey;
        this.iosCertificate = iosCertificate;
        this.iosCertificateFingerprint = iosCertificateFingerprint;
        this.denialContextInquiryEnabled = denialContextInquiryEnabled;
        this.webhookUrl = webhookUrl;
        publicKeys = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
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

    public String getIosCertificate() {
        return iosCertificate;
    }

    public List<PublicKeyEntity> getPublicKeys() {
        return publicKeys;
    }

    public Boolean isDenialContextInquiryEnabled() {
        return denialContextInquiryEnabled;
    }

    public URI getWebhookUrl() {
        return webhookUrl;
    }

    public static DirectoryEntity fromDirectory(Directory directory) {
        return new DirectoryEntity(directory.getId(), directory.getName(), directory.isActive(),
                directory.getServiceIds(), directory.getSdkKeys(), directory.getAndroidKey(),
                directory.getIosCertificateFingerprint(), null, directory.isDenialContextInquiryEnabled(),
                directory.getWebhookUrl());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryEntity)) return false;
        DirectoryEntity that = (DirectoryEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(active, that.active)) return false;
        if (serviceIds != null) Collections.sort(serviceIds);
        if (that.serviceIds != null) Collections.sort(that.serviceIds);
        if (!Objects.equals(serviceIds, that.serviceIds)) return false;
        if (sdkKeys != null) Collections.sort(sdkKeys);
        if (that.sdkKeys != null) Collections.sort(that.sdkKeys);
        if (!Objects.equals(sdkKeys, that.sdkKeys)) return false;
        if (!Objects.equals(androidKey, that.androidKey)) return false;
        if (!Objects.equals(denialContextInquiryEnabled, that.denialContextInquiryEnabled)) return false;
        if (!Objects.equals(iosCertificateFingerprint, that.iosCertificateFingerprint)) return false;
        return Objects.equals(webhookUrl, that.webhookUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "DirectoryEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", serviceIds=" + serviceIds +
                ", sdkKeys=" + sdkKeys +
                ", androidKey='" + androidKey + '\'' +
                ", iosCertificateFingerprint='" + iosCertificateFingerprint + '\'' +
                ", iosCertificate='" + iosCertificate + '\'' +
                ", publicKeys=" + publicKeys +
                ", denialContextInquiryEnabled=" + denialContextInquiryEnabled +
                ", webhookUrl=" + webhookUrl +
                '}';
    }
}
