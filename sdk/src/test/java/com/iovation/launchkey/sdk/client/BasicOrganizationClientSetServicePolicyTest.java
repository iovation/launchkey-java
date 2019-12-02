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

import com.fasterxml.jackson.databind.node.*;
import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.AuthPolicy;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyPutRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientSetServicePolicyTest {
    @Mock
    private Transport transport;

    @Mock
    private ServicePolicy servicePolicy;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicePolicyPutRequest> requestCaptor;

    private static final UUID organizationId = UUID.randomUUID();
    private static final UUID serviceId = UUID.randomUUID();
    private BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicOrganizationClient(organizationId, transport);
        when(servicePolicy.getLocations()).thenReturn(new ArrayList<ServicePolicy.Location>());
        when(servicePolicy.getTimeFences()).thenReturn(new ArrayList<ServicePolicy.TimeFence>());
    }

    @Test

    public void sendsSubjectEntityType() throws Exception {
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(any(ServicePolicyPutRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(any(ServicePolicyPutRequest.class), entityCaptor.capture());
        assertEquals(organizationId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void setsLocations() throws Exception {
        when(servicePolicy.getLocations()).thenReturn(Arrays.asList(
                new ServicePolicy.Location("Location 1", 1.1, 1.2, 1.3),
                new ServicePolicy.Location("Location 2", 2.1, 2.2, 2.3)
        ));
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expected = new ArrayNode(jnf) {{
            ObjectNode geofence = jnf.objectNode();
            geofence.set("factor", new TextNode("geofence"));
            geofence.set("requirement", new TextNode("forced requirement"));
            geofence.set("priority", new IntNode(1));
            ObjectNode geoFenceAttributes = jnf.objectNode();
            ArrayNode locations = new ArrayNode(jnf) {{
                ObjectNode location1 = jnf.objectNode();
                location1.set("name", new TextNode("Location 1"));
                location1.set("radius", new DoubleNode(1.1));
                location1.set("latitude", new DoubleNode(1.2));
                location1.set("longitude", new DoubleNode(1.3));
                add(location1);
                ObjectNode location2 = jnf.objectNode();
                location2.set("name", new TextNode("Location 2"));
                location2.set("radius", new DoubleNode(2.1));
                location2.set("latitude", new DoubleNode(2.2));
                location2.set("longitude", new DoubleNode(2.3));
                add(location2);
            }};
            geoFenceAttributes.set("locations", locations);
            geofence.set("attributes", geoFenceAttributes);
            add(geofence);
        }};
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getPolicy().getFactors());
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
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expected = new ArrayNode(jnf) {{
            ObjectNode timeFence = jnf.objectNode();
            timeFence.set("factor", new TextNode("timefence"));
            timeFence.set("requirement", new TextNode("forced requirement"));
            timeFence.set("priority", new IntNode(1));
            ObjectNode timeFenceAttributes = jnf.objectNode();
            timeFenceAttributes.set("time fences", new ArrayNode(jnf) {{
                ObjectNode fence1 = jnf.objectNode();
                fence1.set("name", new TextNode("Fence 1"));
                fence1.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Monday"));
                    add(new TextNode("Tuesday"));
                }});
                fence1.set("start hour", new IntNode(1));
                fence1.set("end hour", new IntNode(3));
                fence1.set("start minute", new IntNode(2));
                fence1.set("end minute", new IntNode(4));
                fence1.set("timezone", new TextNode("America/Los_Angeles"));
                add(fence1);
                ObjectNode fence2 = jnf.objectNode();
                fence2.set("name", new TextNode("Fence 2"));
                fence2.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Friday"));
                    add(new TextNode("Sunday"));
                }});
                fence2.set("start hour", new IntNode(4));
                fence2.set("end hour", new IntNode(2));
                fence2.set("start minute", new IntNode(3));
                fence2.set("end minute", new IntNode(1));
                fence2.set("timezone", new TextNode("America/New_York"));
                add(fence2);
            }});
            timeFence.set("attributes", timeFenceAttributes);
            add(timeFence);
        }};
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getPolicy().getFactors());
    }

    @Test
    public void setsMinimumRequirements() throws Exception {
        when(servicePolicy.isInherenceFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("No minimum requirements found", 1,
                requestCaptor.getValue().getPolicy().getMinimumRequirements().size());
        assertNotNull("Minimum requirements are null",
                requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0));
    }

    @Test
    public void setsMinimumRequirementsInherenceAsOneForTrue() throws Exception {
        when(servicePolicy.isInherenceFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(1), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getInherence());
    }

    @Test
    public void setsMinimumRequirementsInherenceAsZeroForFalse() throws Exception {
        when(servicePolicy.getRequiredFactors()).thenReturn(1);
        when(servicePolicy.isInherenceFactorRequired()).thenReturn(false);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(0), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getInherence());
    }

    @Test
    public void setsMinimumRequirementsKnowledgeAsOneForTrue() throws Exception {
        when(servicePolicy.isKnowledgeFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(1), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getKnowledge());
    }

    @Test
    public void setsMinimumRequirementsKnowledgeAsZeroForFalse() throws Exception {
        when(servicePolicy.getRequiredFactors()).thenReturn(1);
        when(servicePolicy.isKnowledgeFactorRequired()).thenReturn(false);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(0), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getKnowledge());
    }

    @Test
    public void setsMinimumRequirementsPossessionAsOneForTrue() throws Exception {
        when(servicePolicy.isPossessionFactorRequired()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(1), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getPossession());
    }

    @Test
    public void setsMinimumRequirementsPossessionAsZeroForFalse() throws Exception {
        when(servicePolicy.getRequiredFactors()).thenReturn(1);
        when(servicePolicy.isPossessionFactorRequired()).thenReturn(false);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(0), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getPossession());
    }

    @Test
    public void setsMinimumRequirementsAnyAsSameInteger() throws Exception {
        when(servicePolicy.getRequiredFactors()).thenReturn(4);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(4), requestCaptor.getValue().getPolicy().getMinimumRequirements().get(0).getAny());
    }

    @Test
    public void setsDeviceIntegrityFromJailBreakProtectionEnabled() throws Exception {
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode expected = new ArrayNode(jnf) {{
            ObjectNode deviceIntegrity = jnf.objectNode();
            deviceIntegrity.set("factor", new TextNode("device integrity"));
            deviceIntegrity.set("requirement", new TextNode("forced requirement"));
            deviceIntegrity.set("priority", new IntNode(1));
            ObjectNode deviceIntegrityAttributes = jnf.objectNode();
            deviceIntegrityAttributes.set("factor enabled", new IntNode(1));
            deviceIntegrity.set("attributes", deviceIntegrityAttributes);
            add(deviceIntegrity);
        }};
        when(servicePolicy.isJailBreakProtectionEnabled()).thenReturn(true);
        client.setServicePolicy(serviceId, servicePolicy);
        verify(transport).organizationV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(expected, requestCaptor.getValue().getPolicy().getFactors());
    }
}