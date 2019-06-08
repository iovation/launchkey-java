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

import com.iovation.launchkey.sdk.transport.domain.DirectoryV3SessionsListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3SessionsListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3SessionsListPostResponseSession;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportDirectoryV3SessionsListPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private DirectoryV3SessionsListPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;
    private DirectoryV3SessionsListPostResponseSession[] result;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        result = new DirectoryV3SessionsListPostResponseSession[]{mock(DirectoryV3SessionsListPostResponseSession.class)};
        when(objectMapper.readValue(anyString(), eq(DirectoryV3SessionsListPostResponseSession[].class))).thenReturn(result);
    }

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.directoryV3SessionsListPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/directory/v3/sessions/list")));
    }

    @Test
    public void marshalsExpectedRequestData() throws Exception {
        transport.directoryV3SessionsListPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.directoryV3SessionsListPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.directoryV3SessionsListPost(request, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void responseIsParsedProperlyAndReturned() throws Exception {
        when(objectMapper.readValue(anyString(), eq(DirectoryV3SessionsListPostResponseSession[].class))).thenReturn(result);
        DirectoryV3SessionsListPostResponse expected = new DirectoryV3SessionsListPostResponse(Arrays.asList(result));
        DirectoryV3SessionsListPostResponse actual =
                transport.directoryV3SessionsListPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(DirectoryV3SessionsListPostResponseSession[].class));
        assertEquals(expected, actual);
    }
}