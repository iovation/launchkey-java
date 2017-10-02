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

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServiceKeysPatchRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientUpdateServicePublicKeyTest {
    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<ServiceKeysPatchRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    private static final UUID directoryId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private DirectoryClient client;
    private static final UUID serviceId = UUID.randomUUID();

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
    }


    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.updateServicePublicKey(serviceId, null, false, null);
        verify(transport)
                .directoryV3ServiceKeysPatch(any(ServiceKeysPatchRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.updateServicePublicKey(serviceId, null, false, null);
        verify(transport)
                .directoryV3ServiceKeysPatch(any(ServiceKeysPatchRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceIdInRequest() throws Exception {
        client.updateServicePublicKey(serviceId, null, false, null);
        verify(transport).directoryV3ServiceKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void sendsKeyIdInRequest() throws Exception {
        client.updateServicePublicKey(serviceId, "Key Id", false, null);
        verify(transport).directoryV3ServiceKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Key Id", requestCaptor.getValue().getKeyId());
    }

    @Test
    public void sendsActiveInRequest() throws Exception {
        client.updateServicePublicKey(serviceId, null, true, null);
        verify(transport).directoryV3ServiceKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }

    @Test
    public void sendsExpiresInRequest() throws Exception {
        Date expires = new Date(1500L);
        client.updateServicePublicKey(serviceId, null, false, expires);
        verify(transport).directoryV3ServiceKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expires, requestCaptor.getValue().getExpires());
    }
}