package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.error.CryptographyError;
import com.iovation.launchkey.sdk.error.InvalidResponseException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.NoSuchAlgorithmException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportTestResponseValidationTest extends ApacheHttpTransportTestBase {

    @Test
    public void verifySha256hashIsUsedForS256ResponseFuncValueInJwt() throws Exception {
        when(jwtClaims.getContentHashAlgorithm()).thenReturn("S256");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
        verify(crypto).sha256("Hello World!".getBytes());
    }

    @Test
    public void verifySha384hashIsUsedForS384ResponseFuncValueInJwt() throws Exception {
        when(jwtClaims.getContentHashAlgorithm()).thenReturn("S384");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
        verify(crypto).sha384("Hello World!".getBytes());
    }

    @Test
    public void verifySha512hashIsUsedForS512ResponseFuncValueInJwt() throws Exception {
        when(jwtClaims.getContentHashAlgorithm()).thenReturn("S512");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
        verify(crypto).sha512("Hello World!".getBytes());
    }

    @Test(expected = InvalidResponseException.class)
    public void whenInvalidContentHashAlgorithmIsUsedThenInvalidResponseExceptionIsThrown() throws Exception {
        when(jwtClaims.getContentHashAlgorithm()).thenReturn("S128");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = CryptographyError.class)
    public void whenCryptoHashDoesNotExistThenInvalidResponseExceptionIsThrown() throws Exception {
        when(crypto.sha256(any(byte[].class))).thenThrow(new NoSuchAlgorithmException());
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenContentHashAlgorithmDoesNotExistButThereIsContentThenInvalidResponseExceptionIsThrown() throws Exception {
        when(jwtClaims.getContentHashAlgorithm()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenContentHashAlgorithmDoesExistButThereIsNoContentThenInvalidResponseExceptionIsThrown() throws Exception {
        when(jwtClaims.getContentHash()).thenReturn(null);
        when(httpResponse.getEntity()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenContentHashDoesExistButThereIsNoContentThenInvalidResponseExceptionIsThrown() throws Exception {
        when(jwtClaims.getContentHashAlgorithm()).thenReturn(null);
        when(httpResponse.getEntity()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenContentHashDoesNotExistButThereIsContentThenInvalidResponseExceptionIsThrown() throws Exception {
        when(jwtClaims.getContentHash()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    public void whenContentHashDoesNotExistsAndContentHashAlgorithmDoesNotExistAndContentIsEmptyThenNothingIsVerified() throws Exception {
        when(jwtClaims.getContentHash()).thenReturn(null);
        when(jwtClaims.getContentHashAlgorithm()).thenReturn(null);
        when(httpResponse.getEntity()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
        verifyZeroInteractions(crypto);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenContentHashDoesNotMatchThenInvalidResponseExceptionIsThrown() throws Exception {
        when(crypto.sha256(any(byte[].class))).thenReturn("Not the same hash".getBytes());
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenStatusCodeDoesNoMatchJwtValueThenInvalidResponseExceptionIsThrown() throws Exception {
        when(jwtClaims.getStatusCode()).thenReturn(400);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenLocationHeaderDoesNoMatchJwtValueThenInvalidResponseExceptionIsThrown() throws Exception {
        when(httpResponse.containsHeader("Location")).thenReturn(true);
        when(httpResponse.getFirstHeader("Location")).thenReturn(new BasicHeader("Location", "header"));
        when(jwtClaims.getLocationHeader()).thenReturn("jwt");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenLocationHeaderMissingButJwtValueExistsThenInvalidResponseExceptionIsThrown() throws Exception {
        when(httpResponse.containsHeader("location")).thenReturn(false);
        when(jwtClaims.getLocationHeader()).thenReturn("jwt");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenLocationHeaderExistsButJwtValueMissingThenInvalidResponseExceptionIsThrown() throws Exception {
        when(httpResponse.containsHeader("Location")).thenReturn(true);
        when(httpResponse.getFirstHeader("Location")).thenReturn(new BasicHeader("Location", "header"));
        when(jwtClaims.getLocationHeader()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenCacheControlHeaderDoesNoMatchJwtValueThenInvalidResponseExceptionIsThrown() throws Exception {
        when(httpResponse.containsHeader("Cache-Control")).thenReturn(true);
        when(httpResponse.getFirstHeader("Cache-Control")).thenReturn(new BasicHeader("Cache-Control", "header"));
        when(jwtClaims.getCacheControlHeader()).thenReturn("jwt");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenCacheControlHeaderMissingButJwtValueExistsThenInvalidResponseExceptionIsThrown() throws Exception {
        when(httpResponse.containsHeader("Cache-Control")).thenReturn(false);
        when(jwtClaims.getCacheControlHeader()).thenReturn("jwt");
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }

    @Test(expected = InvalidResponseException.class)
    public void whenCacheControlHeaderExistsButJwtValueMissingThenInvalidResponseExceptionIsThrown() throws Exception {
        when(httpResponse.containsHeader("Cache-Control")).thenReturn(true);
        when(httpResponse.getFirstHeader("Cache-Control")).thenReturn(new BasicHeader("Cache-Control", "header"));
        when(jwtClaims.getCacheControlHeader()).thenReturn(null);
        transport.getHttpResponse("GET", "/", issuer, null, true, null);
    }
}