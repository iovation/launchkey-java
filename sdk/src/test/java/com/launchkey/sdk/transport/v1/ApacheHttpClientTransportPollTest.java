package com.launchkey.sdk.transport.v1;

import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.InvalidRequestException;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.transport.v1.domain.PollRequest;
import com.launchkey.sdk.transport.v1.domain.PollResponse;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
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
public class ApacheHttpClientTransportPollTest {

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
        String responseBody = "{\"user_hash\": \"User Hash\", \"auth\": \"Auth\", \"organization_user\": \"Org User\", \"user_push_id\": \"Push ID\"}";
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
    public void testPollExecutesGet() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.poll(new PollRequest(null, 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getMethod(), is("GET"));
    }

    @Test
    public void testPollUsesCorrectURL() throws Exception {
        transport = new ApacheHttpClientTransport(httpClient, "https://api.launchkey.com/v1", crypto);
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.poll(new PollRequest(null, 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getPath(), is("/v1/poll"));
    }

    @Test
    public void testPollPassesAuthRequest() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.poll(new PollRequest("expected_auth_request", 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getRawQuery(), containsString("auth_request=expected_auth_request"));
    }

    @Test
    public void testPollPassesRocketKey() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.poll(new PollRequest(null, 1234567890L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getRawQuery(), containsString("app_key=1234567890"));
    }

    @Test
    public void testPollPassesSecretKey() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.poll(new PollRequest(null, 0L, "SecretKey==", null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getRawQuery(), containsString("secret_key=SecretKey%3D%3D"));
    }

    @Test
    public void testPollPassesSignature() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.poll(new PollRequest(null, 0L, null, "Signature=="));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getRawQuery(), containsString("signature=Signature%3D%3D"));
    }

    @Test
    public void testPollReturnsExpectedResponse() throws Exception {
        PollResponse expected = new PollResponse("Auth", "User Hash", "Org User", "Push ID");
        PollResponse actual = transport.poll(new PollRequest(null, 0L, null, null));
        assertEquals(expected, actual);
    }

    @Test
    public void testPollWrapsClientExceptionInLaunchKeyException() throws Exception {
        ClientProtocolException expectedCause = new ClientProtocolException();
        expectedException.expect(ApiException.class);
        expectedException.expectMessage("Exception caught processing poll request");
        expectedException.expectCause(is(expectedCause));

        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(expectedCause);
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testPollThrowsExpectedExceptionWhenInvalidResponse() throws Exception {
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Invalid".getBytes())).build()
        );

        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Error parsing response body");
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf100ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 100, "Expected Message")
        );
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf300ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 300, "Expected Message")
        );
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf401ThrowsExpectedException() throws Exception {
        expectedException.expect(ApiException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, "Expected Message")
        );
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf500ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Expected Message")
        );
        transport.poll(new PollRequest(null, 0L, null, null));
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
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf400WithMessageCodeOf70403ReturnsNull() throws Exception {
        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "Status Message")
        );
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(
                        new ByteArrayInputStream(
                                "{\"message_code\": 70403, \"message\": \"Pending response\"}".getBytes("UTF-8")
                        )
                ).build()
        );
        assertNull(transport.poll(new PollRequest(null, 0L, null, null)));
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
        transport.poll(new PollRequest(null, 0L, null, null));
    }

    @Test
    public void testUnparseableBodyThrowsExpectedException() throws Exception {
        expectedException.expect(InvalidResponseException.class);
        expectedException.expectMessage("Error parsing response body");

        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Unparseable".getBytes("UTF-8"))).build()
        );
        transport.poll(new PollRequest(null, 0L, null, null));
    }
}