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

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerSentEventAuthorizationResponseCore {
    private final String auth;
    private final String userPushId;
    private final String serviceUserHash;
    private final String orgUserHash;
    private final String publicKeyId;
    private final String authJwe;

    @JsonCreator
    public ServerSentEventAuthorizationResponseCore(
            @JsonProperty(value = "auth", required = true) String auth,
            @JsonProperty(value = "auth_jwe", required = false) String authJwe,
            @JsonProperty(value = "user_push_id", required = true) String userPushId,
            @JsonProperty(value = "service_user_hash", required = true) String serviceUserHash,
            @JsonProperty(value = "public_key_id", required = true) String publicKeyId,
            @JsonProperty(value = "org_user_hash") String orgUserHash
    ) {
        this.auth = auth;
        this.authJwe = authJwe;
        this.userPushId = userPushId;
        this.serviceUserHash = serviceUserHash;
        this.publicKeyId = publicKeyId;
        this.orgUserHash = orgUserHash;
    }

    public String getAuth() {
        return auth;
    }

    public String getAuthJwe() {return authJwe;}

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
        return Objects.equals(getAuth(), that.getAuth()) &&
                Objects.equals(getUserPushId(), that.getUserPushId()) &&
                Objects.equals(getServiceUserHash(), that.getServiceUserHash()) &&
                Objects.equals(getOrgUserHash(), that.getOrgUserHash()) &&
                Objects.equals(getPublicKeyId(), that.getPublicKeyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuth(), getUserPushId(), getServiceUserHash(), getOrgUserHash(), getPublicKeyId());
    }

    @Override
    public String toString() {
        return "ServerSentEventAuthorizationResponseCore{" +
                "auth='" + auth + '\'' +
                ", userPushId='" + userPushId + '\'' +
                ", serviceUserHash='" + serviceUserHash + '\'' +
                ", orgUserHash='" + orgUserHash + '\'' +
                ", publicKeyId='" + publicKeyId + '\'' +
                '}';
    }
}
