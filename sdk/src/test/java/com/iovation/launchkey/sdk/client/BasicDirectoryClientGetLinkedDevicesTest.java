package com.iovation.launchkey.sdk.client; /**
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

import com.iovation.launchkey.sdk.domain.directory.Device;
import com.iovation.launchkey.sdk.domain.directory.DeviceStatus;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3DevicesListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3DevicesListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3DevicesListPostResponseDevice;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientGetLinkedDevicesTest {
    private BasicDirectoryClient client;
    private final UUID directoryId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");

    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<DirectoryV3DevicesListPostRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Mock
    private DirectoryV3DevicesListPostResponse response;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
        when(response.getDevices()).thenReturn(new ArrayList<DirectoryV3DevicesListPostResponseDevice>());
        when(transport
                .directoryV3DevicesListPost(any(DirectoryV3DevicesListPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void sendsUserIdAsIdentifier() throws Exception {
        client.getLinkedDevices("Expected User");
        verify(transport).directoryV3DevicesListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected User", requestCaptor.getValue().getIdentifier());
    }

    @Test
    public void sendsDirectoryEntity() throws Exception {
        client.getLinkedDevices(null);
        verify(transport)
                .directoryV3DevicesListPost(any(DirectoryV3DevicesListPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        client.getLinkedDevices(null);
        verify(transport)
                .directoryV3DevicesListPost(any(DirectoryV3DevicesListPostRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void returnsDevices() throws Exception {
        when(transport
                .directoryV3DevicesListPost(any(DirectoryV3DevicesListPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(new DirectoryV3DevicesListPostResponse(
                        Arrays.asList(
                                new DirectoryV3DevicesListPostResponseDevice(
                                        UUID.fromString("f9842d14-5734-4d09-b1d3-2ca64413d4a6"), "name 1", "type 1", 0,
                                        new Date(100L), new Date(101L)),
                                new DirectoryV3DevicesListPostResponseDevice(
                                        UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), "name 2", "type 2", 1,
                                        new Date(200L), new Date(201L))
                        )));

        List<Device> expected = Arrays.asList(
                new Device("f9842d14-5734-4d09-b1d3-2ca64413d4a6", "name 1", DeviceStatus.LINK_PENDING, "type 1", new Date(100L), new Date(101L)),
                new Device("fac25a3c-af79-49df-bd65-777e9c86e288", "name 2", DeviceStatus.LINKED, "type 2", new Date(200L), new Date(201L))
        );
        assertEquals(expected, client.getLinkedDevices(null));
    }
}