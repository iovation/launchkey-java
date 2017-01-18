package com.launchkey.sdk.transport; /**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.transport.domain.PublicPingGetResponse;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicStatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApacheHttpTransportPublicPingGetTest extends ApacheHttpTransportTest {

    @Test
    public void publicPingGetCallsHttpClientWithProperHttpRequestMethod() throws Exception {
        transport.publicPingGet();
        ArgumentCaptor<HttpUriRequest> actual = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(actual.capture());
        assertEquals("GET", actual.getValue().getMethod());
    }

    @Test
    public void publicPingGetCallsHttpClientWithProperHttpRequestUri() throws Exception {
        URI expected = URI.create(baseUrl.concat("/public/v3/ping"));
        transport.publicPingGet();
        ArgumentCaptor<HttpUriRequest> actual = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(actual.capture());
        assertEquals(expected, actual.getValue().getURI());
    }

    @Test
    public void publicPingGetParsesResponseWithResponseBody() throws Exception {
        InputStream expected = httpResponse.getEntity().getContent();
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(expected);
        when(httpResponse.getEntity()).thenReturn(entity);
        transport.publicPingGet();
        verify(objectMapper).readValue(eq(expected), any(Class.class));
    }

    @Test
    public void publicPingGetParsesResponseWithProperClass() throws Exception {
        transport.publicPingGet();
        verify(objectMapper).readValue(any(InputStream.class), eq(PublicPingGetResponse.class));
    }

    @Test
    public void publicPingGetReturnsParsedJson() throws Exception {
        PublicPingGetResponse expected = new PublicPingGetResponse(new Date());
        when(objectMapper.readValue(any(InputStream.class), any(Class.class))).thenReturn(expected);
        PublicPingGetResponse actual = transport.publicPingGet();
        assertEquals(expected, actual);
    }

    @Test
    public void publicPingThrowsCommunicationErrorExceptionWhenHttpClientThrowsIOError() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(new IOException());
        thrown.expect(CommunicationErrorException.class);
        transport.publicPingGet();
    }

    @Test
    public void publicPingThrowsInvalidResponseExceptionWhenObjectParserThrowsJsonMappingException() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(Class.class))).thenThrow(mock(JsonMappingException.class));
        thrown.expect(InvalidResponseException.class);
        transport.publicPingGet();
    }

    @Test
    public void publicPingThrowsInvalidResponseExceptionWhenObjectParserThrowsJsonParseException() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(Class.class)))
                .thenThrow(mock(JsonParseException.class));
        thrown.expect(InvalidResponseException.class);
        transport.publicPingGet();
    }
}