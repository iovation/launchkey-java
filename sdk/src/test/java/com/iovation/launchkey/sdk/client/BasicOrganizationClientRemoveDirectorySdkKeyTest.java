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
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectorySdkKeysDeleteRequest;
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
public class BasicOrganizationClientRemoveDirectorySdkKeyTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectorySdkKeysDeleteRequest> requestCaptor;


    public BasicOrganizationClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.removeDirectorySdkKey(null, null);
        verify(transport).organizationV3DirectorySdkKeysDelete(any(OrganizationV3DirectorySdkKeysDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.removeDirectorySdkKey(null, null);
        verify(transport).organizationV3DirectorySdkKeysDelete(any(OrganizationV3DirectorySdkKeysDeleteRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendServiceIdInRequest() throws Exception {
        UUID id = UUID.randomUUID();
        client.removeDirectorySdkKey(id, null);
        verify(transport).organizationV3DirectorySdkKeysDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getDirectoryId());
    }

    @Test
    public void sendsSdkKeyInRequest() throws Exception {
        UUID sdkKey = UUID.randomUUID();
        client.removeDirectorySdkKey(null, sdkKey);
        verify(transport).organizationV3DirectorySdkKeysDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(sdkKey, requestCaptor.getValue().getSdkKey());
    }


}