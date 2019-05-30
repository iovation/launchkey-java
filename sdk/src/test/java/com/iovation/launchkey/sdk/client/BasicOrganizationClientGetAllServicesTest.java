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

import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServicesGetResponse;
import com.iovation.launchkey.sdk.transport.domain.ServicesGetResponseService;
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientGetAllServicesTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private ServicesGetResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicesListPostRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3ServicesGet(any(EntityIdentifier.class)))
                .thenReturn(response);
        when(response.getServices()).thenReturn(new ArrayList<ServicesGetResponseService>());
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getAllServices();
        verify(transport).organizationV3ServicesGet(entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getAllServices();
        verify(transport).organizationV3ServicesGet(entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void includesAllServicesInTheResponse() throws Exception {
        List<Service> expected = Arrays.asList(
                new Service(UUID.fromString("f4c24794-aefc-11e7-8d36-0469f8dc10a5"), "name1", "description1",
                        URI.create("https://foo.bar"), URI.create("https://fizz.buzz"), true),
                new Service(UUID.fromString("f6d44d14-aefc-11e7-9200-0469f8dc10a5"), "name2", "description2",
                        URI.create("https://bar.foo"), URI.create("https://buzz.fizz"), true)
        );
        when(response.getServices()).thenReturn(Arrays.asList(
                new ServicesGetResponseService(UUID.fromString("f4c24794-aefc-11e7-8d36-0469f8dc10a5"), "name1",
                        "description1", URI.create("https://foo.bar"), URI.create("https://fizz.buzz"), true),
                new ServicesGetResponseService(UUID.fromString("f6d44d14-aefc-11e7-9200-0469f8dc10a5"), "name2",
                        "description2", URI.create("https://bar.foo"), URI.create("https://buzz.fizz"), true)
        ));
        List<Service> actual = client.getAllServices();
        assertEquals(expected, actual);
    }
}