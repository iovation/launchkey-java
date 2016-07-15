/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v3;

import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.crypto.jwe.JWEFailure;
import com.launchkey.sdk.crypto.jwe.JWEService;
import com.launchkey.sdk.crypto.jwt.JWTClaims;
import com.launchkey.sdk.crypto.jwt.JWTError;
import com.launchkey.sdk.crypto.jwt.JWTService;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.service.ping.PingService;
import com.launchkey.sdk.service.token.TokenIdService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.bouncycastle.util.encoders.Hex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class ApacheHttpTransportBase {
    protected static final String BASE_URL = "https://expected.url";
    protected static final String PLATFORM_ID = "platform:id";
    protected static final String ISSUER_ID = "issuer:id";
    protected static final String BODY_HASH_ALG = "Body Hash Alg";
    protected static final byte[] BODY_HASH = "Body Hash".getBytes();
    protected static final String TOKEN_ID = "Token ID";
    protected static final Date PLATFORM_DATE = new Date(100000L); // 100 secs past the EPOC
    protected static final String ENCRYPTED_DATA = "Encrypted Data";
    protected static final String DECRYPTED_DATA = "Decrypted Data";
    protected static final String JWT = "Expected JWT";
    protected HttpClient httpClient;
    protected Transport transport;
    protected JWTService jwtService;
    protected JWEService jweService;
    protected Crypto crypto;
    protected PingService pingService;
    protected HttpResponse httpResponse;
    protected TokenIdService tokenIdService;
    protected HttpEntity httpResponseEntity;
    protected StatusLine statusLine;

    @Before
    public void setUp() throws Exception {
        httpClient = mock(HttpClient.class);

        httpResponse = mock(HttpResponse.class);
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);

        when(httpResponse.getFirstHeader(anyString())).thenReturn(new BasicHeader("Authorization", "Bearer expected token"));
        httpResponseEntity = mock(HttpEntity.class);
        when(httpResponse.getEntity()).thenReturn(httpResponseEntity);
        when(httpResponseEntity.getContent()).thenReturn(new ByteArrayInputStream("".getBytes()));

        statusLine = mock(StatusLine.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(statusLine.getReasonPhrase()).thenReturn("OK");
        when(httpResponse.getFirstHeader("Authorization"))
                .thenReturn(new BasicHeader("Authorization", "Bearer Expected Auth Header"));


        jwtService = mock(JWTService.class);
        when(jwtService.decode(anyString())).thenReturn(new JWTClaims(
                TOKEN_ID, PLATFORM_ID, ISSUER_ID, 0, 0, 0, BODY_HASH_ALG, Hex.toHexString(BODY_HASH), null, null
        ));
        when(jwtService.encode(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(JWT);

        jweService = mock(JWEService.class);
        when(jweService.encrypt(anyString())).thenReturn(ENCRYPTED_DATA);
        when(jweService.decrypt(anyString())).thenReturn(DECRYPTED_DATA);

        crypto = mock(Crypto.class);
        when(crypto.sha256(any(byte[].class))).thenReturn(BODY_HASH);

        pingService = mock(PingService.class);
        when(pingService.getPlatformTime()).thenReturn(PLATFORM_DATE);

        tokenIdService = mock(TokenIdService.class);
        when(tokenIdService.getTokenId()).thenReturn(TOKEN_ID);

        transport = new ApacheHttpTransport(
                httpClient,
                BASE_URL,
                PLATFORM_ID,
                jwtService,
                jweService,
                crypto,
                pingService,
                tokenIdService
        );

    }

    @After
    public void tearDown() throws Exception {
        httpClient = null;
        httpResponse = null;
        httpResponseEntity = null;
        jwtService = null;
        jweService = null;
        crypto = null;
        pingService = null;
        transport = null;
        tokenIdService = null;
        statusLine = null;
    }

    @Test
    public void addAuthorizationHeaderToRequest() throws Exception {
        makeTransportCall();
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture());
        assertNotNull(
                "No \"Authorization\" header in request headers!",
                captor.getValue().getFirstHeader("Authorization")
        );
        String header = captor.getValue().getFirstHeader("Authorization").getValue();
        assertThat("Authorization header does not begin with \"Bearer\"", header, startsWith("Bearer "));
        assertEquals("Unexpected Authorization header value", header, "Bearer ".concat(JWT));
    }

    @Test(expected = CommunicationErrorException.class)
    public void throwsCommunicationErrorExceptionWhenHttpClientThrowsClientProtocolException() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(new ClientProtocolException());
        makeTransportCall();
    }

    @Test(expected = CommunicationErrorException.class)
    public void throwsCommunicationErrorExceptionWhenHttpClientThrowsIOException() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(new IOException());
        makeTransportCall();
    }

    @Test(expected = InvalidStateException.class)
    public void throwsInvalidStateExceptionWhenJwtServiceThrowsJwtErrorEncodingToken() throws Exception {
        when(jwtService.encode(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new JWTError("Error", new Throwable()));
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void throwsInvalidCredentialsExceptionWhenNoAuthorizationHeaderPresent() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void throwsInvalidCredentialsExceptionWhenInvalidAuthorizationHeaderPresent() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(new BasicHeader("Name", "a:b"));
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void throwsInvalidCredentialsExceptionWhenJwtServiceThrowsJwtErrorDecodingToken() throws Exception {
        when(jwtService.decode(anyString())).thenThrow(new JWTError("Error", new Throwable()));
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void throwsInvalidCredentialsExceptionWhenJwtClaimsHaveNoJti() throws Exception {
        when(jwtService.decode(anyString())).thenReturn(new JWTClaims(
                null, PLATFORM_ID, ISSUER_ID, 0, 0, 0, BODY_HASH_ALG, Hex.toHexString(BODY_HASH), null, null
        ));
        makeTransportCall();
    }

    @Test(expected = InvalidResponseException.class)
    public void throwsInvalidCredentialsExceptionWhenJwtClaimsJtiDoesNotMatchRequestJti() throws Exception {
        when(jwtService.decode(anyString())).thenReturn(new JWTClaims(
                "Something Else", PLATFORM_ID, ISSUER_ID, 0, 0, 0, BODY_HASH_ALG, Hex.toHexString(BODY_HASH), null, null
        ));
        makeTransportCall();
    }

    @Test(expected = InvalidRequestException.class)
    public void raisesInvalidRequestExceptionForHttpStatusCode400() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(400);
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        makeTransportCall();
    }

    @Test(expected = InvalidCredentialsException.class)
    public void raisesInvalidCredentialsExceptionForHttpStatusCode401() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(401);
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        makeTransportCall();
    }

    @Test(expected = InvalidCredentialsException.class)
    public void raisesInvalidCredentialsExceptionForHttpStatusCode403() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(403);
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        makeTransportCall();
    }

    @Test(expected = UnknownEntityException.class)
    public void raisesUnknownEntityExceptionForHttpStatusCode404() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(404);
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        makeTransportCall();
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorExceptionForNon200StatusCodes() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(500);
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        makeTransportCall();
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorWithStatusCodeForMessageCodeWhenStatusCodeIsNon200AndContentInBodyIsUnParseable() throws Exception {
        when(statusLine.getStatusCode()).thenReturn(500);
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        setResponseContent("{Invalid JSON}");
        try {
            makeTransportCall();
        } catch (PlatformErrorException e) {
            assertEquals("HTTP-500", e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorWithStatusCodeForMessageCodeWhenStatusCodeIsNon200ContentInBodyIsNotMappable() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        when(statusLine.getStatusCode()).thenReturn(502);
        setResponseContent("{\"Un-mappable\":\"Data\"}");
        try {
            makeTransportCall();
        } catch (PlatformErrorException e) {
            assertEquals("HTTP-502", e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorWithStatusMessageForMessageWhenStatusCodeIsNon200AndContentInBodyIsUnParseable() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(statusLine.getReasonPhrase()).thenReturn("Expected Reason");
        setResponseContent("{Invalid JSON}");
        try {
            makeTransportCall();
        } catch (PlatformErrorException e) {
            assertEquals("Expected Reason", e.getMessage());
            throw e;
        }
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorWithStatusMessageForMessageWhenStatusCodeIsNon200ContentInBodyIsNotMappable() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        when(statusLine.getStatusCode()).thenReturn(502);
        when(statusLine.getReasonPhrase()).thenReturn("Expected Reason");
        setResponseContent("{\"Un-mappable\":\"Data\"}");
        try {
            makeTransportCall();
        } catch (PlatformErrorException e) {
            assertEquals("Expected Reason", e.getMessage());
            throw e;
        }
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorMessageIsMessageFromErrorInBody() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        when(statusLine.getStatusCode()).thenReturn(502);
        String json = "{" +
                "\"successful\": false," +
                " \"status_code\": 400," +
                " \"message\": \"Credentials incorrect for app and app secret\"," +
                " \"message_code\": 40422, \"response\": \"\"" +
                "}";
        setResponseContent(json);
        try {
            makeTransportCall();
        } catch (PlatformErrorException e) {
            assertEquals("Credentials incorrect for app and app secret", e.getMessage());
            throw e;
        }
    }

    @Test(expected = PlatformErrorException.class)
    public void raisesPlatformErrorCodeIsMessageCodeFromErrorInBody() throws Exception {
        when(httpResponse.getFirstHeader(anyString())).thenReturn(null);
        when(statusLine.getStatusCode()).thenReturn(502);
        String json = "{" +
                "\"successful\": false," +
                " \"status_code\": 400," +
                " \"message\": \"Credentials incorrect for app and app secret\"," +
                " \"message_code\": 40422, \"response\": \"\"" +
                "}";
        setResponseContent(json);
        try {
            makeTransportCall();
        } catch (PlatformErrorException e) {
            assertEquals("40422", e.getErrorCode());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidURIThrowsIllegalArgumentException() throws Exception {
        transport = new ApacheHttpTransport(
                httpClient,
                "Invalid Base URL",
                PLATFORM_ID,
                jwtService,
                jweService,
                crypto,
                pingService,
                tokenIdService
        );
    }

    @Test(expected = InvalidResponseException.class)
    public void jwtServiceDecodeJwtErrorThrowsInvalidStateException() throws Exception {
        when(jwtService.decode(anyString()))
                .thenThrow(new JWTError(null, null));
        makeTransportCall();
    }

    @Test(expected = InvalidStateException.class)
    public void jweServiceEncryptJweFailureThrowsInvalidStateException() throws Exception {
        when(jweService.encrypt(anyString()))
                .thenThrow(new JWEFailure(null, null));
        makeTransportCall();
    }

    protected abstract void makeTransportCall() throws BaseException;

    protected void setResponseContent(String value) throws IOException {
        when(httpResponseEntity.getContent()).thenReturn(new ByteArrayInputStream(value.getBytes()));
        when(httpResponseEntity.getContentLength()).thenReturn((long) value.length());
    }
}
