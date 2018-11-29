/**
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

package com.iovation.launchkey.sdk.transport.apachehttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.cache.Cache;
import com.iovation.launchkey.sdk.crypto.Crypto;
import com.iovation.launchkey.sdk.crypto.jwe.JWEService;
import com.iovation.launchkey.sdk.crypto.jwt.JWTClaims;
import com.iovation.launchkey.sdk.crypto.jwt.JWTData;
import com.iovation.launchkey.sdk.crypto.jwt.JWTService;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.EntityKeyMap;
import com.iovation.launchkey.sdk.transport.domain.PublicV3PingGetResponse;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ApacheHttpTransportTestBase {

    final static String baseUrl = "https://base.url";
    final static String audience = "Expected Audience";

    @Mock protected ApacheHttpTransport transport;
    @Mock protected HttpClient httpClient;
    @Mock private Provider provider;
    @Mock protected HttpResponse httpResponse;
    @Mock protected ObjectMapper objectMapper;
    @Mock protected EntityIdentifier issuer;
    @Mock protected JWTService jwtService;
    @Mock protected JWTData jwtData;
    @Mock protected JWTClaims jwtClaims;
    @Mock protected JWEService jweService;
    @Mock protected Crypto crypto;
    @Mock private Cache publicKeyCache;
    @Mock protected EntityKeyMap entityKeyMap;
    @Mock private PublicV3PingGetResponse pingResponse;
    @Mock private RSAPublicKey publicKey;
    @Captor protected ArgumentCaptor<HttpUriRequest> requestCaptor;

    @Before
    public void setUp() throws Exception {
        when(httpResponse.getFirstHeader("X-IOV-KEY-ID")).thenReturn(new BasicHeader("X-IOV-KEY-ID", "Key ID"));
        when(httpResponse.getFirstHeader("X-IOV-JWT")).thenReturn(new BasicHeader("X-IOV-JWT", "JWT Header"));
        when(httpResponse.getFirstHeader("Content-Type"))
                .thenReturn(new BasicHeader("Content-Type", "application/jose"));
        when(objectMapper.readValue(any(InputStream.class), any(Class.class))).thenReturn(new Object());
        when(objectMapper.readValue(any(InputStream.class), eq(PublicV3PingGetResponse.class))).thenReturn(pingResponse);
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Marshaled Value");
        when(publicKeyCache.get(anyString())).thenReturn("Public Key");
        when(pingResponse.getApiTime()).thenReturn(new Date());
        when(jweService.encrypt(anyString(), any(PublicKey.class), anyString(), anyString())).thenReturn("Encrypted");
        when(jweService.decrypt(anyString())).thenReturn("Decrypted");
        when(jwtService.getJWTData(anyString())).thenReturn(jwtData);
        when(jwtService.decode(any(PublicKey.class), anyString(), anyString(), any(Date.class), anyString())).thenReturn(jwtClaims);
        byte[] contentHash = MessageDigest.getInstance("SHA-256", new BouncyCastleProvider()).digest("Hello World!".getBytes());
        when(crypto.sha256(any(byte[].class))).thenReturn(contentHash);
        when(crypto.sha384(any(byte[].class))).thenReturn(contentHash);
        when(crypto.sha512(any(byte[].class))).thenReturn(contentHash);
        when(jwtClaims.getStatusCode()).thenReturn(200);
        when(jwtClaims.getContentHashAlgorithm()).thenReturn("S256");
        when(jwtClaims.getContentHash()).thenReturn(Hex.encodeHexString(contentHash));
        when(crypto.getRSAPublicKeyFromPEM(anyString())).thenReturn(publicKey);
        HttpEntity entity = new ByteArrayEntity("Hello World!".getBytes());
        when(httpResponse.getEntity()).thenReturn(entity);
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(
                new ProtocolVersion("HTTP", 1, 1),
                200,
                "OK"
        ));

        when(httpClient.execute(any(HttpUriRequest.class)))
                .thenReturn(httpResponse);

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

    void verifyCall(String method, URI expectedURI) throws IOException {
        verify(httpClient, atLeastOnce()).execute(requestCaptor.capture());
        int found = 0;
        for (HttpUriRequest request : requestCaptor.getAllValues()) {
            if (request.getMethod().equals(method) && request.getURI().equals(expectedURI)) {
                found++;
            }
        }

        assertEquals(
                "Incorrect number of \"" + method + "\" requests for \"" + expectedURI.toString() + "\" in:\n    " +
                        requestCaptor.getAllValues().toString(), 1, found);
    }
}