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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.jwt.JWTService;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.EntityKeyMap;
import com.iovation.launchkey.sdk.crypto.jwe.JWEService;
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
    EntityIdentifier issuer;
    private JWTService jwtService;
    private JWEService jweService;
    private Crypto crypto;
    private Cache publicKeyCache;
    private EntityKeyMap entityKeyMap;

    @Before
    public void setUp() throws Exception {
        httpClient = mock(HttpClient.class);
        crypto = mock(Crypto.class);
        publicKeyCache = mock(Cache.class);
        jwtService = mock(JWTService.class);
        jweService = mock(JWEService.class);
        entityKeyMap = mock(EntityKeyMap.class);
        httpResponse = mock(HttpResponse.class);
        when(httpResponse.getFirstHeader("Content-Type"))
                .thenReturn(new BasicHeader("Content-Type", "application/jose"));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream("Hello World!".getBytes()));
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(
                new ProtocolVersion("HTTP", 1, 1),
                200,
                "OK"
        ));

        when(httpClient.execute(any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

        provider = new BouncyCastleProvider();
        objectMapper = mock(ObjectMapper.class);
        baseUrl = "https://base.url";
        issuer = mock(EntityIdentifier.class);
        audience = "Expected Audience";
        transport = new ApacheHttpTransport(
                httpClient,
                crypto,
                objectMapper,
                publicKeyCache,
                baseUrl,
                issuer,
                jwtService,
                jweService,
                0,
                0,
                entityKeyMap
        );
    }

    @After
    public void tearDown() throws Exception {
        transport = null;
        httpClient = null;
        provider = null;
        entityKeyMap = null;
        httpResponse = null;
        objectMapper = null;
        publicKeyCache = null;
        jweService = null;
        jwtService = null;
        crypto = null;
        baseUrl = null;
        audience = null;
        issuer = null;
    }
}