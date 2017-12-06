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

import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;

import java.util.List;

public class AuthorizationResponseEntity {
    private final String authorizationRequestId;
    private final String deviceId;
    private final String organizationUserHash;
    private final List<String> servicePins;
    private final String serviceUserHash;
    private final String userPushId;
    private final Boolean authorized;

    public AuthorizationResponseEntity(String authorizationRequestId, String deviceId, String organizationUserHash,
                                       List<String> servicePins, String serviceUserHash, String userPushId,
                                       Boolean authorized) {
        this.authorizationRequestId = authorizationRequestId;
        this.deviceId = deviceId;
        this.organizationUserHash = organizationUserHash;
        this.servicePins = servicePins;
        this.serviceUserHash = serviceUserHash;
        this.userPushId = userPushId;
        this.authorized = authorized;
    }

    public String getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getOrganizationUserHash() {
        return organizationUserHash;
    }

    public List<String> getServicePins() {
        return servicePins;
    }

    public String getServiceUserHash() {
        return serviceUserHash;
    }

    public String getUserPushId() {
        return userPushId;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public static AuthorizationResponseEntity fromAuthorizationResponse(AuthorizationResponse authorizationResponse) {
        return new AuthorizationResponseEntity(authorizationResponse.getAuthorizationRequestId(),
                authorizationResponse.getDeviceId(), authorizationResponse.getOrganizationUserHash(),
                authorizationResponse.getServicePins(), authorizationResponse.getServiceUserHash(),
                authorizationResponse.getUserPushId(), authorizationResponse.isAuthorized());
    }
}
