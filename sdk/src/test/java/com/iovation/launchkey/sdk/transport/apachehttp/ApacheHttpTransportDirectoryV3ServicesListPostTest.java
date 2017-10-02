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
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.ServicesListPostResponseService;
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
public class ApacheHttpTransportDirectoryV3ServicesListPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private ServicesListPostRequest servicesListPostRequest;

    @Mock
    private EntityIdentifier entityIdentifier;
    private ServicesListPostResponseService[] result;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        result = new ServicesListPostResponseService[]{mock(ServicesListPostResponseService.class)};
        when(objectMapper.readValue(anyString(), eq(ServicesListPostResponseService[].class))).thenReturn(result);
    }

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.directoryV3ServicesListPost(servicesListPostRequest, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/directory/v3/services/list")));
    }

    @Test
    public void marshalsExpectedRequestData() throws Exception {
        transport.directoryV3ServicesListPost(servicesListPostRequest, entityIdentifier);
        verify(objectMapper).writeValueAsString(servicesListPostRequest);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.directoryV3ServicesListPost(servicesListPostRequest, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.directoryV3ServicesListPost(servicesListPostRequest, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void responseIsParsedProperlyAndReturned() throws Exception {
        when(objectMapper.readValue(anyString(), eq(ServicesListPostResponseService[].class))).thenReturn(result);
        ServicesListPostResponse expected = new ServicesListPostResponse(Arrays.asList(result));
        ServicesListPostResponse actual =
                transport.directoryV3ServicesListPost(servicesListPostRequest, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(ServicesListPostResponseService[].class));
        assertEquals(expected, actual);
    }
}