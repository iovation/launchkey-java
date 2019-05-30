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

import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.KeysPostResponse;
import com.iovation.launchkey.sdk.transport.domain.ServiceKeysPostRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportDirectoryV3ServiceKeysPostTest
        extends ApacheHttpTransportTestBase {

    @Mock
    private ServiceKeysPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;


    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.directoryV3ServiceKeysPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/directory/v3/service/keys")));
    }

    @Test
    public void marshalsExpectedData() throws Exception {
        transport.directoryV3ServiceKeysPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.directoryV3ServiceKeysPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        KeysPostResponse expected = mock(KeysPostResponse.class);
        when(objectMapper.readValue(anyString(), eq(KeysPostResponse.class))).thenReturn(expected);
        KeysPostResponse actual = transport.directoryV3ServiceKeysPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(KeysPostResponse.class));
        assertEquals(expected, actual);
    }
}