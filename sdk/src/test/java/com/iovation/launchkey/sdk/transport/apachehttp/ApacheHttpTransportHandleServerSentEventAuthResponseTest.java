package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.transport.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportHandleServerSentEventAuthResponseTest extends ApacheHttpTransportTestBase {
    private HashMap<String, List<String>> headers;
    @Mock private ServiceV3AuthsGetResponseDevice deviceResponse;
    @Mock private ServerSentEventAuthorizationResponseCore coreResponse;
    @Mock private ServiceV3AuthsGetResponseDeviceJWE deviceJweResponse;
    @Mock private RSAPrivateKey privateKey;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        when(serverSentEventType.getType()).thenReturn(ServerSentEventType.AUTHORIZATION_RESPONSE_WEBHOOK);
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
        when(objectMapper.readValue(anyString(), eq(ServerSentEventAuthorizationResponseCore.class)))
                .thenReturn(coreResponse);
        when(coreResponse.getAuth()).thenReturn("Auth");
        when(objectMapper.readValue(any(byte[].class), eq(ServiceV3AuthsGetResponseDevice.class))).thenReturn(deviceResponse);
        when(objectMapper.readValue(anyString(), eq(ServiceV3AuthsGetResponseDeviceJWE.class)))
                .thenReturn(deviceJweResponse);
        when(entityKeyMap.getKey(any(EntityIdentifier.class), anyString())).thenReturn(privateKey);
        when(crypto.decryptRSA(any(byte[].class), any(PrivateKey.class)))
                .thenReturn("Decrypted Device Response".getBytes());
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
    public void returnsAudienceFromJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");

        EntityIdentifier expected = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE,
                UUID.fromString("5e9aaee6-f1db-11e8-ac7a-fa001d282e01"));
        assertEquals(expected, response.getRequestingEntity());
    }

    @Test
    public void returnsAudienceFromJwtWhenJweResponseIsNotPresent() throws Exception {
        String id = "svc:dda88ff4-f295-11e8-8eb2-f2801f1b9fd1";
        when(jwtClaims.getAudience()).thenReturn(id);
        EntityIdentifier expected = EntityIdentifier.fromString(id);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals(expected, response.getRequestingEntity());
    }

    @Test
    public void returnsRequestSubjectAsServiceId() throws Exception {
        UUID expected = UUID.fromString("bbd6486c-f295-11e8-8eb2-f2801f1b9fd1");
        when(jwtClaims.getSubject()).thenReturn("svc:" + expected.toString());
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals(expected, response.getServiceId());
    }

    @Test
    public void returnsServiceUserHashFromAPIResponse() throws Exception {
        when(coreResponse.getServiceUserHash()).thenReturn("Expected Service User Hash");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("Expected Service User Hash", response.getServiceUserHash());
    }

    @Test
    public void returnsOrgUserHashFromAPIResponse() throws Exception {
        when(coreResponse.getOrgUserHash()).thenReturn("Expected Org User Hash");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("Expected Org User Hash", response.getOrganizationUserHash());
    }

    @Test
    public void returnsUserPushIdFromAPIResponse() throws Exception {
        when(coreResponse.getUserPushId()).thenReturn("Expected User Push ID");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("Expected User Push ID", response.getUserPushId());
    }

    @Test
    public void returnsAuthRequestIdFromJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        UUID expected = UUID.randomUUID();
        when(deviceJweResponse.getAuthorizationRequestId()).thenReturn(expected);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals(expected, response.getAuthorizationRequestId());
    }

    @Test
    public void returnsAuthRequestIdFromDeviceResponseWhenJweResponseIsNotPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        UUID expected = UUID.randomUUID();
        when(deviceResponse.getAuthorizationRequestId()).thenReturn(expected);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals(expected, response.getAuthorizationRequestId());
    }

    @Test
    public void returnsTrueForResponseWhenJweResponseIsPresentAndAndResponseTypeIsAuthorized() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getType()).thenReturn("AUTHORIZED");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertTrue(response.getResponse());
    }

    @Test
    public void returnsFalseForAuthorizdWhenJweResponseIsPresentAndAndResponseTypeIsNotAuthorized() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getType()).thenReturn("NOT AUTHORIZED");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertFalse(response.getResponse());
    }

    @Test
    public void returnsTrueForResponseWhenJweResponseIsNotPresentAndDeviceResponseIsTrue() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        when(deviceResponse.getResponse()).thenReturn(true);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertTrue(response.getResponse());
    }

    @Test
    public void returnsFalseForResponseWhenJweResponseIsNotPresentAndDeviceResponseIsFalse() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        when(deviceResponse.getResponse()).thenReturn(false);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertFalse(response.getResponse());
    }

    @Test
    public void returnsDeviceIdFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getDeviceId()).thenReturn("Expected Device ID");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("Expected Device ID", response.getDeviceId());
    }

    @Test
    public void returnsDeviceIdFromDeviceResponseWhenJweResponseIsNotPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        when(deviceResponse.getDeviceId()).thenReturn("Expected Device ID");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("Expected Device ID", response.getDeviceId());
    }

    @Test
    public void returnsServicePinsFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getServicePins()).thenReturn(new String[]{"PIN1", "PIN2", "PIN3"});
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertArrayEquals(new String[]{"PIN1", "PIN2", "PIN3"}, response.getServicePins());
    }

    @Test
    public void returnsServicePinsFromDeviceResponseWhenJweResponseIsNotPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        when(deviceResponse.getServicePins()).thenReturn(new String[]{"PIN1", "PIN2", "PIN3"});
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertArrayEquals(new String[]{"PIN1", "PIN2", "PIN3"}, response.getServicePins());
    }

    @Test
    public void returnsTypeFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getType()).thenReturn("EXPECTED TYPE");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("EXPECTED TYPE", response.getType());
    }

    @Test
    public void returnsNullForTypeWhenJweResponseIsNotPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertNull(response.getType());
    }

    @Test
    public void returnsReasonFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getReason()).thenReturn("EXPECTED REASON");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("EXPECTED REASON", response.getReason());
    }

    @Test
    public void returnsNullForReasonWhenJweResponseIsNotPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertNull(response.getReason());
    }

    @Test
    public void returnsDenialReasonFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getDenialReason()).thenReturn("EXPECTED DENIAL REASON");
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertEquals("EXPECTED DENIAL REASON", response.getDenialReason());
    }

    @Test
    public void returnsNullForDenialReasonWhenJweResponseIsNotPresent() throws Exception {
        when(coreResponse.getAuthJwe()).thenReturn(null);
        ServerSentEventAuthorizationResponse response =
                (ServerSentEventAuthorizationResponse) transport.handleServerSentEvent(
                        headers, null, null, "body");
        assertNull(response.getDenialReason());
    }
}