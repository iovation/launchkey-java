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

package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.security.interfaces.RSAPrivateKey;

/**
 * Request data for a white label device list call.
 */
@JsonPropertyOrder({"sdk_key", "identifier"})
public class WhiteLabelDeviceListRequest extends RequestAbstract {

    /**
     * SDK Key for the White Label Group in which the White Label User exists or will be created within.
     */
    private final String sdkKey;

    /**
     * Permanent and unique identifier of this user within the White Label Group. This identifier will be used to
     * authenticate the user as well as link devices to the White Label User's account.
     */
    private final String identifier;

    /**
     * @param privateKey PrivateKey to sign requests
     * @param issuer     Issuer to identify the issuer of the request
     * @param sdkKey     SDK Key for the White Label Group in which the White Label User exists or will be created within.
     * @param identifier Permanent and unique identifier of this user within the White Label Group. This identifier
     *                   will be used to authenticate the user as well as link devices to the White Label User's
     *                   account.
     */
    public WhiteLabelDeviceListRequest(RSAPrivateKey privateKey, String issuer, String sdkKey, String identifier) {
        super(privateKey, issuer);
        this.sdkKey = sdkKey;
        this.identifier = identifier;
    }

    @JsonProperty("sdk_key")
    public String getSdkKey() {
        return sdkKey;
    }

    @JsonProperty("identifier")
    public String getIdentifier() {
        return identifier;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhiteLabelDeviceListRequest)) return false;
        if (!super.equals(o)) return false;

        WhiteLabelDeviceListRequest that = (WhiteLabelDeviceListRequest) o;

        if (getSdkKey() != null ? !getSdkKey().equals(that.getSdkKey()) : that.getSdkKey() != null) return false;
        return getIdentifier() != null ? getIdentifier().equals(that.getIdentifier()) : that.getIdentifier() == null;

    }

    @Override public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSdkKey() != null ? getSdkKey().hashCode() : 0);
        result = 31 * result + (getIdentifier() != null ? getIdentifier().hashCode() : 0);
        return result;
    }

    @Override public String toString() {
        return "WhiteLabelDeviceListRequest{" +
                "sdkKey='" + sdkKey + '\'' +
                ", identifier='" + identifier + '\'' +
                "} " + super.toString();
    }
}
