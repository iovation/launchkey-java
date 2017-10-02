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
import com.iovation.launchkey.sdk.transport.domain.ServicesPatchRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.net.URI;
import java.security.PublicKey;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.junit.MockitoJUnitRunner.Silent.class)
public class ApacheHttpTransportDirectoryV3ServicesPatchTest extends ApacheHttpTransportTestBase {
    @Mock
    private ServicesPatchRequest servicesPatchRequest;

    @Mock
    private EntityIdentifier entityIdentifier;

    @Test
    public void sendsRequestWithProperMethodAndPath() throws Exception {
        transport.directoryV3ServicesPatch(servicesPatchRequest, entityIdentifier);
        verifyCall("PATCH", URI.create(baseUrl.concat("/directory/v3/services")));
    }

    @Test
    public void marshalsExpectedServiceData() throws Exception {
        transport.directoryV3ServicesPatch(servicesPatchRequest, entityIdentifier);
        verify(objectMapper).writeValueAsString(servicesPatchRequest);
    }

    @Test
    public void encryptsDataWithMarshaledValue() throws Exception {
        when(objectMapper.writeValueAsString(any(Object.class))).thenReturn("Expected");
        transport.directoryV3ServicesPatch(servicesPatchRequest, entityIdentifier);
        verify(jweService).encrypt(eq("Expected"), any(PublicKey.class), anyString(), anyString());
    }

    @Test
    public void requestSubjectIsUsedForSignature() throws Exception {
        when(entityIdentifier.toString()).thenReturn("Expected");
        transport.directoryV3ServicesPatch(servicesPatchRequest, entityIdentifier);
        verify(jwtService)
                .encode(anyString(), anyString(), eq("Expected"), any(Date.class), anyString(), anyString(),
                        anyString(),
                        anyString());

    }
}