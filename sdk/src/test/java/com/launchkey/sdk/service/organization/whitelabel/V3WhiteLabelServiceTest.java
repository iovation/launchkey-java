package com.launchkey.sdk.service.organization.whitelabel;

import com.launchkey.sdk.domain.Device;
import com.launchkey.sdk.domain.DeviceStatus;
import com.launchkey.sdk.domain.WhiteLabelDeviceLinkData;
import com.launchkey.sdk.transport.v3.Transport;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceAddRequest;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceAddResponse;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceDeleteRequest;
import com.launchkey.sdk.transport.v3.domain.WhiteLabelDeviceListRequest;
import com.sun.javafx.collections.ImmutableObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class V3WhiteLabelServiceTest {
    private Transport transport;
    private long orgKey = 12345L;
    private String issuer = "organization:" + String.valueOf(orgKey);
    private String sdkKey = "Expected SDK Key";
    private WhiteLabelService service;
    private RSAPrivateKey privateKey;

    @Before
    public void setUp() throws Exception {
        transport = mock(Transport.class);
        privateKey = mock(RSAPrivateKey.class);
        service = new V3WhiteLabelService(transport, privateKey, orgKey, sdkKey);
    }

    @After
    public void tearDown() throws Exception {
        transport = null;
        service = null;
    }

    @Test
    public void addDeviceCallesTransportWithExpectedWhiteLabelDeviceAddRequest() throws Exception {
        String identifier = "Expected Identifier";
        WhiteLabelDeviceAddRequest expected = new WhiteLabelDeviceAddRequest(privateKey, issuer, sdkKey, identifier);
        ArgumentCaptor<WhiteLabelDeviceAddRequest> captor = ArgumentCaptor.forClass(WhiteLabelDeviceAddRequest.class);
        when(transport.whiteLabelUserDeviceAdd(any(WhiteLabelDeviceAddRequest.class)))
                .thenReturn(new WhiteLabelDeviceAddResponse("code", "url"));
        service.linkDevice(identifier);
        verify(transport).whiteLabelUserDeviceAdd(captor.capture());
        WhiteLabelDeviceAddRequest actual = captor.getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void addDeviceReturnsExpectedDeviceAddResponse() throws Exception {
        String code = "Expected Code";
        String url = "Expected URL";
        WhiteLabelDeviceLinkData expected = new WhiteLabelDeviceLinkData(code, url);
        when(transport.whiteLabelUserDeviceAdd(any(WhiteLabelDeviceAddRequest.class)))
                .thenReturn(new WhiteLabelDeviceAddResponse(code, url));
        WhiteLabelDeviceLinkData actual = service.linkDevice("identifier");
        assertEquals(expected, actual);
    }

    @Test
    public void getDevicesPassesExpectedRequest() throws Exception {
        String identifier = "Expected identifier";
        WhiteLabelDeviceListRequest expected = new WhiteLabelDeviceListRequest(privateKey, issuer, sdkKey, identifier);
        com.launchkey.sdk.transport.v3.domain.Device[] devices = {};
        when(transport.whiteLabelUserDeviceList(any(WhiteLabelDeviceListRequest.class)))
                .thenReturn(devices);
        service.getLinkedDevices(identifier);
        verify(transport).whiteLabelUserDeviceList(expected);
    }

    @Test
    public void getDevicesReturnsExpectedResponse() throws Exception {
        Date created1 = new Date(System.currentTimeMillis() - 100);
        Date updated1 = new Date(System.currentTimeMillis() - 200);
        Date created2 = new Date(System.currentTimeMillis() - 300);
        Date updated2 = new Date(System.currentTimeMillis() - 400);
        com.launchkey.sdk.transport.v3.domain.Device[] devices = {
                new com.launchkey.sdk.transport.v3.domain.Device("name1", DeviceStatus.LINKED.getStatusCode(), "type1", created1, updated1),
                new com.launchkey.sdk.transport.v3.domain.Device("name2", DeviceStatus.LINK_PENDING.getStatusCode(), "type2", created2, updated2)
        };
        when(transport.whiteLabelUserDeviceList(any(WhiteLabelDeviceListRequest.class))).thenReturn(devices);
        List<Device> actual = new ImmutableObservableList<Device>(
                new Device("name1", DeviceStatus.LINKED, "type1", created1, updated1),
                new Device("name2", DeviceStatus.LINK_PENDING, "type2", created2, updated2)
        );
        List<Device> expected = service.getLinkedDevices("identifier");
        assertEquals("Unexpected number of devices returned", 2, actual.size());
        assertTrue(
                "Expected devices not found in response:\nExpected: " + expected + "\nActual: " + actual,
                actual.containsAll(expected)
        );
    }

    @Test
    public void removeDevicePassesExpectedRequest() throws Exception {
        String deviceName = "Expected Device Name";
        String identifier = "Expected Identifier";
        WhiteLabelDeviceDeleteRequest expected = new WhiteLabelDeviceDeleteRequest(
                privateKey,
                issuer,
                sdkKey,
                identifier,
                deviceName
        );
        service.unlinkDevice(identifier, deviceName);
        verify(transport).whiteLabelUserDeviceDelete(expected);
    }

}
