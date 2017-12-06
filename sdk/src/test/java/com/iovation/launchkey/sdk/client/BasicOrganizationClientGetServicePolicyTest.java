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

import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Location;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.TimeFence;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicy;
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
    private ServicePolicy response;

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
        when(transport.organizationV3ServicePolicyItemPost(any(ServicePolicyItemPostRequest.class),
                any(EntityIdentifier.class))).thenReturn(response);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getServicePolicy(serviceId);
        verify(transport)
                .organizationV3ServicePolicyItemPost(any(ServicePolicyItemPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getServicePolicy(serviceId);
        verify(transport)
                .organizationV3ServicePolicyItemPost(any(ServicePolicyItemPostRequest.class), entityCaptor.capture());
        assertEquals(organizationId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.getServicePolicy(serviceId);
        verify(transport).organizationV3ServicePolicyItemPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void getsRequiredFactorsFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 9, 0, 0, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        assertEquals(Integer.valueOf(9), client.getServicePolicy(serviceId).getRequiredFactors());
    }

    @Test
    public void getsKnowledgeRequiredFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 0, 1, 0, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        assertEquals(true, client.getServicePolicy(serviceId).isKnowledgeFactorRequired());
    }

    @Test
    public void getsInherenceRequiredFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 1, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        assertEquals(true, client.getServicePolicy(serviceId).isInherenceFactorRequired());
    }

    @Test
    public void getsPossessionRequiredFromMinimumRequirements() throws Exception {
        List<AuthPolicy.MinimumRequirement> minimum = Collections.singletonList(new AuthPolicy.MinimumRequirement(
                AuthPolicy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 1));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        assertEquals(true, client.getServicePolicy(serviceId).isPossessionFactorRequired());
    }

    @Test
    public void getsTimeFences() throws Exception {
        List<ServicePolicy.TimeFence> fences = Arrays.asList(
                new ServicePolicy.TimeFence("Name 1", Arrays.asList("Monday", "Tuesday"), 1, 2, 3, 4,
                        "America/Los_Angeles"),
                new ServicePolicy.TimeFence("Name 2", Arrays.asList("Wednesday", "Sunday"), 4, 3, 2, 1,
                        "America/New_York")
        );
        List<TimeFence> expected = Arrays.asList(
                new TimeFence("Name 1", Arrays.asList(Day.MONDAY, Day.TUESDAY), 1, 3, 2, 4,
                        TimeZone.getTimeZone("America/Los_Angeles")),
                new TimeFence("Name 2", Arrays.asList(Day.WEDNESDAY, Day.SUNDAY), 4, 2, 3, 1,
                        TimeZone.getTimeZone("America/New_York"))
        );
        when(response.getTimeFences()).thenReturn(fences);
        List<TimeFence> actual = client.getServicePolicy(serviceId).getTimeFences();
        assertEquals(expected, actual);
    }

    @Test
    public void getsGeoFences() throws Exception {
        List<ServicePolicy.Location> fences = Arrays.asList(
                new ServicePolicy.Location("Location 1", 1.1, 1.2, 1.3),
                new ServicePolicy.Location("Location 2", 2.1, 2.2, 2.3)
        );
        List<Location> expected = Arrays.asList(
                new Location("Location 1", 1.1, 1.2, 1.3),
                new Location("Location 2", 2.1, 2.2, 2.3)
        );
        when(response.getGeoFences()).thenReturn(fences);
        assertEquals(expected, client.getServicePolicy(serviceId).getLocations());
    }

    @Test
    public void getsDeviceIntegrityAsJailBreakProtection() throws Exception {
        when(response.getDeviceIntegrity()).thenReturn(true);
        assertEquals(true, client.getServicePolicy(serviceId).isJailBreakProtectionEnabled());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void getRequiredFactorsNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        assertNull(client.getServicePolicy(serviceId).getRequiredFactors());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isInherenceFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        assertNull(client.getServicePolicy(serviceId).isInherenceFactorRequired());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isKnowledgeFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        assertNull(client.getServicePolicy(serviceId).isKnowledgeFactorRequired());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isPossessionFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        when(response.getMinimumRequirements()).thenReturn(new ArrayList<AuthPolicy.MinimumRequirement>());
        when(response.getTimeFences()).thenReturn(new ArrayList<ServicePolicy.TimeFence>());
        when(response.getGeoFences()).thenReturn(new ArrayList<ServicePolicy.Location>());
        when(response.getDeviceIntegrity()).thenReturn(null);
        assertNull(client.getServicePolicy(serviceId).isPossessionFactorRequired());
    }
}