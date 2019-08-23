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
import com.iovation.launchkey.sdk.transport.domain.Policy;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyItemPostRequest;
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
public class BasicOrganizationClientGetServicePolicyTest {
    @Mock
    private Transport transport;

    @Mock
    private com.iovation.launchkey.sdk.transport.domain.Policy response;

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
        List<Policy.MinimumRequirement> minimum = Collections.singletonList(new Policy.MinimumRequirement(
                Policy.MinimumRequirement.Type.AUTHENTICATED, 9, 0, 0, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(Integer.valueOf(9), servicePolicy.getRequiredFactors());
    }

    @Test
    public void getsKnowledgeRequiredFromMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimum = Collections.singletonList(new Policy.MinimumRequirement(
                Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 1, 0, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isKnowledgeFactorRequired());
    }

    @Test
    public void getsInherenceRequiredFromMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimum = Collections.singletonList(new Policy.MinimumRequirement(
                Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 1, 0));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isInherenceFactorRequired());
    }

    @Test
    public void getsPossessionRequiredFromMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimum = Collections.singletonList(new Policy.MinimumRequirement(
                Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 1));
        when(response.getMinimumRequirements()).thenReturn(minimum);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isPossessionFactorRequired());
    }

    @Test
    public void getsTimeFences() throws Exception {
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        when(response.getFactors()).thenReturn(new ArrayNode(jnf) {{
            ObjectNode timeFence = jnf.objectNode();
            timeFence.set("factor", new TextNode("timefence"));
            timeFence.set("requirement", new TextNode("forced requirement"));
            timeFence.set("priority", new IntNode(1));
            ObjectNode timeFenceAttributes = jnf.objectNode();
            timeFenceAttributes.set("time fences", new ArrayNode(jnf) {{
                ObjectNode fence1 = jnf.objectNode();
                fence1.set("name", new TextNode("Name 1"));
                fence1.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Monday"));
                    add(new TextNode("Tuesday"));
                }});
                fence1.set("start hour", new IntNode(1));
                fence1.set("end hour", new IntNode(2));
                fence1.set("start minute", new IntNode(3));
                fence1.set("end minute", new IntNode(4));
                fence1.set("timezone", new TextNode("America/Los_Angeles"));
                add(fence1);
                ObjectNode fence2 = jnf.objectNode();
                fence2.set("name", new TextNode("Name 2"));
                fence2.set("days", new ArrayNode(jnf) {{
                    add(new TextNode("Wednesday"));
                    add(new TextNode("Sunday"));
                }});
                fence2.set("start hour", new IntNode(4));
                fence2.set("end hour", new IntNode(3));
                fence2.set("start minute", new IntNode(2));
                fence2.set("end minute", new IntNode(1));
                fence2.set("timezone", new TextNode("America/New_York"));
                add(fence2);
            }});
            timeFence.set("attributes", timeFenceAttributes);
            add(timeFence);
        }});
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        List<com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.TimeFence> actual =
                servicePolicy.getTimeFences();
        List<ServicePolicy.TimeFence> expected = Arrays.asList(
                new ServicePolicy.TimeFence("Name 1", Arrays.asList(
                        com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day.MONDAY,
                        com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy.Day.TUESDAY), 1, 3, 2, 4,
                        TimeZone.getTimeZone("America/Los_Angeles")),
                new ServicePolicy.TimeFence("Name 2", Arrays.asList(
                        ServicePolicy.Day.WEDNESDAY,
                        ServicePolicy.Day.SUNDAY), 4, 2, 3, 1,
                        TimeZone.getTimeZone("America/New_York"))
        );
        assertEquals(expected, actual);
    }

    @Test
    public void getsGeoFences() throws Exception {
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        when(response.getFactors()).thenReturn(new ArrayNode(jnf) {{
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
        }});
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        List<ServicePolicy.Location> expected = Arrays.asList(
                new ServicePolicy.Location("Location 1", 1.1, 1.2, 1.3),
                new ServicePolicy.Location("Location 2", 2.1, 2.2, 2.3)
        );
        assertEquals(expected, servicePolicy.getLocations());
    }

    @Test
    public void getsDeviceIntegrityAsJailBreakProtection() throws Exception {
        final JsonNodeFactory jnf = new JsonNodeFactory(true);
        ArrayNode factors = new ArrayNode(jnf) {{
            ObjectNode deviceIntegrity = jnf.objectNode();
            deviceIntegrity.set("factor", new TextNode("device integrity"));
            ObjectNode deviceIntegrityAttributes = jnf.objectNode();
            deviceIntegrityAttributes.set("factor enabled", new TextNode("1"));
            deviceIntegrity.set("attributes", deviceIntegrityAttributes);
            add(deviceIntegrity);
        }};
        when(response.getFactors()).thenReturn(factors);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertEquals(true, servicePolicy.isJailBreakProtectionEnabled());
    }

    @Test
    public void getsDeviceIntegrityAsNullWhenJailBreakProtectionIsFalse() throws Exception {
        when(response.getFactors()).thenReturn(new ArrayNode(JsonNodeFactory.instance));
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isJailBreakProtectionEnabled());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void getRequiredFactorsNullWhenNoMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>() {{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 0));
        }};
        when(response.getMinimumRequirements()).thenReturn(minimumRequirements);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.getRequiredFactors());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isInherenceFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>() {{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 0));
        }};
        when(response.getMinimumRequirements()).thenReturn(minimumRequirements);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isInherenceFactorRequired());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isKnowledgeFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>() {{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 0));
        }};
        when(response.getMinimumRequirements()).thenReturn(minimumRequirements);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isKnowledgeFactorRequired());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void isPossessionFactorRequiredNullWhenNoMinimumRequirements() throws Exception {
        List<Policy.MinimumRequirement> minimumRequirements = new ArrayList<Policy.MinimumRequirement>() {{
            add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.AUTHENTICATED, 0, 0, 0, 0));
        }};
        when(response.getMinimumRequirements()).thenReturn(minimumRequirements);
        ServicePolicy servicePolicy = client.getServicePolicy(serviceId);
        assertNull(servicePolicy.isPossessionFactorRequired());
    }
}