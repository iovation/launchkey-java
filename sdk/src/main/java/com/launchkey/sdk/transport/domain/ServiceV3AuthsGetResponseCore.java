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

package com.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceV3AuthsGetResponseCore {
    private final String encryptedDeviceResponse;
    private final String serviceUserHash;
    private final String orgUserHash;
    private final String userPushId;
    private final String publicKeyId;

    @JsonCreator
    public ServiceV3AuthsGetResponseCore(
            @JsonProperty(value = "auth", required = true) String encryptedDeviceResponse,
            @JsonProperty(value = "service_user_hash", required = true) String serviceUserHash,
            @JsonProperty(value = "org_user_hash") String orgUserHash,
            @JsonProperty(value = "user_push_id", required = true) String userPushId,
            @JsonProperty(value = "public_key_id", required = true) String publicKeyId
    ) {
        this.encryptedDeviceResponse = encryptedDeviceResponse;
        this.serviceUserHash = serviceUserHash;
        this.orgUserHash = orgUserHash;
        this.userPushId = userPushId;
        this.publicKeyId = publicKeyId;
    }

    public String getEncryptedDeviceResponse() {
        return encryptedDeviceResponse;
    }

    public String getServiceUserHash() {
        return serviceUserHash;
    }

    public String getOrgUserHash() {
        return orgUserHash;
    }

    public String getUserPushId() {
        return userPushId;
    }

    public String getPublicKeyId() {
        return publicKeyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsGetResponseCore)) return false;

        ServiceV3AuthsGetResponseCore that = (ServiceV3AuthsGetResponseCore) o;

        if (getEncryptedDeviceResponse() != null ? !getEncryptedDeviceResponse().equals(that.getEncryptedDeviceResponse()) : that.getEncryptedDeviceResponse() != null)
            return false;
        if (getServiceUserHash() != null ? !getServiceUserHash().equals(that.getServiceUserHash()) : that.getServiceUserHash() != null)
            return false;
        if (getOrgUserHash() != null ? !getOrgUserHash().equals(that.getOrgUserHash()) : that.getOrgUserHash() != null)
            return false;
        if (getUserPushId() != null ? !getUserPushId().equals(that.getUserPushId()) : that.getUserPushId() != null)
            return false;
        return getPublicKeyId() != null ? getPublicKeyId().equals(that.getPublicKeyId()) : that.getPublicKeyId() == null;
    }

    @Override
    public int hashCode() {
        int result = getEncryptedDeviceResponse() != null ? getEncryptedDeviceResponse().hashCode() : 0;
        result = 31 * result + (getServiceUserHash() != null ? getServiceUserHash().hashCode() : 0);
        result = 31 * result + (getOrgUserHash() != null ? getOrgUserHash().hashCode() : 0);
        result = 31 * result + (getUserPushId() != null ? getUserPushId().hashCode() : 0);
        result = 31 * result + (getPublicKeyId() != null ? getPublicKeyId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsGetResponseCore{" +
                "encryptedDeviceResponse='" + encryptedDeviceResponse + '\'' +
                ", serviceUserHash='" + serviceUserHash + '\'' +
                ", orgUserHash='" + orgUserHash + '\'' +
                ", userPushId='" + userPushId + '\'' +
                ", publicKeyId='" + publicKeyId + '\'' +
                '}';
    }
}
