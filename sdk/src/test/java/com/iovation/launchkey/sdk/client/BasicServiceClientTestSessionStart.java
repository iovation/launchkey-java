package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.ServiceV3SessionsPostRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicServiceClientTestSessionStart {
    private final static UUID serviceId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");
    private final static UUID authRequestId = UUID.fromString("3e3dd6f8-a24e-11e8-b600-8c85909ffbde");
    private final static String user = "User Name";

    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServiceV3SessionsPostRequest> requestCaptor;

    private ServiceClient client;

    @Before
    public void setUp() throws Exception {
        client = new BasicServiceClient(serviceId, transport);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.sessionStart(user, authRequestId.toString());
        verify(transport).serviceV3SessionsPost(any(ServiceV3SessionsPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.SERVICE, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.sessionStart(user, authRequestId.toString());
        verify(transport).serviceV3SessionsPost(any(ServiceV3SessionsPostRequest.class), entityCaptor.capture());
        assertEquals(serviceId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsUserAsEndUserIdentifierWhenNoAuthRequestIdProvided() throws Exception {
        client.sessionStart(user);
        verify(transport).serviceV3SessionsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getEndUserIdentifier());
    }

    @Test
    public void sendsUserAsEndUserIdentifierWhenAuthRequestIdProvided() throws Exception {
        client.sessionStart(user);
        verify(transport).serviceV3SessionsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(user, requestCaptor.getValue().getEndUserIdentifier());
    }

    @Test
    public void sendsNullForAuthRequestWhenNoAuthRequestIdProvided() throws Exception {
        client.sessionStart(user);
        verify(transport).serviceV3SessionsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().getAuthorizationRequestId());
    }

    @Test
    public void sendsUserIdentifierWhenAuthRequestIdProvided() throws Exception {
        client.sessionStart(user, authRequestId.toString());
        verify(transport).serviceV3SessionsPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(authRequestId, requestCaptor.getValue().getAuthorizationRequestId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsIllegalArgumentWhenAuthRequestIdIsNotUUID() throws Exception {
        client.sessionStart(user, "Not a UUID");
    }
}