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

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class OrganizationV3DirectorySdkKeysDeleteRequestTest {
    @Test
    public void getDirectoryId() throws Exception {
        UUID expected = UUID.randomUUID();
        assertEquals(expected, new OrganizationV3DirectorySdkKeysDeleteRequest(expected, null).getDirectoryId());
    }

    @Test
    public void getSdkKey() throws Exception {
        UUID expected = UUID.randomUUID();
        assertEquals(expected, new OrganizationV3DirectorySdkKeysDeleteRequest(null, expected).getSdkKey());
    }

    @Test
    public void toJSON() throws Exception {
        assertEquals(
                "{\"directory_id\":\"0d3697e1-b5da-11e7-a831-0469f8dc10a5\"," +
                        "\"sdk_key\":\"891918a6-b5e6-11e7-942f-0469f8dc10a5\"}",
                new ObjectMapper().writeValueAsString(new OrganizationV3DirectorySdkKeysDeleteRequest(
                        UUID.fromString("0d3697e1-b5da-11e7-a831-0469f8dc10a5"),
                        UUID.fromString("891918a6-b5e6-11e7-942f-0469f8dc10a5"))));
    }
}