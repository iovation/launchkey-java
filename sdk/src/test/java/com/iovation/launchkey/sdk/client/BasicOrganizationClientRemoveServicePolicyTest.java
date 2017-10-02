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
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyDeleteRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientRemoveServicePolicyTest {
    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicePolicyDeleteRequest> requestCaptor;

    private static final UUID organizationId = UUID.randomUUID();
    private static final UUID serviceId = UUID.randomUUID();
    private BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(organizationId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.removeServicePolicy(serviceId);
        verify(transport)
                .organizationV3ServicePolicyDelete(any(ServicePolicyDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.removeServicePolicy(serviceId);
        verify(transport)
                .organizationV3ServicePolicyDelete(any(ServicePolicyDeleteRequest.class), entityCaptor.capture());
        assertEquals(organizationId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.removeServicePolicy(serviceId);
        verify(transport).organizationV3ServicePolicyDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }
}