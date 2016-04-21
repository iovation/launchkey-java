package com.launchkey.sdk.transport.v1;

import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.InvalidRequestException;
import com.launchkey.sdk.transport.v1.domain.LogsRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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
public class ApacheHttpClientTransportLogsTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
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
        String responseBody = "{\"message\": \"Successfully updated\"}";
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
    public void testExecutesPUT() throws Exception {
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getMethod(), is("PUT"));
    }

    @Test
    public void testUsesCorrectURL() throws Exception {
        transport = new ApacheHttpClientTransport(httpClient, "https://api.launchkey.com/v1", crypto);
        ArgumentCaptor<HttpUriRequest> request = ArgumentCaptor.forClass(HttpUriRequest.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(request.getValue().getURI().getPath(), is("/v1/logs"));
    }

    @Test
    public void testPassesAction() throws Exception {
        ArgumentCaptor<HttpEntityEnclosingRequestBase> request = ArgumentCaptor.forClass(HttpEntityEnclosingRequestBase.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(getStringFromReader(request.getValue().getEntity().getContent()), containsString("action=Authenticate"));
    }

    @Test
    public void testPassesStatus() throws Exception {
        ArgumentCaptor<HttpEntityEnclosingRequestBase> request = ArgumentCaptor.forClass(HttpEntityEnclosingRequestBase.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(getStringFromReader(request.getValue().getEntity().getContent()), containsString("status=true"));
    }

    @Test
    public void testPassesAuthRequest() throws Exception {
        ArgumentCaptor<HttpEntityEnclosingRequestBase> request = ArgumentCaptor.forClass(HttpEntityEnclosingRequestBase.class);
        transport.logs(new LogsRequest("Authenticate", true, "expected_auth_request", 0L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(getStringFromReader(request.getValue().getEntity().getContent()), containsString("auth_request=expected_auth_request"));
    }

    @Test
    public void testPassesAppKey() throws Exception {
        ArgumentCaptor<HttpEntityEnclosingRequestBase> request = ArgumentCaptor.forClass(HttpEntityEnclosingRequestBase.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 1234567890L, null, null));
        verify(httpClient).execute(request.capture());
        assertThat(getStringFromReader(request.getValue().getEntity().getContent()), containsString("app_key=1234567890"));
    }

    @Test
    public void testPassesSecretKey() throws Exception {
        ArgumentCaptor<HttpEntityEnclosingRequestBase> request = ArgumentCaptor.forClass(HttpEntityEnclosingRequestBase.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, "SecretKey==", null));
        verify(httpClient).execute(request.capture());
        assertThat(getStringFromReader(request.getValue().getEntity().getContent()), containsString("secret_key=SecretKey%3D%3D"));
    }

    @Test
    public void testPassesSignature() throws Exception {
        ArgumentCaptor<HttpEntityEnclosingRequestBase> request = ArgumentCaptor.forClass(HttpEntityEnclosingRequestBase.class);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, "Signature=="));
        verify(httpClient).execute(request.capture());
        assertThat(getStringFromReader(request.getValue().getEntity().getContent()), containsString("signature=Signature%3D%3D"));
    }

    @Test
    public void testWrapsClientExceptionInLaunchKeyException() throws Exception {
        ClientProtocolException expectedCause = new ClientProtocolException();
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Exception caught processing logs request");
        expectedException.expectCause(is(expectedCause));

        when(httpClient.execute(any(HttpUriRequest.class))).thenThrow(expectedCause);
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf100ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 100, "Expected Message")
        );
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf300ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 300, "Expected Message")
        );
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf401ThrowsExpectedException() throws Exception {
        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 401, "Status Message")
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
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf500ThrowsExpectedException() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 500, "Expected Message")
        );
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
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
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
    }

    @Test
    public void testResponseStatusCodeOf400ReturnsHttpValuesWhenBodyNotParsable() throws Exception {
        expectedException.expect(CommunicationErrorException.class);
        expectedException.expectMessage("Expected Message");

        when(response.getStatusLine()).thenReturn(
                new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1), 400, "Expected Message")
        );
        when(response.getEntity()).thenReturn(
                EntityBuilder.create().setStream(new ByteArrayInputStream("Not Parsable".getBytes("UTF-8"))).build()
        );
        transport.logs(new LogsRequest("Authenticate", true, null, 0L, null, null));
    }

    @SuppressWarnings("Duplicates")
    private String getStringFromReader(InputStream content) throws Exception {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(content));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        return sb.toString();
    }
}