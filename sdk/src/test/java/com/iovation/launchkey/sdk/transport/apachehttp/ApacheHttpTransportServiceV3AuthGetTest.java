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

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportServiceV3AuthGetTest extends ApacheHttpTransportTestBase {

    @Mock
    public static ServiceV3AuthsGetResponseCore response;

    @Mock
    public static ServiceV3AuthsGetResponseDevice deviceResponse;

    @Mock
    public static ServiceV3AuthsGetResponseDeviceJWE deviceJweResponse;

    @Mock
    public static RSAPrivateKey privateKey;

    @Mock
    public static Error errorResponse;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        when(jwtData.getAudience()).thenReturn("svc:767e72d9-e7aa-11e8-a951-fa001d282e01");
        when(jweService.getHeaders(anyString())).thenReturn(new HashMap<String, String>() {{
            put("aud", "svc:5e9aaee6-f1db-11e8-ac7a-fa001d282e01");
        }});
        when(objectMapper.readValue(anyString(), eq(ServiceV3AuthsGetResponseCore.class))).thenReturn(response);
        when(objectMapper.readValue(any(byte[].class), eq(ServiceV3AuthsGetResponseDevice.class)))
                .thenReturn(deviceResponse);
        when(objectMapper.readValue(anyString(), eq(ServiceV3AuthsGetResponseDeviceJWE.class)))
                .thenReturn(deviceJweResponse);
        when(objectMapper.readValue(anyString(), eq(Error.class)))
                .thenReturn(errorResponse);
        when(crypto.decryptRSA(any(byte[].class), any(PrivateKey.class))).thenReturn("RSA decrypted".getBytes());
        when(response.getEncryptedDeviceResponse()).thenReturn(
                new String(Base64.encodeBase64("encrypted device response".getBytes())));
        when(response.getPublicKeyId()).thenReturn("public key ID");
        when(entityKeyMap.getKey(any(EntityIdentifier.class), anyString())).thenReturn(privateKey);
    }

    @Test
    public void returnsNullWhenResponseStatusIs204() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);
        when(jwtClaims.getStatusCode()).thenReturn(204);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(204);

        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertNull(response);
    }

    @Test(expected = AuthorizationRequestTimedOutError.class)
    public void throwsAuthorizationRequestTimedOutErrorWhenResponseStatusIs408() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);
        when(jwtClaims.getStatusCode()).thenReturn(408);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(408);

        transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );
    }

    @Test
    public void throwsAuthorizationInProgressErrorWhenResponseStatusIs409() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);
        when(jwtClaims.getStatusCode()).thenReturn(409);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(409);
        when(errorResponse.getErrorCode()).thenReturn("SVC-005");
        Map<String, Object> errorData = new HashMap<String, Object>() {{
            put("auth_request", "Auth Request ID");
            put("expires", "1970-01-01T00:00:00Z");
            put("from_same_service", true);
        }};
        when(errorResponse.getErrorData()).thenReturn(errorData);

        try {
            transport.serviceV3AuthsGet(
                    UUID.randomUUID(),
                    new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
            );
            fail("Expected " + AuthorizationInProgress.class + " but was not thrown");
        } catch (AuthorizationInProgress e) {
            assertEquals("Unexpected Auth Request ID value!", "Auth Request ID", e.getAuthorizationRequestId());
            assertTrue("Unexpected from same service value!", e.isFromSameService());
            Date expectedDate = new Date(){{setTime(0L);}};
            assertEquals("Unexpected expires value!", expectedDate, e.getExpires());
        }
    }

    @Test
    public void returnsAudienceFromJwtResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        EntityIdentifier expected = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE,
                UUID.fromString("767e72d9-e7aa-11e8-a951-fa001d282e01"));
        assertEquals(expected, response.getRequestingEntity());
    }

    @Test
    public void returnsAudienceFromJwtWhenJweResponseIsNotPresent() throws Exception {
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        EntityIdentifier expected = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE,
                UUID.fromString("767e72d9-e7aa-11e8-a951-fa001d282e01"));
        assertEquals(expected, response.getRequestingEntity());
    }

    @Test
    public void returnsSubjectPassedToMethod() throws Exception {
        UUID expected = UUID.randomUUID();
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, expected)
        );

        assertEquals(expected, response.getServiceId());
    }

    @Test
    public void returnsServiceUserHashFromAPIResponse() throws Exception {
        when(response.getServiceUserHash()).thenReturn("Expected Service User Hash");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("Expected Service User Hash", response.getServiceUserHash());
    }

    @Test
    public void returnsOrgUserHashFromAPIResponse() throws Exception {
        when(response.getOrgUserHash()).thenReturn("Expected Org User Hash");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("Expected Org User Hash", response.getOrganizationUserHash());
    }

    @Test
    public void returnsUserPushIdFromAPIResponse() throws Exception {
        when(response.getUserPushId()).thenReturn("Expected User Push ID");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("Expected User Push ID", response.getUserPushId());
    }

    @Test
    public void returnsAuthRequestIdFromJweResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        UUID expected = UUID.randomUUID();
        when(deviceJweResponse.getAuthorizationRequestId()).thenReturn(expected);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals(expected, response.getAuthorizationRequestId());
    }

    @Test
    public void returnsAuthRequestIdFromDeviceResponseWhenJweResponseIsNotPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        UUID expected = UUID.randomUUID();
        when(deviceResponse.getAuthorizationRequestId()).thenReturn(expected);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals(expected, response.getAuthorizationRequestId());
    }

    @Test
    public void returnsTrueForResponseWhenJweResponseIsPresentAndAndResponseTypeIsAuthorized() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getType()).thenReturn("AUTHORIZED");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertTrue(response.getResponse());
    }

    @Test
    public void returnsFalseForAuthorizdWhenJweResponseIsPresentAndAndResponseTypeIsNotAuthorized() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getType()).thenReturn("NOT AUTHORIZED");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertFalse(response.getResponse());
    }

    @Test
    public void returnsTrueForResponseWhenJweResponseIsNotPresentAndDeviceResponseIsTrue() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        when(deviceResponse.getResponse()).thenReturn(true);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertTrue(response.getResponse());
    }

    @Test
    public void returnsFalseForResponseWhenJweResponseIsNotPresentAndDeviceResponseIsFalse() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        when(deviceResponse.getResponse()).thenReturn(false);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertFalse(response.getResponse());
    }

    @Test
    public void returnsDeviceIdFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getDeviceId()).thenReturn("Expected Device ID");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("Expected Device ID", response.getDeviceId());
    }

    @Test
    public void returnsDeviceIdFromDeviceResponseWhenJweResponseIsNotPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        when(deviceResponse.getDeviceId()).thenReturn("Expected Device ID");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("Expected Device ID", response.getDeviceId());
    }

    @Test
    public void returnsServicePinsFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getServicePins()).thenReturn(new String[]{"PIN1", "PIN2", "PIN3"});
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertArrayEquals(new String[]{"PIN1", "PIN2", "PIN3"}, response.getServicePins());
    }

    @Test
    public void returnsServicePinsFromDeviceResponseWhenJweResponseIsNotPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        when(deviceResponse.getServicePins()).thenReturn(new String[]{"PIN1", "PIN2", "PIN3"});
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertArrayEquals(new String[]{"PIN1", "PIN2", "PIN3"}, response.getServicePins());
    }

    @Test
    public void returnsTypeFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getType()).thenReturn("EXPECTED TYPE");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("EXPECTED TYPE", response.getType());
    }

    @Test
    public void returnsNullForTypeWhenJweResponseIsNotPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertNull(response.getType());
    }

    @Test
    public void returnsReasonFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getReason()).thenReturn("EXPECTED REASON");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("EXPECTED REASON", response.getReason());
    }

    @Test
    public void returnsNullForReasonWhenJweResponseIsNotPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertNull(response.getReason());
    }

    @Test
    public void returnsDenialReasonFromDeviceJweResponseWhenJweResponseIsPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn("JWE Encrypted Value");
        when(deviceJweResponse.getDenialReason()).thenReturn("EXPECTED DENIAL REASON");
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertEquals("EXPECTED DENIAL REASON", response.getDenialReason());
    }

    @Test
    public void returnsNullForDenialReasonWhenJweResponseIsNotPresent() throws Exception {
        when(response.getJweEncryptedDeviceResponse()).thenReturn(null);
        ServiceV3AuthsGetResponse response = transport.serviceV3AuthsGet(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );

        assertNull(response.getDenialReason());
    }
}