package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServiceV3SessionsDeleteRequest;
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
public class BasicServiceClientSessionEndTest {
    private final static UUID serviceId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private final static String user = "User Name";
    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServiceV3SessionsDeleteRequest> requestCaptor;

    private ServiceClient client;
    @Before
    public void setUp() throws Exception {
        client = new BasicServiceClient(serviceId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.sessionEnd(user);
        verify(transport).serviceV3SessionsDelete(any(ServiceV3SessionsDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.sessionEnd(user);
        verify(transport).serviceV3SessionsDelete(any(ServiceV3SessionsDeleteRequest.class), entityCaptor.capture());
        assertEquals(serviceId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsUserAsEndUserIdentifier() throws Exception {
        client.sessionEnd(user);
        verify(transport).serviceV3SessionsDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getEndUserIdentifier());
    }
}