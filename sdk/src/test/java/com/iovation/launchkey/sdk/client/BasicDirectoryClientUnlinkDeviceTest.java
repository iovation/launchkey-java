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

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3DevicesDeleteRequest;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientUnlinkDeviceTest {
    private BasicDirectoryClient client;
    private final UUID directoryId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");
    private final UUID deviceId = UUID.randomUUID();

    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<DirectoryV3DevicesDeleteRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonUuidUserIdThrowsIllegalArgument() throws Exception {
        client.unlinkDevice(null, "Not UUID");
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullUserIdThrowsIllegalArgument() throws Exception {
        client.unlinkDevice(null, null);
    }

    @Test
    public void sendsDeviceIdInRequest() throws Exception {
        client.unlinkDevice(null, deviceId.toString());
        verify(transport).directoryV3devicesDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(deviceId, requestCaptor.getValue().getDeviceId());
    }

    @Test
    public void sendsUserIdInRequest() throws Exception {
        client.unlinkDevice("User ID", deviceId.toString());
        verify(transport).directoryV3devicesDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("User ID", requestCaptor.getValue().getIdentifier());
    }


    @Test
    public void sendsDirectoryEntity() throws Exception {
        client.unlinkDevice(null, deviceId.toString());
        verify(transport).directoryV3devicesDelete(any(DirectoryV3DevicesDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        client.unlinkDevice(null, deviceId.toString());
        verify(transport).directoryV3devicesDelete(any(DirectoryV3DevicesDeleteRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }
}