package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BasicServiceClientCancelAuthorizationRequestTest {
    private final static UUID serviceId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private final static String authRequestId = "a5a07697-92ab-4c45-81d6-1ea41c019c09";
    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    private ServiceClient client;
    @Before
    public void setUp() throws Exception {
        client = new BasicServiceClient(serviceId, transport);
    }


    @Test
    public void sendsAuthorizationRequestId() throws Exception {
        client.cancelAuthorizationRequest(authRequestId);
        UUID expected = UUID.fromString("a5a07697-92ab-4c45-81d6-1ea41c019c09");
        verify(transport).serviceV3AuthsDelete(eq(expected), any(EntityIdentifier.class));
    }


    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.cancelAuthorizationRequest(authRequestId);
        verify(transport).serviceV3AuthsDelete(any(UUID.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.cancelAuthorizationRequest(authRequestId);
        verify(transport).serviceV3AuthsDelete(any(UUID.class), entityCaptor.capture());
        assertEquals(serviceId, entityCaptor.getValue().getId());
    }
}