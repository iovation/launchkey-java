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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.domain.EntityIdentifier;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import javax.cache.spi.CachingProvider;
import java.io.ByteArrayInputStream;
import java.security.Provider;

import static org.mockito.Mockito.*;

public class ApacheHttpTransportTestBase {

    ApacheHttpTransport transport;
    HttpClient httpClient;
    Provider provider;
    String baseUrl;
    String audience;
    HttpResponse httpResponse;
    ObjectMapper objectMapper;
    CachingProvider cachingProvider;
    EntityIdentifier issuer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        httpClient = mock(HttpClient.class);
        httpResponse = mock(HttpResponse.class);
        when(httpResponse.getFirstHeader("Content-Type"))
                .thenReturn(new BasicHeader("Content-Type", "application/json"));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream("Hello World!".getBytes()));
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(
                new ProtocolVersion("HTTP", 1, 1),
                200,
                "OK"
        ));
        when(httpClient.execute(any(HttpUriRequest.class))).thenReturn(httpResponse);
        provider = new BouncyCastleProvider();
        objectMapper = mock(ObjectMapper.class);
        cachingProvider = mock(CachingProvider.class);
        baseUrl = "https://base.url";
        issuer = mock(EntityIdentifier.class);
        audience = "Expected Audience";
//        transport = new ApacheHttpTransport(httpClient, provider, objectMapper, cachingProvider, baseUrl, issuer, audience, jwtService, jweService);
    }

    @After
    public void tearDown() throws Exception {
        transport = null;
        httpClient = null;
        provider = null;
        httpResponse = null;
        objectMapper = null;
        cachingProvider = null;
        baseUrl = null;
        audience = null;
        issuer = null;
    }
}