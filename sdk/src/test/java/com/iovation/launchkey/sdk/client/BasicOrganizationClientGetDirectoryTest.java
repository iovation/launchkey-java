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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientGetDirectoryTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private OrganizationV3DirectoriesListPostResponse response;

    @Mock
    OrganizationV3DirectoriesListPostResponseDirectory responseDirectory;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoriesListPostRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3DirectoriesListPost(any(OrganizationV3DirectoriesListPostRequest.class),
                any(EntityIdentifier.class))).thenReturn(response);
        when(response.getDirectories()).thenReturn(Collections.singletonList(responseDirectory));
        when(responseDirectory.getId()).thenReturn(UUID.randomUUID());
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getDirectory(null);
        verify(transport).organizationV3DirectoriesListPost(any(OrganizationV3DirectoriesListPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getDirectory(null);
        verify(transport).organizationV3DirectoriesListPost(any(OrganizationV3DirectoriesListPostRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        UUID id = UUID.randomUUID();
        client.getDirectory(id);
        verify(transport).organizationV3DirectoriesListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Collections.singletonList(id), requestCaptor.getValue().getDirectoryIds());
    }

    @Test
    public void getsServiceIdFromResponse() throws Exception {
        UUID expected = UUID.randomUUID();
        when(responseDirectory.getId()).thenReturn(expected);
        assertEquals(expected, client.getDirectory(null).getId());
    }

    @Test
    public void getsServiceNameFromResponse() throws Exception {
        when(responseDirectory.getName()).thenReturn("Name");
        assertEquals("Name", client.getDirectory(null).getName());
    }

    @Test
    public void getsServiceIdsFromResponse() throws Exception {
        List<UUID> expected = Collections.singletonList(UUID.randomUUID());
        when(responseDirectory.getServiceIds()).thenReturn(expected);
        assertEquals(expected, client.getDirectory(null).getServiceIds());
    }

    @Test
    public void getsSdkKeysFromResponse() throws Exception {
        List<UUID> expected = Collections.singletonList(UUID.randomUUID());
        when(responseDirectory.getSdkKeys()).thenReturn(expected);
        assertEquals(expected, client.getDirectory(null).getSdkKeys());
    }

    @Test
    public void getsAndroidKeyFromResponse() throws Exception {
        when(responseDirectory.getAndroidKey()).thenReturn("Expected");
        assertEquals("Expected", client.getDirectory(null).getAndroidKey());
    }

    @Test
    public void getsIosCertificateFingerprintFromResponse() throws Exception {
        when(responseDirectory.getIosCertificateFingerprint()).thenReturn("Expected");
        assertEquals("Expected", client.getDirectory(null).getIosCertificateFingerprint());
    }

    @Test
    public void getsServiceActiveFromResponse() throws Exception {
        when(responseDirectory.isActive()).thenReturn(true);
        assertTrue(client.getDirectory(null).isActive());
    }
}