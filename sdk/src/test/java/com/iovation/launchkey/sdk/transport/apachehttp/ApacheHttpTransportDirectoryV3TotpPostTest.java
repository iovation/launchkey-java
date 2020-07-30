package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportDirectoryV3TotpPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private DirectoryV3TotpPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.directoryV3TotpPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/directory/v3/totp")));
    }

    @Test
    public void marshalsExpectedRequestData() throws Exception {
        transport.directoryV3TotpPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.directoryV3TotpPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.directoryV3TotpPost(request, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        DirectoryV3TotpPostResponse expected = mock(DirectoryV3TotpPostResponse.class);
        when(objectMapper.readValue(anyString(), eq(DirectoryV3TotpPostResponse.class))).thenReturn(expected);
        DirectoryV3TotpPostResponse actual = transport.directoryV3TotpPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(DirectoryV3TotpPostResponse.class));
        assertEquals(expected, actual);
    }
}