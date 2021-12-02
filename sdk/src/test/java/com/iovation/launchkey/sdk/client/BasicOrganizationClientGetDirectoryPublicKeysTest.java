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

import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.KeysListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.KeysListPostResponsePublicKey;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysListPostRequest;
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
public class BasicOrganizationClientGetDirectoryPublicKeysTest {

    @Mock
    private Transport transport;

    @Mock
    private KeysListPostResponse response;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoryKeysListPostRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    private static final UUID organizationId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private BasicOrganizationClient client;
    private static final UUID directoryId = UUID.randomUUID();

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(organizationId, transport);
        when(transport
                .organizationV3DirectoryKeysListPost(any(OrganizationV3DirectoryKeysListPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
        when(response.getPublicKeys()).thenReturn(new ArrayList<KeysListPostResponsePublicKey>());
    }


    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getDirectoryPublicKeys(directoryId);
        verify(transport)
                .organizationV3DirectoryKeysListPost(any(OrganizationV3DirectoryKeysListPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getDirectoryPublicKeys(directoryId);
        verify(transport)
                .organizationV3DirectoryKeysListPost(any(OrganizationV3DirectoryKeysListPostRequest.class), entityCaptor.capture());
        assertEquals(organizationId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsDirectoryIdInRequest() throws Exception {
        client.getDirectoryPublicKeys(directoryId);
        verify(transport).organizationV3DirectoryKeysListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(directoryId, requestCaptor.getValue().getDirectoryId());
    }

    @Test
    public void includesAllKeysInTheResponse() throws Exception {
        List<PublicKey> expected = Arrays.asList(
                new PublicKey("ID 1", true, new Date(1000L), new Date(2000L)),
                new PublicKey("ID 2", false, new Date(3000L), new Date(4000L)));
        when(response.getPublicKeys()).thenReturn(Arrays.asList(
                new KeysListPostResponsePublicKey("ID 1", "Key 1", new Date(1000L), new Date(2000L), true, 0),
                new KeysListPostResponsePublicKey("ID 2", "Key 2", new Date(3000L), new Date(4000L), true, 0)));
        List<PublicKey> actual = client.getDirectoryPublicKeys(directoryId);
        assertEquals(expected, actual);
    }
}