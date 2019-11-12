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
import com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy;
import com.iovation.launchkey.sdk.domain.policy.FactorsPolicy;
import com.iovation.launchkey.sdk.domain.policy.MethodAmountPolicy;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.Fence;
import com.iovation.launchkey.sdk.transport.domain.Policy;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyItemPostRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientGetAdvancedServicePolicyTest {
    private Transport transport;
    private Policy response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServicePolicyItemPostRequest> requestCaptor;

    private static final UUID directoryId = UUID.randomUUID();
    private static final UUID serviceId = UUID.randomUUID();
    private BasicDirectoryClient client;

    @Before
    public void setUp() throws Exception {
        response = mock(Policy.class);
        transport = mock(Transport.class);
        when(transport.directoryV3ServicePolicyItemPost(
                any(ServicePolicyItemPostRequest.class),
                any(EntityIdentifier.class))
        ).thenReturn(response);
        client = new BasicDirectoryClient(directoryId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.getAdvancedServicePolicy(serviceId);
        verify(transport)
                .directoryV3ServicePolicyItemPost(any(ServicePolicyItemPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.getAdvancedServicePolicy(serviceId);
        verify(transport)
                .directoryV3ServicePolicyItemPost(any(ServicePolicyItemPostRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.getAdvancedServicePolicy(serviceId);
        verify(transport).directoryV3ServicePolicyItemPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void getsKnowledgeRequiredFromFactorsPolicy() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_FACTORS);
        when(response.getFactors()).thenReturn(new ArrayNode(JsonNodeFactory.instance) {{
            add(new TextNode("KNOWLEDGE"));
        }});
        FactorsPolicy policy = (FactorsPolicy) client.getAdvancedServicePolicy(serviceId);
        assertTrue(policy.isKnowledgeRequired());
    }

    @Test
    public void getsInherenceRequiredFromFactorsPolicy() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_FACTORS);
        when(response.getFactors()).thenReturn(new ArrayNode(JsonNodeFactory.instance) {{
            add(new TextNode("INHERENCE"));
        }});
        FactorsPolicy policy = (FactorsPolicy) client.getAdvancedServicePolicy(serviceId);
        assertTrue(policy.isInherenceRequired());
    }

    @Test
    public void getsPossessionRequiredFromFactorsPolicy() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_FACTORS);
        when(response.getFactors()).thenReturn(new ArrayNode(JsonNodeFactory.instance) {{
            add(new TextNode("POSSESSION"));
        }});
        FactorsPolicy policy = (FactorsPolicy) client.getAdvancedServicePolicy(serviceId);
        assertTrue(policy.isPossessionRequired());
    }

    @Test
    public void getsFencesFromMethodAmountPolicy() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_METHOD_AMOUNT);
        when(response.getAmount()).thenReturn(0);
        when(response.getFences()).thenReturn(new ArrayList<Fence>() {{
            add(new Fence("G1", Fence.TYPE_GEO_CIRCLE, 1.1, 1.2, 1.3, null, null, null));
            add(new Fence("T1", Fence.TYPE_TERRITORY, null, null, null, "c", "aa", "pc"));
        }});
        List<com.iovation.launchkey.sdk.domain.policy.Fence> expected =
                new ArrayList<com.iovation.launchkey.sdk.domain.policy.Fence>() {{
                    add(new com.iovation.launchkey.sdk.domain.policy.GeoCircleFence("G1", 1.1, 1.2, 1.3));
                    add(new com.iovation.launchkey.sdk.domain.policy.TerritoryFence("T1", "c", "aa", "pc"));
                }};
        MethodAmountPolicy policy = (MethodAmountPolicy) client.getAdvancedServicePolicy(serviceId);
        assertEquals(expected, policy.getFences());
    }

    @Test
    public void getsDenyRootedJailbrokenFromMethodAmountPolicy() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_METHOD_AMOUNT);
        when(response.getAmount()).thenReturn(0);
        when(response.getDenyRootedJailbroken()).thenReturn(true);
        MethodAmountPolicy policy = (MethodAmountPolicy) client.getAdvancedServicePolicy(serviceId);
        assertTrue(policy.getDenyRootedJailbroken());
    }

    @Test
    public void getsDenyEmulatorSimulatorFromMethodAmountPolicy() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_METHOD_AMOUNT);
        when(response.getAmount()).thenReturn(0);
        when(response.getDenyEmulatorSimulator()).thenReturn(true);
        MethodAmountPolicy policy = (MethodAmountPolicy) client.getAdvancedServicePolicy(serviceId);
        assertTrue(policy.getDenyEmulatorSimulator());
    }

    @Test
    public void setsInsidePolicyFencesToNoneWhenEmpty() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_COND_GEO);
        when(response.getFences()).thenReturn(new ArrayList<Fence>(){{
            add(new Fence("name", Fence.TYPE_TERRITORY, null, null, null, "country", null, null));
        }});
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null, null, new ArrayList<Fence>(),
                null, 0, null, null, null);
        when(response.getInPolicy()).thenReturn(policy);
        when(response.getOutPolicy()).thenReturn(policy);
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy actual =
                (ConditionalGeoFencePolicy) client.getAdvancedServicePolicy(serviceId);
        assertNull(actual.getInside().getFences());
    }

    @Test
    public void setsInsidePolicyDenyRootedJailbrokenToFalse() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_COND_GEO);
        when(response.getFences()).thenReturn(new ArrayList<Fence>(){{
            add(new Fence("name", Fence.TYPE_TERRITORY, null, null, null, "country", null, null));
        }});
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null, null, null, null, 0, null, null, null);
        when(response.getInPolicy()).thenReturn(policy);
        when(response.getOutPolicy()).thenReturn(policy);
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy actual =
                (ConditionalGeoFencePolicy) client.getAdvancedServicePolicy(serviceId);
        assertFalse(actual.getInside().getDenyRootedJailbroken());
    }

    @Test
    public void setsInsidePolicyDenySimulatorEmulatorToFalse() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_COND_GEO);
        when(response.getFences()).thenReturn(new ArrayList<Fence>(){{
            add(new Fence("name", Fence.TYPE_TERRITORY, null, null, null, "country", null, null));
        }});
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null, null, null, null, 0, null, null, null);
        when(response.getInPolicy()).thenReturn(policy);
        when(response.getOutPolicy()).thenReturn(policy);
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy actual =
                (ConditionalGeoFencePolicy) client.getAdvancedServicePolicy(serviceId);
        assertFalse(actual.getInside().getDenyEmulatorSimulator());
    }

    @Test
    public void setsOutsidePolicyFencesToNoneWhenEmpty() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_COND_GEO);
        when(response.getFences()).thenReturn(new ArrayList<Fence>(){{
            add(new Fence("name", Fence.TYPE_TERRITORY, null, null, null, "country", null, null));
        }});
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null, null, new ArrayList<Fence>(),
                null, 0, null, null, null);
        when(response.getInPolicy()).thenReturn(policy);
        when(response.getOutPolicy()).thenReturn(policy);
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy actual =
                (ConditionalGeoFencePolicy) client.getAdvancedServicePolicy(serviceId);
        assertNull(actual.getOutside().getFences());
    }

    @Test
    public void setsOutsidePolicyDenyRootedJailbrokenToNoneWhenFalse() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_COND_GEO);
        when(response.getFences()).thenReturn(new ArrayList<Fence>(){{
            add(new Fence("name", Fence.TYPE_TERRITORY, null, null, null, "country", null, null));
        }});
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null, null, null, null, 0, null, null, null);
        when(response.getInPolicy()).thenReturn(policy);
        when(response.getOutPolicy()).thenReturn(policy);
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy actual =
                (ConditionalGeoFencePolicy) client.getAdvancedServicePolicy(serviceId);
        assertFalse(actual.getOutside().getDenyRootedJailbroken());
    }

    @Test
    public void setsOutsidePolicyDenySimulatorEmulatorToNoneWhenFalse() throws Exception {
        when(response.getPolicyType()).thenReturn(Policy.TYPE_COND_GEO);
        when(response.getFences()).thenReturn(new ArrayList<Fence>(){{
            add(new Fence("name", Fence.TYPE_TERRITORY, null, null, null, "country", null, null));
        }});
        Policy policy = new Policy(Policy.TYPE_METHOD_AMOUNT, null, null, null, null, 0, null, null, null);
        when(response.getInPolicy()).thenReturn(policy);
        when(response.getOutPolicy()).thenReturn(policy);
        com.iovation.launchkey.sdk.domain.policy.ConditionalGeoFencePolicy actual =
                (ConditionalGeoFencePolicy) client.getAdvancedServicePolicy(serviceId);
        assertFalse(actual.getOutside().getDenyEmulatorSimulator());
    }
}