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

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerSentEventAuthorizationResponseCore {
    private final String auth;
    private final String userPushId;
    private final String serviceUserHash;
    private final String orgUserHash;
    private final String publicKeyId;

    @JsonCreator
    public ServerSentEventAuthorizationResponseCore(
            @JsonProperty(value = "auth", required = true) String auth,
            @JsonProperty(value = "user_push_id", required = true) String userPushId,
            @JsonProperty(value = "service_user_hash", required = true) String serviceUserHash,
            @JsonProperty(value = "public_key_id", required = true) String publicKeyId,
            @JsonProperty(value = "org_user_hash") String orgUserHash
    ) {
        this.auth = auth;
        this.userPushId = userPushId;
        this.serviceUserHash = serviceUserHash;
        this.publicKeyId = publicKeyId;
        this.orgUserHash = orgUserHash;
    }

    public String getAuth() {
        return auth;
    }

    public String getUserPushId() {
        return userPushId;
    }

    public String getServiceUserHash() {
        return serviceUserHash;
    }

    public String getOrgUserHash() {
        return orgUserHash;
    }

    public String getPublicKeyId() {
        return publicKeyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSentEventAuthorizationResponseCore)) return false;

        ServerSentEventAuthorizationResponseCore that = (ServerSentEventAuthorizationResponseCore) o;

        if (auth != null ? !auth.equals(that.auth) : that.auth != null) return false;
        if (userPushId != null ? !userPushId.equals(that.userPushId) : that.userPushId != null) return false;
        if (serviceUserHash != null ? !serviceUserHash.equals(that.serviceUserHash) : that.serviceUserHash != null)
            return false;
        if (orgUserHash != null ? !orgUserHash.equals(that.orgUserHash) : that.orgUserHash != null) return false;
        return publicKeyId != null ? publicKeyId.equals(that.publicKeyId) : that.publicKeyId == null;
    }

    @Override
    public int hashCode() {
        int result = auth != null ? auth.hashCode() : 0;
        result = 31 * result + (userPushId != null ? userPushId.hashCode() : 0);
        result = 31 * result + (serviceUserHash != null ? serviceUserHash.hashCode() : 0);
        result = 31 * result + (orgUserHash != null ? orgUserHash.hashCode() : 0);
        result = 31 * result + (publicKeyId != null ? publicKeyId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthorizationResponseServerSentEvent{" +
                "auth='" + auth + '\'' +
                ", userPushId='" + userPushId + '\'' +
                ", serviceUserHash='" + serviceUserHash + '\'' +
                ", orgUserHash='" + orgUserHash + '\'' +
                ", publicKeyId='" + publicKeyId + '\'' +
                '}';
    }
}
