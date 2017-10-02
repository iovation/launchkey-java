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
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectorySdkKeysListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectorySdkKeysListPostResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientGetAllDirectorySdkKeysTest {

    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private OrganizationV3DirectorySdkKeysListPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectorySdkKeysListPostRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3DirectorySdkKeysListPost(any(OrganizationV3DirectorySdkKeysListPostRequest.class),
                any(EntityIdentifier.class)))
                .thenReturn(response);
        when(response.getSdkKeys()).thenReturn(new ArrayList<UUID>());
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getAllDirectorySdkKeys(null);
        verify(transport)
                .organizationV3DirectorySdkKeysListPost(any(OrganizationV3DirectorySdkKeysListPostRequest.class),
                        entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getAllDirectorySdkKeys(null);
        verify(transport)
                .organizationV3DirectorySdkKeysListPost(any(OrganizationV3DirectorySdkKeysListPostRequest.class),
                        entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        UUID expected = UUID.randomUUID();
        client.getAllDirectorySdkKeys(expected);
        verify(transport)
                .organizationV3DirectorySdkKeysListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getDirectoryId());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void includesAllSdkKeysInTheResponse() throws Exception {
        List<UUID> expected = Arrays.asList(
                UUID.fromString("f4c24794-aefc-11e7-8d36-0469f8dc10a5"),
                UUID.fromString("f6d44d14-aefc-11e7-9200-0469f8dc10a5")
        );
        when(response.getSdkKeys()).thenReturn(Arrays.asList(
                UUID.fromString("f4c24794-aefc-11e7-8d36-0469f8dc10a5"),
                UUID.fromString("f6d44d14-aefc-11e7-9200-0469f8dc10a5")
        ));
        List<UUID> actual = client.getAllDirectorySdkKeys(null);
        assertEquals(expected, actual);
    }
}