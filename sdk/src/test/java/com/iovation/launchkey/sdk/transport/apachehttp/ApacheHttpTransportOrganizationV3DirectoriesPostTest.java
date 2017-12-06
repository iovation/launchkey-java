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

import com.iovation.launchkey.sdk.transport.domain.*;
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
public class ApacheHttpTransportOrganizationV3DirectoriesPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private OrganizationV3DirectoriesPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3DirectoriesPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/organization/v3/directories")));
    }

    @Test
    public void marshalsExpectedServiceData() throws Exception {
        transport.organizationV3DirectoriesPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.organizationV3DirectoriesPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.organizationV3DirectoriesPost(request, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());
    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        OrganizationV3DirectoriesPostResponse expected = mock(OrganizationV3DirectoriesPostResponse.class);
        when(objectMapper.readValue(anyString(), eq(OrganizationV3DirectoriesPostResponse.class))).thenReturn(expected);
        OrganizationV3DirectoriesPostResponse actual = transport.organizationV3DirectoriesPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(OrganizationV3DirectoriesPostResponse.class));
        assertEquals(expected, actual);
    }
}