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
import com.iovation.launchkey.sdk.transport.domain.ServicesPostRequest;
import com.iovation.launchkey.sdk.transport.domain.ServicesPostResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientCreateServiceTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private ServicesPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicesPostRequest> requestCaptor;

    public BasicOrganizationClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3ServicesPost(any(ServicesPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.createService(null, null, null, null, false);
        verify(transport).organizationV3ServicesPost(any(ServicesPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.createService(null, null, null, null, false);
        verify(transport).organizationV3ServicesPost(any(ServicesPostRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsName() throws Exception {
        client.createService("Expected Name", null, null, null, false);
        verify(transport).organizationV3ServicesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Name", requestCaptor.getValue().getName());
    }

    @Test
    public void sendsIcon() throws Exception {
        URI expected = URI.create("https://foo.bar");
        client.createService(null, null, expected, null, false);
        verify(transport).organizationV3ServicesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getIcon());
    }

    @Test
    public void sendsDescription() throws Exception {
        client.createService(null, "Expected Description", null, null, false);
        verify(transport).organizationV3ServicesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Description", requestCaptor.getValue().getDescription());
    }

    @Test
    public void sendsCallbackURL() throws Exception {
        URI expected = URI.create("https://foo.bar");
        client.createService(null, null, null, expected, false);
        verify(transport).organizationV3ServicesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getCallbackURL());
    }

    @Test
    public void sendsActive() throws Exception {
        client.createService(null, null, null, null, true);
        verify(transport).organizationV3ServicesPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }

    @Test
    public void returnsId() throws Exception {
        UUID expected = UUID.randomUUID();
        when(response.getId()).thenReturn(expected);
        UUID actual = client.createService(null, null, null, null, true);
        assertEquals(expected, actual);
    }
}