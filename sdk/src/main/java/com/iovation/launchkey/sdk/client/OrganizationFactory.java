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

package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.UUIDHelper;
import com.iovation.launchkey.sdk.transport.Transport;

import java.util.UUID;

public class OrganizationFactory {
    private final Transport transport;
    private final UUID organizationId;

    public OrganizationFactory(Transport transport, UUID organizationId) {
        UUIDHelper.validateVersion(organizationId, 1);
        this.transport = transport;
        this.organizationId = organizationId;
    }

    public DirectoryClient makeDirectoryClient(String directoryId) {
        UUID directoryUUID;
        try {
            directoryUUID = UUIDHelper.fromString(directoryId, 1);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Directory ID", e);
        }
        return new BasicDirectoryClient(directoryUUID, transport);
    }

    public ServiceClient makeServiceClient(String serviceId) {
        UUID serviceUUID;
        try {
            serviceUUID = UUIDHelper.fromString(serviceId, 1);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Service ID", e);
        }
        return new BasicServiceClient(serviceUUID, transport);
    }
}
