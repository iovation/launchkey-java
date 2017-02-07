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

import java.util.Arrays;
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

    public ServerSentEventAuthorizationResponse(
            EntityIdentifier requestingEntity, UUID serviceId, String serviceUserHash, String organizationUserHash,
            String userPushId, UUID authorizationRequestId, boolean response, String deviceId, String[] servicePins
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSentEventAuthorizationResponse)) return false;

        ServerSentEventAuthorizationResponse that = (ServerSentEventAuthorizationResponse) o;

        if (getResponse() != that.getResponse()) return false;
        if (getRequestingEntity() != null ? !getRequestingEntity().equals(that.getRequestingEntity()) : that.getRequestingEntity() != null)
            return false;
        if (getServiceId() != null ? !getServiceId().equals(that.getServiceId()) : that.getServiceId() != null)
            return false;
        if (getServiceUserHash() != null ? !getServiceUserHash().equals(that.getServiceUserHash()) : that.getServiceUserHash() != null)
            return false;
        if (getOrganizationUserHash() != null ? !getOrganizationUserHash().equals(that.getOrganizationUserHash()) : that.getOrganizationUserHash() != null)
            return false;
        if (getUserPushId() != null ? !getUserPushId().equals(that.getUserPushId()) : that.getUserPushId() != null)
            return false;
        if (getAuthorizationRequestId() != null ? !getAuthorizationRequestId().equals(that.getAuthorizationRequestId()) : that.getAuthorizationRequestId() != null)
            return false;
        if (getDeviceId() != null ? !getDeviceId().equals(that.getDeviceId()) : that.getDeviceId() != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getServicePins(), that.getServicePins());
    }

    @Override
    public int hashCode() {
        int result = getRequestingEntity() != null ? getRequestingEntity().hashCode() : 0;
        result = 31 * result + (getServiceId() != null ? getServiceId().hashCode() : 0);
        result = 31 * result + (getServiceUserHash() != null ? getServiceUserHash().hashCode() : 0);
        result = 31 * result + (getOrganizationUserHash() != null ? getOrganizationUserHash().hashCode() : 0);
        result = 31 * result + (getUserPushId() != null ? getUserPushId().hashCode() : 0);
        result = 31 * result + (getAuthorizationRequestId() != null ? getAuthorizationRequestId().hashCode() : 0);
        result = 31 * result + (getResponse() ? 1 : 0);
        result = 31 * result + (getDeviceId() != null ? getDeviceId().hashCode() : 0);
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
                '}';
    }
}
