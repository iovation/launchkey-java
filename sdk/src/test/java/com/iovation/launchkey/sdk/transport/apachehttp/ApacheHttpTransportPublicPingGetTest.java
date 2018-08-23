package com.iovation.launchkey.sdk.transport.apachehttp; /**
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
import com.iovation.launchkey.sdk.error.CommunicationErrorException;
import com.iovation.launchkey.sdk.error.InvalidResponseException;
import com.iovation.launchkey.sdk.transport.domain.PublicV3PingGetResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportPublicPingGetTest extends ApacheHttpTransportTestBase {

    @Test
    public void publicPingGetCallsHttpClientWithProperHttpRequestMethod() throws Exception {
        transport.publicV3PingGet();
        ArgumentCaptor<HttpUriRequest> actual = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(actual.capture());
        assertEquals("GET", actual.getValue().getMethod());
    }

    @Test
    public void publicPingGetCallsHttpClientWithProperHttpRequestUri() throws Exception {
        URI expected = URI.create(baseUrl.concat("/public/v3/ping"));
        transport.publicV3PingGet();
        verify(httpClient).execute(requestCaptor.capture());
        assertEquals(expected, requestCaptor.getValue().getURI());
    }

    @Test
    public void publicPingGetParsesResponseWithProperClass() throws Exception {
        transport.publicV3PingGet();
        verify(objectMapper).readValue(any(InputStream.class), eq(PublicV3PingGetResponse.class));
    }

    @Test
    public void publicPingGetReturnsParsedJson() throws Exception {
        PublicV3PingGetResponse expected = new PublicV3PingGetResponse(new Date());
        when(objectMapper.readValue(any(InputStream.class), any(Class.class))).thenReturn(expected);
        PublicV3PingGetResponse actual = transport.publicV3PingGet();
        assertEquals(expected, actual);
    }

    @Test(expected = CommunicationErrorException.class)
    public void publicPingThrowsCommunicationErrorExceptionWhenHttpClientThrowsIOError() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(new IOException());
        transport.publicV3PingGet();
    }

    @Test(expected = InvalidResponseException.class)
    public void publicPingThrowsInvalidResponseExceptionWhenObjectParserThrowsJsonMappingException() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(Class.class)))
                .thenThrow(mock(JsonMappingException.class));
        transport.publicV3PingGet();
    }

    @Test(expected = InvalidResponseException.class)
    public void publicPingThrowsInvalidResponseExceptionWhenObjectParserThrowsJsonParseException() throws Exception {
        when(objectMapper.readValue(any(InputStream.class), any(Class.class)))
                .thenThrow(mock(JsonParseException.class));
        transport.publicV3PingGet();
    }
}