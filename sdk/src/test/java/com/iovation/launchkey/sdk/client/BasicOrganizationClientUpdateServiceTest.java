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
import com.iovation.launchkey.sdk.transport.domain.*;
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

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientUpdateServiceTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private ServicesPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicesPatchRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
    }


    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.updateService(null, null, null, null, null, false);
        verify(transport).organizationV3ServicesPatch(any(ServicesPatchRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.updateService(null, null, null, null, null, false);
        verify(transport).organizationV3ServicesPatch(any(ServicesPatchRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        UUID id = UUID.randomUUID();
        client.updateService(id, null, null, null, null, false);
        verify(transport).organizationV3ServicesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void sendsName() throws Exception {
        client.updateService(null, "name", null, null, null, false);
        verify(transport).organizationV3ServicesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("name", requestCaptor.getValue().getName());
    }

    @Test
    public void sendsDescription() throws Exception {
        client.updateService(null, null, "description", null, null, false);
        verify(transport).organizationV3ServicesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("description", requestCaptor.getValue().getDescription());
    }

    @Test
    public void sendsIcon() throws Exception {
        client.updateService(null, null, null, URI.create("https://foo.bar"), null, false);
        verify(transport).organizationV3ServicesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(URI.create("https://foo.bar"), requestCaptor.getValue().getIcon());
    }

    @Test
    public void sendsCallbackURL() throws Exception {
        client.updateService(null, null, null, null, URI.create("https://foo.bar"), false);
        verify(transport).organizationV3ServicesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(URI.create("https://foo.bar"), requestCaptor.getValue().getCallbackURL());
    }

    @Test
    public void sendsActive() throws Exception {
        client.updateService(null, null, null, null, null, true);
        verify(transport).organizationV3ServicesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }
}