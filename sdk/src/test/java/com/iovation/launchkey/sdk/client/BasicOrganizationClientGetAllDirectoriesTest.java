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
import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientGetAllDirectoriesTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private OrganizationV3DirectoriesGetResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3DirectoriesGet(any(EntityIdentifier.class)))
                .thenReturn(response);
        when(response.getDirectories()).thenReturn(new ArrayList<OrganizationV3DirectoriesGetResponseDirectory>());
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getAllDirectories();
        verify(transport).organizationV3DirectoriesGet(entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getAllDirectories();
        verify(transport).organizationV3DirectoriesGet(entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void includesAllDirectoriesInTheResponse() throws Exception {
        List<Directory> expected = Arrays.asList(
                new Directory(UUID.fromString("f4c24794-aefc-11e7-8d36-0469f8dc10a5"), "name1", true,
                        Collections.singletonList(UUID.fromString("4137af5c-b460-11e7-9bcd-0469f8dc10a5")),
                        Collections.singletonList(UUID.fromString("7e45bb07-b467-11e7-9c84-0469f8dc10a5")),
                        "AK 1", "p12 1", true, URI.create("https://a.b")),
                new Directory(UUID.fromString("f6d44d14-aefc-11e7-9200-0469f8dc10a5"), "name2", false,
                        Collections.singletonList(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5")),
                        Collections.singletonList(UUID.fromString("7ecd274f-b467-11e7-8936-0469f8dc10a5")),
                        "AK 2", "p12 2", false, URI.create("https://b.c"))
        );
        when(response.getDirectories()).thenReturn(Arrays.asList(
                new OrganizationV3DirectoriesGetResponseDirectory(UUID.fromString("f4c24794-aefc-11e7-8d36-0469f8dc10a5"), "name1", true,
                        Collections.singletonList(UUID.fromString("4137af5c-b460-11e7-9bcd-0469f8dc10a5")),
                        Collections.singletonList(UUID.fromString("7e45bb07-b467-11e7-9c84-0469f8dc10a5")),
                        "AK 1", "p12 1", true, URI.create("https://a.b")),
                new OrganizationV3DirectoriesGetResponseDirectory(UUID.fromString("f6d44d14-aefc-11e7-9200-0469f8dc10a5"), "name2", false,
                        Collections.singletonList(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5")),
                        Collections.singletonList(UUID.fromString("7ecd274f-b467-11e7-8936-0469f8dc10a5")),
                        "AK 2", "p12 2", false, URI.create("https://b.c"))
        ));
        List<Directory> actual = client.getAllDirectories();
        assertEquals(expected, actual);
    }
}