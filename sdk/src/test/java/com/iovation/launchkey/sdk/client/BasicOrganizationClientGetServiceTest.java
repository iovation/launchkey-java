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
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostResponseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientGetServiceTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private ServicesListPostResponse response;

    @Mock
    ServicesListPostResponseService responseService;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicesListPostRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3ServicesListPost(any(ServicesListPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
        when(response.getServices()).thenReturn(Collections.singletonList(responseService));
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getService(null);
        verify(transport).organizationV3ServicesListPost(any(ServicesListPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getService(null);
        verify(transport).organizationV3ServicesListPost(any(ServicesListPostRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        UUID id = UUID.randomUUID();
        client.getService(id);
        verify(transport).organizationV3ServicesListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Collections.singletonList(id), requestCaptor.getValue().getServiceIds());
    }

    @Test
    public void getsServiceIdFromResponseListService() throws Exception {
        UUID expected = UUID.randomUUID();
        when(responseService.getId()).thenReturn(expected);
        assertEquals(expected, client.getService(null).getId());
    }

    @Test
    public void getsServiceNameFromResponseListService() throws Exception {
        when(responseService.getName()).thenReturn("Name");
        assertEquals("Name", client.getService(null).getName());
    }

    @Test
    public void getsServiceDescriptionFromResponseListService() throws Exception {
        when(responseService.getDescription()).thenReturn("description");
        assertEquals("description", client.getService(null).getDescription());
    }

    @Test
    public void getsServiceIconFromResponseListService() throws Exception {
        when(responseService.getIcon()).thenReturn(URI.create("https://foo.bar"));
        assertEquals(URI.create("https://foo.bar"), client.getService(null).getIcon());
    }

    @Test
    public void getsServiceCallbackUrlFromResponseListService() throws Exception {
        when(responseService.getCallbackURL()).thenReturn(URI.create("https://foo.bar"));
        assertEquals(URI.create("https://foo.bar"), client.getService(null).getCallbackURL());
    }

    @Test
    public void getsServiceActiveFromResponseListService() throws Exception {
        when(responseService.isActive()).thenReturn(true);
        assertTrue(client.getService(null).isActive());
    }
}