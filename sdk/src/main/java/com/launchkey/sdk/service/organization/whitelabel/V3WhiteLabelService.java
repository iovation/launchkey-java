/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.organization.whitelabel;

import com.launchkey.sdk.domain.Device;
import com.launchkey.sdk.domain.DeviceStatus;
import com.launchkey.sdk.domain.WhiteLabelDeviceLinkData;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.transport.v3.Transport;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceAddRequest;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceAddResponse;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceDeleteRequest;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceListRequest;

import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * White Label service based on an API V3 transport
 */
public class V3WhiteLabelService implements WhiteLabelService {

    private final Transport transport;
    private final long orgKey;
    private final String sdkKey;
    private final String issuer;
    private final RSAPrivateKey privateKey;

    /**
     * @param transport Transport service for interacting with the Platform API
     * @param orgKey    Organization key for the Organization in which the White Label Group exists
     * @param sdkKey    SDK Key for the White Label Organization with which this service will interact
     */
    public V3WhiteLabelService(Transport transport, RSAPrivateKey privateKey, long orgKey, String sdkKey) {
        this.transport = transport;
        this.privateKey = privateKey;
        this.issuer = "organization:".concat(String.valueOf(orgKey));
        this.orgKey = orgKey;
        this.sdkKey = sdkKey;
    }

    @Override
    public WhiteLabelDeviceLinkData linkDevice(
            String identifier
    ) throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidRequestException, InvalidCredentialsException, CommunicationErrorException {
        WhiteLabelDeviceAddRequest request = new WhiteLabelDeviceAddRequest(privateKey, issuer, sdkKey, identifier);
        WhiteLabelDeviceAddResponse response = transport.whiteLabelUserDeviceAdd(request);
        WhiteLabelDeviceLinkData domainResponse = new WhiteLabelDeviceLinkData(response.getCode(), response.getQrCodeUrl());
        return domainResponse;
    }

    @Override
    public List<Device> getLinkedDevices(
            String identifier
    ) throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidRequestException, InvalidCredentialsException, CommunicationErrorException {
        WhiteLabelDeviceListRequest request = new WhiteLabelDeviceListRequest(privateKey, issuer, sdkKey, identifier);
        com.launchkey.sdk.transport.v3.domain.Device[] transportDevices = transport.whiteLabelUserDeviceList(request);
        List<Device> devices = new ArrayList<Device>(transportDevices.length);
        for (com.launchkey.sdk.transport.v3.domain.Device transportDevice : transportDevices) {
            Device device = new Device(
                    transportDevice.getName(),
                    DeviceStatus.fromCode(transportDevice.getStatus()),
                    transportDevice.getDeviceType(),
                    transportDevice.getCreated(),
                    transportDevice.getUpdated()
            );
            devices.add(device);
        }
        return devices;
    }

    @Override
    public void unlinkDevice(
            String identifier, String deviceName
    ) throws PlatformErrorException, UnknownEntityException, InvalidResponseException, InvalidStateException,
            InvalidRequestException, InvalidCredentialsException, CommunicationErrorException {
        WhiteLabelDeviceDeleteRequest request = new WhiteLabelDeviceDeleteRequest(
                privateKey,
                issuer,
                sdkKey,
                identifier,
                deviceName
        );
        transport.whiteLabelUserDeviceDelete(request);
    }
}
