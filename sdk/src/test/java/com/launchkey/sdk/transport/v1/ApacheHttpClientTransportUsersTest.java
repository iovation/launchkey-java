package com.launchkey.sdk.transport.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.InvalidRequestException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.transport.v1.domain.UsersRequest;
import com.launchkey.sdk.transport.v1.domain.UsersResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicStatusLine;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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
public class ApacheHttpClientTransportUsersTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private HttpResponse response;
    private HttpClient httpClient;
    private Transport transport;
    private Crypto crypto;
    private Base64 base64 = new Base64(0);
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        httpClient = mock(HttpClient.class);
        response = mock(HttpResponse.class);
        crypto = mock(Crypto.class);
        when(response.getStatusLine()).thenReturn(new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 200, "OK"));
        String responseBody = "{\"successful\": true, \"message\": \"Expected Message\", \"message_code\": 1234, \"response\":{\"cipher\": \"expected cipher\",\"data\":\"expected data\"}}";
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream(responseBody.getBytes("UTF-8"))).build()
        );
        when(crypto.sign(any(byte[].class))).thenReturn("Expected Signature".getBytes());
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
    public void testExecutesPost() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.users(new UsersRequest(null, 0L, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getMethod(), is("POST"));
    }

    @Test
    public void testUsesCorrectURL() throws Exception {
        transport = new ApacheHttpClientTransport(httpClient, "https://api.launchkey.com/v1", crypto);
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.users(new UsersRequest(null, 0L, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getPath(), is("/v1/users"));
    }

    @Test
    public void testPassesSignatureInQuery() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.users(new UsersRequest(null, 0L, null));
        verify(httpClient).execute(request.capture());
        assertEquals(
                request.getValue().getURI().getRawQuery(),
                "signature=" + base64.encodeAsString("Expected Signature".getBytes())
        );
    }

    @Test
    public void testPostBodyHasExpectedData() throws Exception {
        UsersRequest usersRequest = new UsersRequest("identifier", 0L, "secret key");
        String expected = objectMapper.writeValueAsString(usersRequest);
        ArgumentCaptor<HttpPost> request = ArgumentCaptor.forClass(HttpPost.class);
        transport.users(usersRequest);
        verify(httpClient).execute(request.capture());
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getValue().getEntity().getContent()));
        String segment;
        while ((segment = reader.readLine()) != null) {
            builder.append(segment);
        }
        assertEquals(expected, builder.toString());
    }

    @Test
    public void testReturnsExpectedResponse() throws Exception {
        UsersResponse expected = new UsersResponse(
                new UsersResponse.UsersResponseResponse("expected cipher", "expected data"), true, 1, null
        );
        UsersResponse actual = transport.users(new UsersRequest(null, 0L, null));
        assertEquals(expected, actual);
    }

    @Test
    public void testWrapsClientExceptionInLaunchKeyException() throws Exception {
        ClientProtocolException expectedCause = new ClientProtocolException();
        expectedException.expect(ApiException.class);
        expectedException.expectMessage("Exception processing users request");
        expectedException.expectCause(is(expectedCause));

        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(expectedCause);
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testThrowsExpectedExceptionWhenInvalidResponse() throws Exception {
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Invalid".getBytes())).build()
        );

        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Error parsing response body");
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testResponseStatusCodeOf100ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 100, "Expected Message")
        );
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testResponseStatusCodeOf300ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 300, "Expected Message")
        );
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testResponseStatusCodeOf401ThrowsExpectedException() throws Exception {
        expectedException.expect(ApiException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, "Expected Message")
        );
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testResponseStatusCodeOf500ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Expected Message")
        );
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testResponseStatusCodeOf400ReturnsBodyErrorValues() throws Exception {
        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "Status Message")
        );
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(
                        new ByteArrayInputStream(
                                "{\"message_code\": 70401, \"message\": \"Expected Message\"}".getBytes("UTF-8")
                        )
                ).build()
        );
        expectedException.expect(InvalidRequestException.class);
        expectedException.expectMessage("Expected Message");
        transport.users(new UsersRequest(null, 0L, null));
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
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testUnparseableBodyThrowsExpectedException() throws Exception {
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Error parsing response body");

        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Unparseable".getBytes("UTF-8"))).build()
        );
        transport.users(new UsersRequest(null, 0L, null));
    }

    @Test
    public void testResponseIsSuccessfulFalseThrowsExpectedException() throws Exception {
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(
                        new ByteArrayInputStream(
                                "{\"successful\": false, \"message_code\": 1000, \"message\": \"Expected Special Message\", \"response\": \"\"}".getBytes("UTF-8")
                        )
                ).build()
        );
        expectedException.expect(ApiException.class);
        expectedException.expectMessage("Expected Special Message");
        transport.users(new UsersRequest(null, 0L, null));
    }
}