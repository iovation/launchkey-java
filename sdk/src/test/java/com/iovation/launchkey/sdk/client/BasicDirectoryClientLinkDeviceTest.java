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

import com.iovation.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3DevicesPostRequest;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3DevicesPostResponse;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientLinkDeviceTest {
    private BasicDirectoryClient client;
    private final UUID directoryId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");

    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<DirectoryV3DevicesPostRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Mock
    private DirectoryV3DevicesPostResponse response;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
        when(transport
                .directoryV3DevicesPost(any(DirectoryV3DevicesPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void putsUsernameInRequest() throws Exception {
        client.linkDevice("Expected Username");
        verify(transport).directoryV3DevicesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Username", requestCaptor.getValue().getIdentifier());
    }

    @Test
    public void sendsDirectoryEntity() throws Exception {
        client.linkDevice(null);
        verify(transport).directoryV3DevicesPost(any(DirectoryV3DevicesPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        client.linkDevice(null);
        verify(transport).directoryV3DevicesPost(any(DirectoryV3DevicesPostRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void responseReturnsLinkingCode() throws Exception {
        when(response.getCode()).thenReturn("Expected Code");
        DirectoryUserDeviceLinkData response = client.linkDevice(null);
        assertEquals("Expected Code", response.getCode());
    }

    @Test
    public void responseReturnsQrCodeURL() throws Exception {
        when(response.getQRCode()).thenReturn("Expected QR Code");
        DirectoryUserDeviceLinkData response = client.linkDevice(null);
        assertEquals("Expected QR Code", response.getQrCodeUrl());
    }
}