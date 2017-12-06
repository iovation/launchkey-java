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
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoriesPostRequest;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoriesPostResponse;
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
public class BasicOrganizationClientCreateDirectoryTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private OrganizationV3DirectoriesPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoriesPostRequest> requestCaptor;

    public BasicOrganizationClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3DirectoriesPost(any(OrganizationV3DirectoriesPostRequest.class),
                any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.createDirectory(null);
        verify(transport)
                .organizationV3DirectoriesPost(any(OrganizationV3DirectoriesPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.createDirectory(null);
        verify(transport)
                .organizationV3DirectoriesPost(any(OrganizationV3DirectoriesPostRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsName() throws Exception {
        client.createDirectory("Expected Name");
        verify(transport).organizationV3DirectoriesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Name", requestCaptor.getValue().getName());
    }

    @Test
    public void returnsId() throws Exception {
        UUID expected = UUID.randomUUID();
        when(response.getId()).thenReturn(expected);
        UUID actual = client.createDirectory(null);
        assertEquals(expected, actual);
    }
}