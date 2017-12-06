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
import com.iovation.launchkey.sdk.transport.domain.ServicesGetResponse;
import com.iovation.launchkey.sdk.transport.domain.ServicesGetResponseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportOrganizationV3ServicesGetTest extends ApacheHttpTransportTestBase {
    @Mock
    private EntityIdentifier entityIdentifier;
    private ServicesGetResponseService[] result;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        result = new ServicesGetResponseService[]{mock(ServicesGetResponseService.class)};
        when(objectMapper.readValue(anyString(), eq(ServicesGetResponseService[].class))).thenReturn(result);
    }

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3ServicesGet(entityIdentifier);
        verifyCall("GET", URI.create(baseUrl.concat("/organization/v3/services")));
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.organizationV3ServicesGet(entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        eq((String) null), eq((String) null));

    }

    @Test
    public void responseIsParsedProperlyAndReturned() throws Exception {
        when(objectMapper.readValue(anyString(), eq(ServicesGetResponseService[].class))).thenReturn(result);
        ServicesGetResponse expected = new ServicesGetResponse(Arrays.asList(result));
        ServicesGetResponse actual = transport.organizationV3ServicesGet(entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(ServicesGetResponseService[].class));
        assertEquals(expected, actual);
    }
}