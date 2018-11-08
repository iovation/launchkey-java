package com.iovation.launchkey.sdk.client;
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

import com.iovation.launchkey.sdk.domain.organization.Directory;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BasicOrganizationClientTest {
    private static final UUID organizationId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");
    private Transport transport;
    private BasicOrganizationClient client;
    private OrganizationV3DirectoriesPostResponse directoriesPostResponse;
    private OrganizationV3DirectoriesGetResponse directoriesGetResponse;

    @Before
    public void setUp() throws Exception {
        transport = mock(Transport.class);
        directoriesPostResponse = mock(OrganizationV3DirectoriesPostResponse.class);
        when(transport.organizationV3DirectoriesPost(any(OrganizationV3DirectoriesPostRequest.class),
                any(EntityIdentifier.class))).thenReturn(directoriesPostResponse);
        directoriesGetResponse = mock(OrganizationV3DirectoriesGetResponse.class);
        when(directoriesGetResponse.getDirectories())
                .thenReturn(new ArrayList<OrganizationV3DirectoriesGetResponseDirectory>());
        when(transport.organizationV3DirectoriesGet(any(EntityIdentifier.class))).thenReturn(directoriesGetResponse);
        client = new BasicOrganizationClient(organizationId, transport);
    }

    @After
    public void tearDown() throws Exception {
        transport = null;
        client = null;
    }

    @Test
    public void createDirectorySendsNameInRequest() throws Exception {
        String expectedName = "Expected Name";
        client.createDirectory(expectedName);
        ArgumentCaptor<OrganizationV3DirectoriesPostRequest> captor =
                ArgumentCaptor.forClass(OrganizationV3DirectoriesPostRequest.class);
        verify(transport).organizationV3DirectoriesPost(captor.capture(), any(EntityIdentifier.class));
        assertEquals(expectedName, captor.getValue().getName());
    }

    @Test
    public void createDirectorySendsOrgEntityIdentifierInRequest() throws Exception {
        client.createDirectory(null);
        ArgumentCaptor<EntityIdentifier> captor = ArgumentCaptor.forClass(EntityIdentifier.class);
        verify(transport)
                .organizationV3DirectoriesPost(any(OrganizationV3DirectoriesPostRequest.class), captor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, captor.getValue().getType());
    }

    @Test
    public void createDirectorySendsOrgIdInEntityIdentifierInRequest() throws Exception {
        client.createDirectory(null);
        ArgumentCaptor<EntityIdentifier> captor = ArgumentCaptor.forClass(EntityIdentifier.class);
        verify(transport)
                .organizationV3DirectoriesPost(any(OrganizationV3DirectoriesPostRequest.class), captor.capture());
        assertEquals(organizationId, captor.getValue().getId());
    }

    @Test
    public void getAllDirectoriesSendsOrgEntityIdentifierInRequest() throws Exception {
        client.getAllDirectories();
        ArgumentCaptor<EntityIdentifier> captor = ArgumentCaptor.forClass(EntityIdentifier.class);
        verify(transport).organizationV3DirectoriesGet(captor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, captor.getValue().getType());
    }

    @Test
    public void getAllDirectoriesSendsOrgIdInEntityIdentifierInRequest() throws Exception {
        client.getAllDirectories();
        ArgumentCaptor<EntityIdentifier> captor = ArgumentCaptor.forClass(EntityIdentifier.class);
        verify(transport).organizationV3DirectoriesGet(captor.capture());
        assertEquals(organizationId, captor.getValue().getId());
    }

    @Test
    public void getAllDirectoriesReturnsAllDirectoriesInResponse() throws Exception {
        List<OrganizationV3DirectoriesGetResponseDirectory> transportDirectories = Arrays.asList(
                new OrganizationV3DirectoriesGetResponseDirectory(
                        UUID.randomUUID(), "Name One", false, null, null, null, null, false),
                new OrganizationV3DirectoriesGetResponseDirectory(
                        UUID.randomUUID(), "Name True", true, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()),
                        Arrays.asList(UUID.randomUUID(), UUID.randomUUID()), "Android Key", "IOS FP", true)
        );
        when(directoriesGetResponse.getDirectories()).thenReturn(transportDirectories);
        List<Directory> expected = Arrays.asList(
                new Directory(transportDirectories.get(0).getId(), transportDirectories.get(0).getName(),
                        transportDirectories.get(0).isActive(), transportDirectories.get(0).getServiceIds(),
                        transportDirectories.get(0).getSdkKeys(), transportDirectories.get(0).getAndroidKey(),
                        transportDirectories.get(0).getIosCertificateFingerprint(), false),
                new Directory(transportDirectories.get(1).getId(), transportDirectories.get(1).getName(),
                        transportDirectories.get(1).isActive(), transportDirectories.get(1).getServiceIds(),
                        transportDirectories.get(1).getSdkKeys(), transportDirectories.get(1).getAndroidKey(),
                        transportDirectories.get(1).getIosCertificateFingerprint(), true)
                );
        List<Directory> actual = client.getAllDirectories();
        assertEquals(expected, actual);
    }

}