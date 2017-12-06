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
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysDeleteRequest;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysDeleteRequest;
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
public class BasicOrganizationClientRemoveDirectoryPublicKeyTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoryKeysDeleteRequest> requestCaptor;


    public BasicOrganizationClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.removeDirectoryPublicKey(null, null);
        verify(transport).organizationV3DirectoryKeysDelete(any(OrganizationV3DirectoryKeysDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.removeDirectoryPublicKey(null, null);
        verify(transport).organizationV3DirectoryKeysDelete(any(OrganizationV3DirectoryKeysDeleteRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendDirectoryIdInRequest() throws Exception {
        UUID id = UUID.randomUUID();
        client.removeDirectoryPublicKey(id, null);
        verify(transport).organizationV3DirectoryKeysDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getDirectoryId());
    }

    @Test
    public void sendsKeyIdInRequest() throws Exception {
        client.removeDirectoryPublicKey(null, "Key ID");
        verify(transport).organizationV3DirectoryKeysDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Key ID", requestCaptor.getValue().getKeyId());
    }
}