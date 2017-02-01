package com.launchkey.sdk.transport.apachehttp; /**
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

import com.launchkey.sdk.transport.domain.ServiceV3SessionsPostRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ApacheHttpTransportApplicationSessionsPostTest extends ApacheHttpTransportTestBase {
    @Test
    public void callsHttpClientWithProperHttpRequestMethod() throws Exception {
        transport.serviceV3SessionsPost(mock(ServiceV3SessionsPostRequest.class), null);
        ArgumentCaptor<HttpUriRequest> actual = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(actual.capture());
        assertEquals("POST", actual.getValue().getMethod());
    }

    @Test
    public void callsHttpClientWithProperHttpRequestUri() throws Exception {
        URI expected = URI.create(baseUrl.concat("/application/v3/sessions"));
        transport.serviceV3SessionsPost(mock(ServiceV3SessionsPostRequest.class), null);
        ArgumentCaptor<HttpUriRequest> actual = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(actual.capture());
        assertEquals(expected, actual.getValue().getURI());
    }

    @Test
    public void marshalsPassedRequestObject() throws Exception {
        ServiceV3SessionsPostRequest expected = mock(ServiceV3SessionsPostRequest.class);
        transport.serviceV3SessionsPost(expected, null);
        ArgumentCaptor<ServiceV3SessionsPostRequest> captor = ArgumentCaptor.forClass(ServiceV3SessionsPostRequest.class);
        verify(objectMapper).writeValueAsString(captor.capture());
        assertSame(expected, captor.getValue());
    }

    @Test
    public void encryptsMarshaledRequestObject() throws Exception {


    }
}