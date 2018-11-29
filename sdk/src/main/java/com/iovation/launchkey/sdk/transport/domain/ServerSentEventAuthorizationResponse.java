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

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class ServerSentEventAuthorizationResponse implements ServerSentEvent {
    private final EntityIdentifier requestingEntity;
    private final UUID serviceId;
    private final String serviceUserHash;
    private final String organizationUserHash;
    private final String userPushId;
    private final UUID authorizationRequestId;
    private final boolean response;
    private final String deviceId;
    private final String[] servicePins;
    private final String type;
    private final String reason;
    private final String denialReason;

    public ServerSentEventAuthorizationResponse(
            EntityIdentifier requestingEntity, UUID serviceId, String serviceUserHash, String organizationUserHash,
            String userPushId, UUID authorizationRequestId, boolean response, String deviceId, String[] servicePins,
            String type, String reason, String denialReason
    ) {
        this.requestingEntity = requestingEntity;
        this.serviceId = serviceId;
        this.serviceUserHash = serviceUserHash;
        this.organizationUserHash = organizationUserHash;
        this.userPushId = userPushId;
        this.authorizationRequestId = authorizationRequestId;
        this.response = response;
        this.deviceId = deviceId;
        this.servicePins = servicePins;
        this.type = type;
        this.reason = reason;
        this.denialReason = denialReason;
    }

    public EntityIdentifier getRequestingEntity() {
        return requestingEntity;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getServiceUserHash() {
        return serviceUserHash;
    }

    public String getOrganizationUserHash() {
        return organizationUserHash;
    }

    public String getUserPushId() {
        return userPushId;
    }

    public UUID getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    public boolean getResponse() {
        return response;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String[] getServicePins() {
        return servicePins;
    }

    public String getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public String getDenialReason() {
        return denialReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSentEventAuthorizationResponse)) return false;
        ServerSentEventAuthorizationResponse that = (ServerSentEventAuthorizationResponse) o;
        return getResponse() == that.getResponse() &&
                Objects.equals(getRequestingEntity(), that.getRequestingEntity()) &&
                Objects.equals(getServiceId(), that.getServiceId()) &&
                Objects.equals(getServiceUserHash(), that.getServiceUserHash()) &&
                Objects.equals(getOrganizationUserHash(), that.getOrganizationUserHash()) &&
                Objects.equals(getUserPushId(), that.getUserPushId()) &&
                Objects.equals(getAuthorizationRequestId(), that.getAuthorizationRequestId()) &&
                Objects.equals(getDeviceId(), that.getDeviceId()) &&
                Arrays.equals(getServicePins(), that.getServicePins()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getReason(), that.getReason()) &&
                Objects.equals(getDenialReason(), that.getDenialReason());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getRequestingEntity(), getServiceId(), getServiceUserHash(), getOrganizationUserHash(), getUserPushId(), getAuthorizationRequestId(), getResponse(), getDeviceId(), getType(), getReason(), getDenialReason());
        result = 31 * result + Arrays.hashCode(getServicePins());
        return result;
    }

    @Override
    public String toString() {
        return "ServerSentEventAuthorizationResponse{" +
                "requestingEntity=" + requestingEntity +
                ", serviceId=" + serviceId +
                ", serviceUserHash='" + serviceUserHash + '\'' +
                ", organizationUserHash='" + organizationUserHash + '\'' +
                ", userPushId='" + userPushId + '\'' +
                ", authorizationRequestId=" + authorizationRequestId +
                ", response=" + response +
                ", deviceId='" + deviceId + '\'' +
                ", servicePins=" + Arrays.toString(servicePins) +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", denialReason='" + denialReason + '\'' +
                '}';
    }
}
