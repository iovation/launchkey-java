package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportHandleServerSentEventDeviceLinkingResponseTest extends ApacheHttpTransportTestBase {
    private HashMap<String, List<String>> headers;
    @Mock private ServerSentEventDeviceLinkCompletion serverSentEventDeviceLinkCompletion;
    @Mock private RSAPrivateKey privateKey;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        when(jwtClaims.getAudience()).thenReturn("svc:5e9aaee6-f1db-11e8-ac7a-fa001d282e01");
        when(jwtClaims.getSubject()).thenReturn("svc:767e72d9-e7aa-11e8-a951-fa001d282e01");
        when(jweService.getHeaders(anyString())).thenReturn(new HashMap<String, String>() {{
            put("aud", "svc:5e9aaee6-f1db-11e8-ac7a-fa001d282e01");
            put("sub", "svc:767e72d9-e7aa-11e8-a951-fa001d282e01");
            put("kid", "Key ID");
        }});

        when(jwtData.getKeyId()).thenReturn("Key ID");
        when(jwtService.decode(any(PublicKey.class), anyString(), (String) isNull(), any(Date.class), anyString()))
                .thenReturn(jwtClaims);
        when(jweService.decrypt(anyString(), any(PrivateKey.class))).thenReturn("Decrypted");
        when(objectMapper.readValue(anyString(), eq(ServerSentEventType.class)))
                .thenReturn(serverSentEventType);
        when(serverSentEventType.getType()).thenReturn(ServerSentEventType.DEVICE_LINK_COMPLETION_WEBHOOK);
        when(objectMapper.readValue(anyString(), eq(ServerSentEventDeviceLinkCompletion.class)))
                .thenReturn(serverSentEventDeviceLinkCompletion);
        when(entityKeyMap.getKey(any(EntityIdentifier.class), anyString())).thenReturn(privateKey);
        headers = new HashMap<String, List<String>>(){{
            put("Content-Type", new ArrayList<String>(){{
                add("application/jose");
            }});
            put("X-IOV-JWT", new ArrayList<String>(){{
                add("JWT Header");
            }});
        }};
    }


    @Test
    public void parsesServerSentEventDeviceLinked() throws Exception {
        transport.handleServerSentEvent(headers, null, null, "body");
        verify(objectMapper).readValue(eq("Decrypted"), eq(ServerSentEventDeviceLinkCompletion.class));
    }

    @Test
    public void returnsParsedServerSentEventDeviceLinked() throws Exception {
        ServerSentEvent actual = transport.handleServerSentEvent(
                headers, null, null, "body");
        assertEquals(actual, serverSentEventDeviceLinkCompletion);
    }
}