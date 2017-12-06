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
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectorySdkKeysListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectorySdkKeysListPostResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportOrganizationV3DirectorySdkKeysListPostTest extends ApacheHttpTransportTestBase {
    @Mock
    private OrganizationV3DirectorySdkKeysListPostRequest request;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Before
    public void setUpThis() throws Exception {
        when(objectMapper.readValue(any(String.class), eq(UUID[].class))).thenReturn(new UUID[]{});
    }

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.organizationV3DirectorySdkKeysListPost(request, entityIdentifier);
        verifyCall("POST", URI.create(baseUrl.concat("/organization/v3/directory/sdk-keys/list")));
    }

    @Test
    public void marshalsExpectedData() throws Exception {
        transport.organizationV3DirectorySdkKeysListPost(request, entityIdentifier);
        verify(objectMapper).writeValueAsString(request);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.organizationV3DirectorySdkKeysListPost(request, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void parsedResponseIsReturned() throws Exception {
        List<UUID> expected = Arrays.asList(UUID.fromString("891918a6-b5e6-11e7-942f-0469f8dc10a5"),
                UUID.fromString("891918a6-b5e6-11e7-942f-0469f8dc10a5"));
        when(objectMapper.readValue(anyString(), eq(UUID[].class)))
                .thenReturn(new UUID[]{UUID.fromString("891918a6-b5e6-11e7-942f-0469f8dc10a5"),
                        UUID.fromString("891918a6-b5e6-11e7-942f-0469f8dc10a5")});
        OrganizationV3DirectorySdkKeysListPostResponse actual =
                transport.organizationV3DirectorySdkKeysListPost(request, entityIdentifier);
        verify(objectMapper).readValue(anyString(), eq(UUID[].class));
        assertEquals(expected, actual.getSdkKeys());
    }
}