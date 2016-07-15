/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.security.interfaces.RSAPrivateKey;

/**
 * Request object for a white label device delete call
 */
@JsonPropertyOrder({"sdk_key", "identifier", "device_name"})
public class WhiteLabelDeviceDeleteRequest extends RequestAbstract {

    /**
     * SDK Key for the White Label Group in which the White Label User exists.
     */
    private final String sdkKey;

    /**
     * Permanent and unique identifier of this user within the White Label Group.
     */
    private final String identifier;

    /**
     * Device name for the device linked to the White Label User which will be deleted.
     */
    private final String deviceName;

    /**
     * @param privateKey PrivateKey to sign requests
     * @param issuer     Issuer to identify the issuer of the request
     * @param sdkKey     SDK Key for the White Label Group in which the White Label User exists or will be created within.
     * @param identifier Permanent and unique identifier of this user within the White Label Group. This identifier
     *                   will be used to authenticate the user as well as link devices to the White Label User's
     *                   account.
     * @param deviceName Name of the device to delete
     */
    public WhiteLabelDeviceDeleteRequest(
            RSAPrivateKey privateKey, String issuer, String sdkKey, String identifier, String deviceName
    ) {
        super(privateKey, issuer);
        this.sdkKey = sdkKey;
        this.identifier = identifier;
        this.deviceName = deviceName;
    }

    /**
     * Get the SDK Keyf or the White Label Group in which the White Label User exists.
     *
     * @return SDK Key for the White Label Group in which the White Label User exists.
     */
    @JsonProperty("sdk_key")
    public String getSdkKey() {
        return sdkKey;
    }

    /**
     * Get the permanent and unique identifier of this user within the White Label Group.
     *
     * @return Permanent and unique identifier of this user within the White Label Group.
     */
    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the device name for the device linked to the White Label User which will be deleted.
     *
     * @return Device name for the device linked to the White Label User which will be deleted.
     */
    @JsonProperty("device_name")
    public String getDeviceName() {
        return deviceName;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhiteLabelDeviceDeleteRequest)) return false;
        if (!super.equals(o)) return false;

        WhiteLabelDeviceDeleteRequest that = (WhiteLabelDeviceDeleteRequest) o;

        if (getSdkKey() != null ? !getSdkKey().equals(that.getSdkKey()) : that.getSdkKey() != null) return false;
        if (getIdentifier() != null ? !getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() != null) {
            return false;
        }
        return getDeviceName() != null ? getDeviceName().equals(that.getDeviceName()) : that.getDeviceName() == null;

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSdkKey() != null ? getSdkKey().hashCode() : 0);
        result = 31 * result + (getIdentifier() != null ? getIdentifier().hashCode() : 0);
        result = 31 * result + (getDeviceName() != null ? getDeviceName().hashCode() : 0);
        return result;
    }

    @Override public String
    toString() {
        return "WhiteLabelDeviceDeleteRequest{" +
                "sdkKey='" + sdkKey + '\'' +
                ", identifier='" + identifier + '\'' +
                ", deviceName='" + deviceName + '\'' +
                "} " + super.toString();
    }
}
