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
import com.iovation.launchkey.sdk.transport.domain.ServicesPostRequest;
import com.iovation.launchkey.sdk.transport.domain.ServicesPostResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportOrganizationV3ServicesPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private ServicesPostRequest servicesPostRequest;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3ServicesPost(servicesPostRequest, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/organization/v3/services")));
    }

    @Test
    public void marshalsExpectedServiceData() throws Exception {
        transport.organizationV3ServicesPost(servicesPostRequest, entityIdentifier);
        verify(objectMapper).writeValueAsString(servicesPostRequest);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.organizationV3ServicesPost(servicesPostRequest, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.organizationV3ServicesPost(servicesPostRequest, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        ServicesPostResponse expected = mock(ServicesPostResponse.class);
        when(objectMapper.readValue(anyString(), eq(ServicesPostResponse.class))).thenReturn(expected);
        ServicesPostResponse actual = transport.organizationV3ServicesPost(servicesPostRequest, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(ServicesPostResponse.class));
        assertEquals(expected, actual);
    }
}