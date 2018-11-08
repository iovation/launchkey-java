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

import com.iovation.launchkey.sdk.domain.organization.Directory;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoriesListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoriesListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoriesListPostResponseDirectory;
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
public class BasicOrganizationClientGetDirectoriesTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private OrganizationV3DirectoriesListPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoriesListPostRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(orgId, transport);
        when(transport
                .organizationV3DirectoriesListPost(any(OrganizationV3DirectoriesListPostRequest.class),
                        any(EntityIdentifier.class)))
                .thenReturn(response);
        when(response.getDirectories()).thenReturn(new ArrayList<OrganizationV3DirectoriesListPostResponseDirectory>());
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getDirectories(new ArrayList<UUID>());
        verify(transport).organizationV3DirectoriesListPost(any(OrganizationV3DirectoriesListPostRequest.class),
                entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getDirectories(new ArrayList<UUID>());
        verify(transport).organizationV3DirectoriesListPost(any(OrganizationV3DirectoriesListPostRequest.class),
                entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsDirectoryIds() throws Exception {
        List<UUID> ids = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        client.getDirectories(ids);
        verify(transport).organizationV3DirectoriesListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(ids, requestCaptor.getValue().getDirectoryIds());
    }

    @Test
    public void returnsAllDevices() throws Exception {
        List<Directory> expected = Collections.singletonList(
                new Directory(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), "Expected Name", true,
                        Collections.singletonList(UUID.fromString("4137af5c-b460-11e7-9bcd-0469f8dc10a5")),
                        Collections.singletonList(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5")),
                        "Expected Android Key", "Expected iOS Certificate Fingerprint", true)
        );
        when(response.getDirectories()).thenReturn(Collections.singletonList(
                new OrganizationV3DirectoriesListPostResponseDirectory(
                        UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), "Expected Name", true,
                        Collections.singletonList(UUID.fromString("4137af5c-b460-11e7-9bcd-0469f8dc10a5")),
                        Collections.singletonList(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5")),
                        "Expected Android Key", "Expected iOS Certificate Fingerprint", true)
        ));
        List<Directory> actual = client.getDirectories(new ArrayList<UUID>());
        assertEquals(expected, actual);
    }
}