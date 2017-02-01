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

import java.util.UUID;

public class ServiceV3AuthsGetResponse {
    private final EntityIdentifier requestingEntity;
    private final String requestingEntityPublicKeyId;
    private final UUID serviceId;
    private final ServiceV3AuthsGetResponseCore apiResponse;

    public ServiceV3AuthsGetResponse(
            EntityIdentifier requestingEntity,
            String requestingEntityPublicKeyId,
            UUID serviceId,
            ServiceV3AuthsGetResponseCore apiResponse
    ) {
        this.requestingEntity = requestingEntity;
        this.requestingEntityPublicKeyId = requestingEntityPublicKeyId;
        this.serviceId = serviceId;
        this.apiResponse = apiResponse;
    }

    public EntityIdentifier getRequestingEntity() {
        return requestingEntity;
    }

    public String getRequestingEntityPublicKeyId() {
        return requestingEntityPublicKeyId;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public String getServiceUserHash() {
        return apiResponse.getServiceUserHash();
    }

    public String getOrganizationUserHash() {
        return apiResponse.getOrgUserHash();
    }

    public String getUserPushId() {
        return apiResponse.getUserPushId();
    }

    public String getEncryptedDeviceResponse() {
        return apiResponse.getEncryptedDeviceResponse();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsGetResponse)) return false;

        ServiceV3AuthsGetResponse that = (ServiceV3AuthsGetResponse) o;

        if (getRequestingEntity() != null ? !getRequestingEntity().equals(that.getRequestingEntity()) : that.getRequestingEntity() != null)
            return false;
        if (getRequestingEntityPublicKeyId() != null ? !getRequestingEntityPublicKeyId().equals(that.getRequestingEntityPublicKeyId()) : that.getRequestingEntityPublicKeyId() != null)
            return false;
        if (getServiceId() != null ? !getServiceId().equals(that.getServiceId()) : that.getServiceId() != null)
            return false;
        return apiResponse != null ? apiResponse.equals(that.apiResponse) : that.apiResponse == null;
    }

    @Override
    public int hashCode() {
        int result = getRequestingEntity() != null ? getRequestingEntity().hashCode() : 0;
        result = 31 * result + (getRequestingEntityPublicKeyId() != null ? getRequestingEntityPublicKeyId().hashCode() : 0);
        result = 31 * result + (getServiceId() != null ? getServiceId().hashCode() : 0);
        result = 31 * result + (apiResponse != null ? apiResponse.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsGetResponse{" +
                "requestingEntity=" + requestingEntity +
                ", requestingEntityPublicKeyId='" + requestingEntityPublicKeyId + '\'' +
                ", serviceId=" + serviceId +
                ", apiResponse=" + apiResponse +
                '}';
    }
}
