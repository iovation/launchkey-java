package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.error.AuthorizationRequestCanceled;
import com.iovation.launchkey.sdk.error.AuthorizationResponseExists;
import com.iovation.launchkey.sdk.error.EntityNotFound;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.Error;
import org.apache.http.StatusLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportServiceV3AuthDeleteTest extends ApacheHttpTransportTestBase {

    @Mock
    private EntityIdentifier subject;

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        UUID authRequestId = UUID.fromString("a5a07697-92ab-4c45-81d6-1ea41c019c09");
        transport.serviceV3AuthsDelete(authRequestId, subject);
        verifyCall("DELETE", URI.create(baseUrl.concat("/service/v3/auths/a5a07697-92ab-4c45-81d6-1ea41c019c09")));
    }

    @Test(expected = EntityNotFound.class)
    public void throwsEntityNotFoundWhenResponseStatusIs404() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);
        when(jwtClaims.getStatusCode()).thenReturn(404);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(404);

        transport.serviceV3AuthsDelete(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );
    }

    @Test(expected = AuthorizationRequestCanceled.class)
    public void throwsAuthorizationRequestCanceledWhenResponseStatyusIs400AndErrorCodeIsSvc007() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);
        when(jwtClaims.getStatusCode()).thenReturn(400);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(400);
        Error error = new Error("SVC-007", null, null);
        when(objectMapper.readValue(anyString(), eq(Error.class))).thenReturn(error);

        transport.serviceV3AuthsDelete(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );
    }

    @Test(expected = AuthorizationResponseExists.class)
    public void throwsAuthorizationResponseExistsWhenResponseStatyusIs400AndErrorCodeIsSvc006() throws Exception {
        StatusLine statusLine = mock(StatusLine.class);
        when(jwtClaims.getStatusCode()).thenReturn(400);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(400);
        Error error = new Error("SVC-006", null, null);
        when(objectMapper.readValue(anyString(), eq(Error.class))).thenReturn(error);

        transport.serviceV3AuthsDelete(
                UUID.randomUUID(),
                new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.randomUUID())
        );
    }
}