package com.iovation.launchkey.sdk.client;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.iovation.launchkey.sdk.domain.policy.*;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyPutRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.UUID;

import static com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_GEO_CIRCLE;
import static com.iovation.launchkey.sdk.transport.domain.Fence.TYPE_TERRITORY;
import static com.iovation.launchkey.sdk.transport.domain.Fence.FACTOR_INHERENCE;
import static com.iovation.launchkey.sdk.transport.domain.Fence.FACTOR_KNOWLEDGE;
import static com.iovation.launchkey.sdk.transport.domain.Fence.FACTOR_POSSESSION;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientSetAdvancedServicePolicyTest {
    @Mock
    private Transport transport;

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
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new MethodAmountPolicy(false, false, null, 0));
        verify(transport)
                .directoryV3ServicePolicyPut(any(ServicePolicyPutRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new MethodAmountPolicy(false, false, null, 0));
        verify(transport)
                .directoryV3ServicePolicyPut(any(ServicePolicyPutRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsServiceId() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new MethodAmountPolicy(false, false, null, 0));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(serviceId, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void sendsDenyRootedJailbroken() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new MethodAmountPolicy(true, false, null, 0));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().getPolicy().getDenyRootedJailbroken());
    }

    @Test
    public void sendsDenyEmulatorSimulator() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new MethodAmountPolicy(false, true, null, 0));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().getPolicy().getDenyEmulatorSimulator());
    }

    @Test
    public void sendsFencesWhenPresent() throws Exception {
        Policy policy = new MethodAmountPolicy(false, false, new ArrayList<Fence>() {{
            add(new GeoCircleFence("G-Fence", 1.0, 2.0, 3.0));
            add(new TerritoryFence("T-Fence", "country", "admin", "postal"));
        }}, 0);
        client.setAdvancedServicePolicy(serviceId, policy);
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertThat(requestCaptor.getValue().getPolicy().getFences(), hasItem(
                new com.iovation.launchkey.sdk.transport.domain.Fence(
                        "G-Fence", TYPE_GEO_CIRCLE, 1.0, 2.0, 3.0, null, null, null)));
        assertThat(requestCaptor.getValue().getPolicy().getFences(), hasItem(
                new com.iovation.launchkey.sdk.transport.domain.Fence(
                        "T-Fence", TYPE_TERRITORY, null, null, null, "country", "admin", "postal")));
    }

    @Test
    public void sendsAmountForMethodAmountPolicy() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new MethodAmountPolicy(false, true, null, 22));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(22), requestCaptor.getValue().getPolicy().getAmount());
    }

    @Test
    public void sendsTypesForFactorsPolicy() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new FactorsPolicy(false, false, null, true, true, true));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        JsonNodeFactory nf = new JsonNodeFactory(true);
        ArrayNode expected = new ArrayNode(nf);
        expected.add(FACTOR_INHERENCE);
        expected.add(FACTOR_KNOWLEDGE);
        expected.add(FACTOR_POSSESSION);

        assertEquals(expected, requestCaptor.getValue().getPolicy().getFactors());
    }

    @Test
    public void sendsNullForDenyRootedJailbrokenOfInsidePolicyOfCondGeo() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new ConditionalGeoFencePolicy(false, false, null,
                new MethodAmountPolicy(false, false, null, 0),
                new MethodAmountPolicy(false, false, null, 0)
        ));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getInPolicy().getDenyRootedJailbroken());
    }

    @Test
    public void sendsNullForDenyRootedJailbrokenOfOutsidePolicyOfCondGeo() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new ConditionalGeoFencePolicy(false, false, null,
                new MethodAmountPolicy(false, false, null, 0),
                new MethodAmountPolicy(false, false, null, 0)
        ));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getOutPolicy().getDenyRootedJailbroken());
    }

    @Test
    public void sendsNullForDenyEmulatorSimulatorOfInsidePolicyOfCondGeo() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new ConditionalGeoFencePolicy(false, false, null,
                new MethodAmountPolicy(false, false, null, 0),
                new MethodAmountPolicy(false, false, null, 0)
        ));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getInPolicy().getDenyEmulatorSimulator());
    }

    @Test
    public void sendsNullForDenyEmulatorSimulatorOfOutsidePolicyOfCondGeo() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new ConditionalGeoFencePolicy(false, false, null,
                new MethodAmountPolicy(false, false, null, 0),
                new MethodAmountPolicy(false, false, null, 0)
        ));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getPolicy().getOutPolicy().getDenyEmulatorSimulator());
    }

    @Test
    public void sendsAmountForInsidePolicyOfCondGeoWhenAmount() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new ConditionalGeoFencePolicy(false, false, null,
                new MethodAmountPolicy(false, false, null, 22),
                new FactorsPolicy(false, false, null, true, true, true)

        ));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(22), requestCaptor.getValue().getPolicy().getInPolicy().getAmount());
    }
    @Test
    public void sendsAmountForOutsidePolicyOfCondGeoWhenAmount() throws Exception {
        client.setAdvancedServicePolicy(serviceId, new ConditionalGeoFencePolicy(false, false, null,
                new FactorsPolicy(false, false, null, true, true, true),
                new MethodAmountPolicy(false, false, null, 22)
        ));
        verify(transport).directoryV3ServicePolicyPut(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(Integer.valueOf(22), requestCaptor.getValue().getPolicy().getOutPolicy().getAmount());
    }
}