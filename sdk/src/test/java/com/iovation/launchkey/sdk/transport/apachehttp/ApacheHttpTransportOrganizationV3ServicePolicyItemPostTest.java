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
import com.iovation.launchkey.sdk.transport.domain.PolicyAdapter;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicy;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicyItemPostRequest;
import org.junit.Before;
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
public class ApacheHttpTransportOrganizationV3ServicePolicyItemPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private ServicePolicyItemPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Mock
    private ServicePolicy result;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(objectMapper.readValue(anyString(), eq(ServicePolicy.class))).thenReturn(result);
    }

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3PolicyItemPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/organization/v3/service/policy/item")));
    }

    @Test
    public void marshalsExpectedRequestData() throws Exception {
        transport.organizationV3PolicyItemPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.organizationV3PolicyItemPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.organizationV3PolicyItemPost(request, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }

    @Test
    public void responseIsParsedProperlyAndReturned() throws Exception {
        when(objectMapper.readValue(anyString(), eq(ServicePolicy.class))).thenReturn(result);
        PolicyAdapter adapter = transport.organizationV3PolicyItemPost(request, entityIdentifier);
        ServicePolicy actual = (ServicePolicy) adapter;
        verify(objectMapper).readValue(anyString(), eq(ServicePolicy.class));
        assertEquals(result, actual);
    }

}