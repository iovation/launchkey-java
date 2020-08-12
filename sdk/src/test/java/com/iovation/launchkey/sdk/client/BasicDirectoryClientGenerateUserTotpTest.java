package com.iovation.launchkey.sdk.client;
import com.iovation.launchkey.sdk.domain.DirectoryUserTotp;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.*;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientGenerateUserTotpTest {
    private BasicDirectoryClient client;
    private final UUID directoryId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");

    @Mock
    private Transport transport;

    @Captor
    private ArgumentCaptor<DirectoryV3TotpPostRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Mock
    private DirectoryV3TotpPostResponse response;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
        when(transport
                .directoryV3TotpPost(any(DirectoryV3TotpPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void putsUsernameInRequest() throws Exception {
        client.generateUserTotp("Expected Username");
        verify(transport).directoryV3TotpPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected Username", requestCaptor.getValue().getIdentifier());
    }

    @Test
    public void sendsDirectoryEntity() throws Exception {
        client.generateUserTotp(null);
        verify(transport).directoryV3TotpPost(any(DirectoryV3TotpPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        client.generateUserTotp(null);
        verify(transport).directoryV3TotpPost(any(DirectoryV3TotpPostRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void responseReturnsSecret() throws Exception {
        when(response.getSecret()).thenReturn("Expected Secret");
        DirectoryUserTotp response = client.generateUserTotp(null);
        assertEquals("Expected Secret", response.getSecret());
    }

    @Test
    public void responseReturnsAlgorithm() throws Exception {
        when(response.getAlgorithm()).thenReturn("Expected Algorithm");
        DirectoryUserTotp response = client.generateUserTotp(null);
        assertEquals("Expected Algorithm", response.getAlgorithm());
    }

    @Test
    public void responseReturnsPeriod() throws Exception {
        when(response.getPeriod()).thenReturn(1234);
        DirectoryUserTotp response = client.generateUserTotp(null);
        assertEquals(1234, response.getPeriod());
    }
    @Test
    public void responseReturnsDigits() throws Exception {
        when(response.getDigits()).thenReturn(4321);
        DirectoryUserTotp response = client.generateUserTotp(null);
        assertEquals(4321, response.getDigits());
    }
}
