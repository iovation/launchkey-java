package com.iovation.launchkey.sdk.transport.domain; /**
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

import static org.junit.Assert.assertEquals;

public class ServiceV3SessionsDeleteRequestTest {
    @Test
    public void getEndUserIdentifier() throws Exception {
        String expected = "Expected User Identifier";
        String actual = new ServiceV3SessionsDeleteRequest(expected).getEndUserIdentifier();
        assertEquals(expected, actual);
    }

    @Test
    public void marshalsFullJsonAsExpected() throws Exception {
        ServiceV3SessionsDeleteRequest request = new ServiceV3SessionsDeleteRequest("USER IDENTIFIER");
        String expected = "{\"username\":\"USER IDENTIFIER\"}";
        String actual = new ObjectMapper().writeValueAsString(request);
        assertEquals(expected, actual);
    }

    @Test
    public void equalObjectsAreEqual() throws Exception {
        assertEquals(
                new ServiceV3SessionsDeleteRequest("UserName"),
                new ServiceV3SessionsDeleteRequest("UserName")
        );
    }

    @Test
    public void equalObjectsHaveSameHashcode() throws Exception {
        assertEquals(
                new ServiceV3SessionsDeleteRequest("UserName").hashCode(),
                new ServiceV3SessionsDeleteRequest("UserName").hashCode()
        );
    }
}