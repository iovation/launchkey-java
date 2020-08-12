package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.error.AuthorizationInProgress;
import com.iovation.launchkey.sdk.error.AuthorizationRequestTimedOutError;
import com.iovation.launchkey.sdk.transport.domain.Error;
import com.iovation.launchkey.sdk.transport.domain.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.StatusLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportServiceV3TotpPostTest extends ApacheHttpTransportTestBase {

    @Mock
    public static ServiceV3TotpPostResponse response;

    @Mock
    private ServiceV3TotpPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        when(jwtData.getAudience()).thenReturn("svc:767e72d9-e7aa-11e8-a951-fa001d282e01");
        when(jweService.getHeaders(anyString())).thenReturn(new HashMap<String, String>() {{
            put("aud", "svc:5e9aaee6-f1db-11e8-ac7a-fa001d282e01");
        }});
        when(objectMapper.readValue(anyString(), eq(ServiceV3TotpPostResponse.class))).thenReturn(response);
    }


    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.serviceV3TotpPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/service/v3/totp")));
    }

    @Test
    public void marshalsExpectedRequestData() throws Exception {
        transport.serviceV3TotpPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.serviceV3TotpPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.serviceV3TotpPost(request, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        ServiceV3TotpPostResponse expected = mock(ServiceV3TotpPostResponse.class);
        when(objectMapper.readValue(anyString(), eq(ServiceV3TotpPostResponse.class))).thenReturn(expected);
        ServiceV3TotpPostResponse actual = transport.serviceV3TotpPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(ServiceV3TotpPostResponse.class));
        assertEquals(expected, actual);
    }
}