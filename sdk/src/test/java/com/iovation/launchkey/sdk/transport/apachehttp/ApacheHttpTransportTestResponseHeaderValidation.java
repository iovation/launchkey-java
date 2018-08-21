package com.iovation.launchkey.sdk.transport.apachehttp;

import com.iovation.launchkey.sdk.error.InvalidResponseException;
import org.apache.http.message.BasicHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportTestResponseHeaderValidation extends ApacheHttpTransportTestBase {

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