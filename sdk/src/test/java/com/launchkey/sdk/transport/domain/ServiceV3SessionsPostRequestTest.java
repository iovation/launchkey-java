package com.launchkey.sdk.transport.domain; /**
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ServiceV3SessionsPostRequestTest {
    @Test
    public void getServiceId() throws Exception {
        UUID expected = UUID.randomUUID();
        UUID actual = new ServiceV3SessionsPostRequest(expected, null, null).getServiceId();
        assertEquals(expected, actual);
    }

    @Test
    public void getEndUserIdentifier() throws Exception {
        String expected = "Expected User Identifier";
        String actual = new ServiceV3SessionsPostRequest(null, expected, null).getEndUserIdentifier();
        assertEquals(expected, actual);
    }

    @Test
    public void getAuthorizationRequestId() throws Exception {
        UUID expected = UUID.randomUUID();
        UUID actual = new ServiceV3SessionsPostRequest(null, null, expected).getAuthorizationRequestId();
        assertEquals(expected, actual);
    }

    @Test
    public void marshalsFullJsonAsExpected() throws Exception {
        ServiceV3SessionsPostRequest request = new ServiceV3SessionsPostRequest(
                UUID.randomUUID(),
                "USER IDENTIFIER",
                UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d")
        );
        String expected = "{\"username\":\"USER IDENTIFIER\"," +
                "\"auth_request\":\"319d2db1-3965-4f2e-89a0-26572ddbf31d\"}";
        String actual = new ObjectMapper().writeValueAsString(request);
        assertEquals(expected, actual);

    }

    @Test
    public void marshalLeavesOutAuthRequestWhenAuthorizationRequestIdIsNull() throws Exception {
        ServiceV3SessionsPostRequest request = new ServiceV3SessionsPostRequest(
                UUID.randomUUID(),
                "USER IDENTIFIER",
                null
        );
        String expected = "{\"username\":\"USER IDENTIFIER\"}";
        String actual = new ObjectMapper().writeValueAsString(request);
        assertEquals(expected, actual);
    }

    @Test
    public void equalObjectsAreEqual() throws Exception {
        assertEquals(
                new ServiceV3SessionsPostRequest(
                        UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d"),
                        "UserName",
                        UUID.fromString("baa53338-ff66-4d27-a3bb-5bdd3b5f2890")
                ),
                new ServiceV3SessionsPostRequest(
                        UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d"),
                        "UserName",
                        UUID.fromString("baa53338-ff66-4d27-a3bb-5bdd3b5f2890")
                )
        );
    }

    @Test
    public void equalObjectsHaveSameHashcode() throws Exception {
        assertEquals(
                new ServiceV3SessionsPostRequest(
                        UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d"),
                        "UserName",
                        UUID.fromString("baa53338-ff66-4d27-a3bb-5bdd3b5f2890")
                ).hashCode(),
                new ServiceV3SessionsPostRequest(
                        UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d"),
                        "UserName",
                        UUID.fromString("baa53338-ff66-4d27-a3bb-5bdd3b5f2890")
                ).hashCode()
        );
    }
}