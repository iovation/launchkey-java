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

package com.launchkey.sdk.client;

import com.launchkey.sdk.transport.Transport;

import java.util.UUID;

public class DirectoryFactory {
    private final Transport transport;
    private final UUID directoryId;

    public DirectoryFactory(Transport transport, UUID directoryId) {
        this.transport = transport;
        this.directoryId = directoryId;
    }

    public DirectoryClient makeDirectoryClient() {
        return new BasicDirectoryClient(directoryId, transport);
    }

    public ServiceClient makeServiceClient(String serviceId) {
        UUID serviceUUID;
        try {
            serviceUUID = UUID.fromString(serviceId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Service ID", e);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Service ID cannot be null", e);
        }
        return new BasicServiceClient(serviceUUID, transport);
    }
}
