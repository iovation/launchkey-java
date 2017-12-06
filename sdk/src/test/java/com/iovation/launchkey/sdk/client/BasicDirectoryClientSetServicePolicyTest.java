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
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy.Location;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyPutRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientSetServicePolicyTest {
    @Mock
    private Transport transport;

    @Mock
    private ServicePolicy servicePolicy;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicePolicyPutRequest> requestCaptor;

    private static final UUID directoryId = UUID.randomUUID();
    private static final UUID serviceId = UUID.randomUUID();
    private BasicDirectoryClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
        when(servicePolicy.getLocations()).thenReturn(new ArrayList<ServicePolicy.Location>());
        when(servicePolicy.getTimeFences()).thenReturn(new ArrayList<ServicePolicy.TimeFence>());
    }

    @Test

    public void sendsSubjectEntityType() throws Exception {
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(any(ServicePolicyPutRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(any(ServicePolicyPutRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void setsLocations() throws Exception {
        when(servicePolicy.getLocations()).thenReturn(Arrays.asList(
                new ServicePolicy.Location("Location 1", 1.1, 1.2, 1.3),
                new ServicePolicy.Location("Location 2", 2.1, 2.2, 2.3)
        ));
        List<Location> expected = Arrays.asList(
                new Location("Location 1", 1.1, 1.2, 1.3),
                new Location("Location 2", 2.1, 2.2, 2.3)

        );
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getPolicy().getGeoFences());
    }

    @Test
    public void setsTimeFences() throws Exception {
        when(servicePolicy.getTimeFences()).thenReturn(Arrays.asList(
                new ServicePolicy.TimeFence("Fence 1",
                        Arrays.asList(ServicePolicy.Day.MONDAY, ServicePolicy.Day.TUESDAY), 1, 2, 3, 4,
                        TimeZone.getTimeZone("America/Los_Angeles")),
                new ServicePolicy.TimeFence("Fence 2",
                        Arrays.asList(ServicePolicy.Day.FRIDAY, ServicePolicy.Day.SUNDAY), 4, 3, 2, 1,
                        TimeZone.getTimeZone("America/New_York"))
        ));
        List<TimeFence> expected = Arrays.asList(
                new TimeFence("Fence 1", Arrays.asList("Monday", "Tuesday"), 1, 3, 2, 4, "America/Los_Angeles"),
                new TimeFence("Fence 2", Arrays.asList("Friday", "Sunday"), 4, 2, 3, 1, "America/New_York")

        );
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getPolicy().getTimeFences());
    }

    @Test
    public void setsMinimumRequirements() throws Exception {
        when(servicePolicy.isInherenceFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("No minimum requirements found", 1,
                requestCaptor.getValue().getPolicy().getMinimumRequirements().size());
        assertNotNull("Minimum requirements are null",
                requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0));
    }

    @Test
    public void setsMinimumRequirementsInherenceAsOneForTrue() throws Exception {
        when(servicePolicy.isInherenceFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(1), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getInherence());
    }

    @Test
    public void setsMinimumRequirementsInherenceAsZeroForFalse() throws Exception {
        when(servicePolicy.isInherenceFactorRequired()).thenReturn(false);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(0), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getInherence());
    }

    @Test
    public void setsMinimumRequirementsKnowledgeAsOneForTrue() throws Exception {
        when(servicePolicy.isKnowledgeFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(1), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getKnowledge());
    }

    @Test
    public void setsMinimumRequirementsKnowledgeAsZeroForFalse() throws Exception {
        when(servicePolicy.isKnowledgeFactorRequired()).thenReturn(false);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(0), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getKnowledge());
    }

    @Test
    public void setsMinimumRequirementsPossessionAsOneForTrue() throws Exception {
        when(servicePolicy.isPossessionFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(1), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getPossession());
    }

    @Test
    public void setsMinimumRequirementsPossessionAsZeroForFalse() throws Exception {
        when(servicePolicy.isPossessionFactorRequired()).thenReturn(false);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(0), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getPossession());
    }

    @Test
    public void setsMinimumRequirementsAnyAsSameInteger() throws Exception {
        when(servicePolicy.getRequiredFactors()).thenReturn(4);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(4), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getAny());
    }

    @Test
    public void setsDeviceIntegrityFromJailBreakProtectionEnabled() throws Exception {
        when(servicePolicy.isJailBreakProtectionEnabled()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(true, requestCaptor.getValue().getPolicy().getDeviceIntegrity());
    }
}