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

import com.launchkey.sdk.domain.directory.Device;
import com.launchkey.sdk.domain.directory.DeviceStatus;
import com.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.service.DirectoryService;
import com.launchkey.sdk.transport.Transport;
import com.launchkey.sdk.transport.domain.*;
import com.launchkey.sdk.transport.domain.EntityIdentifier.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicDirectoryClient implements DirectoryClient {
    public final Transport transport;
    public final EntityIdentifier directory;

    public BasicDirectoryClient(UUID directoryId, Transport transport) {
        this.transport = transport;
        this.directory = new EntityIdentifier(EntityType.DIRECTORY, directoryId);
    }

    public DirectoryUserDeviceLinkData linkDevice(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidRequestException, InvalidCredentialsException,
            CommunicationErrorException, MarshallingError, CryptographyError {
        DirectoryV3DevicesPostRequest request = new DirectoryV3DevicesPostRequest(userId);
        DirectoryV3DevicesPostResponse response = transport.directoryV3DevicesPost(request, directory);
        return new DirectoryUserDeviceLinkData(response.getCode(), response.getQRCode());
    }

    @Override
    public List<Device> getLinkedDevices(String userId) throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException, InvalidRequestException, InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        DirectoryV3DevicesListPostRequest request = new DirectoryV3DevicesListPostRequest(userId);
        DirectoryV3DevicesListPostResponse response = transport.directoryV3DevicesListPost(request, directory);
        List<Device> devices = new ArrayList();
        for (DirectoryV3DevicesListPostResponseDevice responseDevice : response.getDevices()) {
            devices.add(new Device(
                    responseDevice.getId().toString(),
                    responseDevice.getName(),
                    DeviceStatus.fromCode(responseDevice.getStatus()),
                    responseDevice.getType()
            ));
        }
        return devices;
    }

    @Override
    public void unlinkDevice(String userId, String deviceId) throws PlatformErrorException,
            UnknownEntityException, InvalidResponseException, InvalidStateException, InvalidRequestException,
            InvalidCredentialsException, CommunicationErrorException, MarshallingError, CryptographyError {
        UUID deviceUUID;
        try {
            deviceUUID = UUID.fromString(deviceId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Device ID", e);

        }
        DirectoryV3DevicesDeleteRequest request = new DirectoryV3DevicesDeleteRequest(userId, deviceUUID);
        transport.directoryV3devicesDelete(request, directory);
    }

    @Override
    public void endAllServiceSessions(String userId) throws PlatformErrorException, UnknownEntityException,
            InvalidResponseException, InvalidStateException, InvalidRequestException, InvalidCredentialsException,
            CommunicationErrorException, CryptographyError, MarshallingError {
        DirectoryV3SessionsDeleteRequest request = new DirectoryV3SessionsDeleteRequest((userId));
        transport.directoryV3SessionsDelete(request, directory);
    }
}
