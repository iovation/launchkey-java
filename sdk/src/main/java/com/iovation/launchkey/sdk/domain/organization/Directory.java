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

package com.iovation.launchkey.sdk.domain.organization;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * A Directory is the root level for a White Label implementation. It has Directory Services for Authorizations
 * amd Sessions. It also has User Devices used to perform Authorizations and manage Sessions for a Directory User.
 */
public class Directory {
    private final UUID id;
    private final String name;
    private final boolean active;
    private final List<UUID> serviceIds;
    private final List<UUID> sdkKeys;
    private final String androidKey;
    private final String iosCertificateFingerprint;
    private final Boolean denialContextInquiryEnabled;

    /**
     * @param id The unique identifier for the Directory
     * @param name The name of the Directory
     * @param active Is the Directory active
     * @param serviceIds List of IDs for Services belonging to the Directory
     * @param sdkKeys List of Mobile SDK Keys for the Directory
     * @param androidKey The key that will be used to send push notifications to Android Devices.
     * @param iosCertificateFingerprint The MD5 certificate fingerprint for the Certificate used to send push
     * notifications to iOS Devices.
     * @param denialContextInquiryEnabled Will the user be prompted for denial context when they deny authorization
     * requests for any and all child services.
     *
     */
    public Directory(UUID id, String name, boolean active, List<UUID> serviceIds, List<UUID> sdkKeys, String androidKey,
                     String iosCertificateFingerprint, Boolean denialContextInquiryEnabled) {
        if (id == null) throw new IllegalArgumentException("Argument \"id\" cannot be null.");
        this.id = id;
        this.name = name;
        this.active = active;
        this.serviceIds = serviceIds;
        this.sdkKeys = sdkKeys;
        this.androidKey = androidKey;
        this.iosCertificateFingerprint = iosCertificateFingerprint;
        this.denialContextInquiryEnabled = denialContextInquiryEnabled;
    }

    /**
     * Get the unique identifier for the Directory
     *
     * @return The unique identifier for the Directory
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get the name of the Directory
     * @return The Directory name
     */
    public String getName() {
        return name;
    }

    /**
     * Is the Directory active?
     * @return Active determination for the Directory
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Get the IDs for all Services that belong to the Directory
     * @return List of Service IDs for the Directory
     */
    public List<UUID> getServiceIds() {
        return serviceIds;
    }

    /**
     * Get all of the valid SDK Keys for the Directory
     * @return List of valid SDK Keys for the Directory
     */
    public List<UUID> getSdkKeys() {
        return sdkKeys;
    }

    /**
     * Get the key used for push notifications on Android devices
     * @return Android push key
     */
    public String getAndroidKey() {
        return androidKey;
    }

    /**
     * Get the SHA256 fingerprint of the certificate used for push notifications in APNS
     * @return SHA256 fingerprint of the APNS push certificate
     */
    public String getIosCertificateFingerprint() {
        return iosCertificateFingerprint;
    }

    /**
     * Should the user be prompted for denial context when they deny authorization requests for any and all child
     * services.
     * @return Should the user be prompted for denial context
     */
    public Boolean isDenialContextInquiryEnabled() {
        return denialContextInquiryEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Directory)) return false;
        Directory directory = (Directory) o;
        return isActive() == directory.isActive() &&
                Objects.equals(getId(), directory.getId()) &&
                Objects.equals(getName(), directory.getName()) &&
                Objects.equals(getServiceIds(), directory.getServiceIds()) &&
                Objects.equals(getSdkKeys(), directory.getSdkKeys()) &&
                Objects.equals(getAndroidKey(), directory.getAndroidKey()) &&
                Objects.equals(getIosCertificateFingerprint(), directory.getIosCertificateFingerprint()) &&
                Objects.equals(isDenialContextInquiryEnabled(), directory.isDenialContextInquiryEnabled());
    }

    @Override
    public int hashCode() {
        return  id.hashCode() * 478;
    }

    @Override
    public String toString() {
        return "Directory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", serviceIds=" + serviceIds +
                ", androidKey='" + androidKey + '\'' +
                ", iosCertificateFingerprint='" + iosCertificateFingerprint + '\'' +
                ", denialContextInquiryEnabled='" + denialContextInquiryEnabled + "\'" +
                '}';
    }
}
