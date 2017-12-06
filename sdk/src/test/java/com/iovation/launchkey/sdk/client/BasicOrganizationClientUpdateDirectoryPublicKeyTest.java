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
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysPatchRequest;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysPatchRequest;
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
public class BasicOrganizationClientUpdateDirectoryPublicKeyTest {
    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoryKeysPatchRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    private static final UUID organizationId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private BasicOrganizationClient client;
    private static final UUID directoryId = UUID.randomUUID();

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(organizationId, transport);
    }


    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.updateDirectoryPublicKey(directoryId, null, false, null);
        verify(transport)
                .organizationV3DirectoryKeysPatch(any(OrganizationV3DirectoryKeysPatchRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.updateDirectoryPublicKey(directoryId, null, false, null);
        verify(transport)
                .organizationV3DirectoryKeysPatch(any(OrganizationV3DirectoryKeysPatchRequest.class), entityCaptor.capture());
        assertEquals(organizationId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsDirectoryIdInRequest() throws Exception {
        client.updateDirectoryPublicKey(directoryId, null, false, null);
        verify(transport).organizationV3DirectoryKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(directoryId, requestCaptor.getValue().getDirectoryId());
    }

    @Test
    public void sendsKeyIdInRequest() throws Exception {
        client.updateDirectoryPublicKey(directoryId, "Key Id", false, null);
        verify(transport).organizationV3DirectoryKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Key Id", requestCaptor.getValue().getKeyId());
    }

    @Test
    public void sendsActiveInRequest() throws Exception {
        client.updateDirectoryPublicKey(directoryId, null, true, null);
        verify(transport).organizationV3DirectoryKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }

    @Test
    public void sendsExpiresInRequest() throws Exception {
        Date expires = new Date(1500L);
        client.updateDirectoryPublicKey(directoryId, null, false, expires);
        verify(transport).organizationV3DirectoryKeysPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expires, requestCaptor.getValue().getExpires());
    }
}