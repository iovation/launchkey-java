package com.iovation.launchkey.sdk.client;

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3TotpDeleteRequest;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3TotpPostResponse;
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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientRemoveUserTotpTest {
    private BasicDirectoryClient client;
    private final UUID directoryId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");

    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<DirectoryV3TotpDeleteRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Mock
    private DirectoryV3TotpPostResponse response;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
    }

    @Test
    public void putsUsernameInRequest() throws Exception {
        client.removeUserTotp("Expected Username");
        verify(transport).directoryV3TotpDelete(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Username", requestCaptor.getValue().getIdentifier());
    }

    @Test
    public void sendsDirectoryEntity() throws Exception {
        client.removeUserTotp(null);
        verify(transport).directoryV3TotpDelete(any(DirectoryV3TotpDeleteRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        client.removeUserTotp(null);
        verify(transport).directoryV3TotpDelete(any(DirectoryV3TotpDeleteRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }
}
