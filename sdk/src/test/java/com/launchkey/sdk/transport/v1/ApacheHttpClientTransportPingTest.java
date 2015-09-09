package com.launchkey.sdk.transport.v1;

import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.InvalidRequestException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.LaunchKeyException;
import com.launchkey.sdk.transport.v1.domain.PingRequest;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicStatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ApacheHttpClientTransportPingTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HttpResponse response;
    private HttpClient httpClient;
    private Transport transport;
    private Crypto crypto;

    @Before
    public void setUp() throws Exception {
        httpClient = mock(HttpClient.class);
        response = mock(HttpResponse.class);
        crypto = mock(Crypto.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
        String responseBody = "{\"date_stamp\" : \"2001-01-01 01:01:01\"," +
                "\"launchkey_time\" : \"2002-02-02 02:02:02\"," +
                "\"key\" : \"Expected Public Key\"}";
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream(responseBody.getBytes("UTF-8"))).build()
        );
        transport = new ApacheHttpClientTransport(httpClient, "https://api.launchkey.com/v1", crypto);
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(response);
    }

    @After
    public void tearDown() throws Exception {
        httpClient = null;
        response = null;
        transport = null;
        crypto = null;
    }

    @Test
    public void testPingExecutesGet() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.ping(new PingRequest());
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getMethod(), is("GET"));
    }

    @Test
    public void testPingUsesCorrectURL() throws Exception {
        transport = new ApacheHttpClientTransport(httpClient, "https://api.launchkey.com/v1", crypto);
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.ping(new PingRequest());
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getPath(), is("/v1/ping"));
    }

    @Test
    public void testPingDoesNotPassDateStampWhenNoneProvided() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.ping(new PingRequest());
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getRawQuery(), not(containsString("date_stamp")));
    }

    @Test
    public void testPingDoesPassDateStampWhenProvided() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.ping(new PingRequest(new Date(0L)));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getRawQuery(), containsString("date_stamp=1970-01-01+00%3A00%3A00"));
    }

    @Test
    public void testPingReturnsExpectedResponse() throws Exception {
        PingResponse expected = new PingResponse("2001-01-01 01:01:01", "2002-02-02 02:02:02", "Expected Public Key");
        PingResponse actual = transport.ping(new PingRequest());
        assertEquals(expected, actual);
    }

    @Test
    public void testPingWrapsClientExceptionInLaunchKeyException() throws Exception {
        ClientProtocolException expectedCause = new ClientProtocolException();
        expectedException.expect(LaunchKeyException.class);
        expectedException.expectMessage("Exception caught processing ping request");
        expectedException.expectCause(is(expectedCause));

        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(expectedCause);
        transport.ping(new PingRequest());
    }

    @Test
    public void testPingThrowsExpectedExceptionWhenInvalidResponse() throws Exception {
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Invalid".getBytes())).build()
        );

        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Error parsing response body");
        transport.ping(new PingRequest());
    }

    @Test
    public void testResponseStatusCodeOf100ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 100, "Expected Message")
        );
        transport.ping(new PingRequest());
    }

    @Test
    public void testResponseStatusCodeOf300ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 300, "Expected Message")
        );
        transport.ping(new PingRequest());
    }

    @Test
    public void testResponseStatusCodeOf401ThrowsExpectedException() throws Exception {
        expectedException.expect(LaunchKeyException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, "Expected Message")
        );
        transport.ping(new PingRequest());
    }

    @Test
    public void testResponseStatusCodeOf500ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Expected Message")
        );
        transport.ping(new PingRequest());
    }

    @Test
    public void testResponseStatusCodeOf400ReturnsBodyErrorValues() throws Exception {
        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "Status Message")
        );
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(
                        new ByteArrayInputStream(
                                "{\"message_code\": 60401, \"message\": \"Expected Message\"}".getBytes("UTF-8")
                        )
                ).build()
        );
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage("Expected Message");
        transport.ping(new PingRequest());
    }

    @Test
    public void testResponseStatusCodeOf400ReturnsHttpValuesWhenBodyNotParseable() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "Expected Message")
        );
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Unparseable".getBytes("UTF-8"))).build()
        );
        transport.ping(new PingRequest());
    }

    @Test
    public void testUnparseableBodyThrowsExpectedException() throws Exception {
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Error parsing response body");

        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Unparseable".getBytes("UTF-8"))).build()
        );
        transport.ping(new PingRequest());
    }
}