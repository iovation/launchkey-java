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


import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyItemPostRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientGetServicePolicyTest {
    @Mock
    private Transport transport;

    @Mock
    private com.iovation.launchkey.sdk.transport.domain.ServicePolicy response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicePolicyItemPostRequest> requestCaptor;

    private static final UUID organizationId = UUID.randomUUID();
    private static final UUID serviceId = UUID.randomUUID();
    private BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(organizationId, transport);
        when(transport.organizationV3PolicyItemPost(any(ServicePolicyItemPostRequest.class),
                any(EntityIdentifier.class))).thenReturn(response);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getServicePolicy(serviceId);
        verify(transport)
                .organizationV3PolicyItemPost(any(ServicePolicyItemPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getServicePolicy(serviceId);
        verify(transport)
                .organizationV3PolicyItemPost(any(ServicePolicyItemPostRequest.class), entityCaptor.capture());
        assertEquals(organizationId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.getServicePolicy(serviceId);
        verify(transport).organizationV3PolicyItemPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void getsRequiredFactorsFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 9, 0, 0, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(Integer.valueOf(9), servicePolicy.getRequiredFactors());
    }

    @Test
    public void getsKnowledgeRequiredFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 0, 1, 0, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isKnowledgeFactorRequired());
    }

    @Test
    public void getsInherenceRequiredFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 1, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isInherenceFactorRequired());
    }

    @Test
    public void getsPossessionRequiredFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 1));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isPossessionFactorRequired());
    }

    @Test
    public void getsTimeFences() throws Exception {
        List<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence> fences = Arrays.asList(
                new com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence("Name 1", Arrays.asList("Monday", "Tuesday"), 1, 2, 3, 4,
                        "America/Los_Angeles"),
                new com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence("Name 2", Arrays.asList("Wednesday", "Sunday"), 4, 3, 2, 1,
                        "America/New_York")
        );
        List<com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.TimeFence> expected = Arrays.asList(
                new com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.TimeFence("Name 1", Arrays.asList(com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day.MONDAY, com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day.TUESDAY), 1, 3, 2, 4,
                        TimeZone.getTimeZone("America/Los_Angeles")),
                new com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.TimeFence("Name 2", Arrays.asList(com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day.WEDNESDAY, com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day.SUNDAY), 4, 2, 3, 1,
                        TimeZone.getTimeZone("America/New_York"))
        );
        when(response.getTimeFences()).thenReturn(fences);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        List<com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.TimeFence> actual = servicePolicy.getTimeFences();
        assertEquals(expected, actual);
    }

    @Test
    public void getsGeoFences() throws Exception {
        List<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location> fences = Arrays.asList(
                new com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location("Location 1", 1.1, 1.2, 1.3),
                new com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location("Location 2", 2.1, 2.2, 2.3)
        );
        List<com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Location> expected = Arrays.asList(
                new com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Location("Location 1", 1.1, 1.2, 1.3),
                new com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Location("Location 2", 2.1, 2.2, 2.3)
        );
        when(response.getGeoFences()).thenReturn(fences);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(expected, servicePolicy.getLocations());
    }

    @Test
    public void getsDeviceIntegrityAsJailBreakProtection() throws Exception {
        when(response.getDeviceIntegrity()).thenReturn(true);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isJailBreakProtectionEnabled());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void getRequiredFactorsNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.getRequiredFactors());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isInherenceFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isInherenceFactorRequired());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isKnowledgeFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isKnowledgeFactorRequired());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isPossessionFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<com.iovation.launchkey.sdk.transport.domain.ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isPossessionFactorRequired());
    }
}