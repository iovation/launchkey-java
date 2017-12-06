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
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServiceKeysDeleteRequest;
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
public class BasicDirectoryClientRemoveServicePublicKeyTest {
    private final static UUID directoryId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServiceKeysDeleteRequest> requestCaptor;


    public DirectoryClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicDirectoryClient(directoryId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.removeServicePublicKey(null, null);
        verify(transport).directoryV3ServiceKeysDelete(any(ServiceKeysDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.removeServicePublicKey(null, null);
        verify(transport).directoryV3ServiceKeysDelete(any(ServiceKeysDeleteRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendServiceIdInRequest() throws Exception {
        UUID id = UUID.randomUUID();
        client.removeServicePublicKey(id, null);
        verify(transport).directoryV3ServiceKeysDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void sendsKeyIdInRequest() throws Exception {
        client.removeServicePublicKey(null, "Key ID");
        verify(transport).directoryV3ServiceKeysDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Key ID", requestCaptor.getValue().getKeyId());
    }

}