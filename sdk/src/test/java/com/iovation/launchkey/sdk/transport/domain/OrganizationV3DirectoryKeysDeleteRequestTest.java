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

public class OrganizationV3DirectoryKeysDeleteRequestTest {
    @Test
    public void getDirectoryId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new OrganizationV3DirectoryKeysDeleteRequest(id, null).getDirectoryId());
    }

    @Test
    public void getKeyId() throws Exception {
        assertEquals("Key ID", new OrganizationV3DirectoryKeysDeleteRequest(null, "Key ID").getKeyId());
    }

    @Test
    public void toJSON() throws Exception {
        String expected = "{\"directory_id\":\"ae29d126-b073-11e7-810c-0469f8dc10a5\",\"key_id\":\"Key ID\"}";
        String actual = new ObjectMapper().writeValueAsString(
                new OrganizationV3DirectoryKeysDeleteRequest(UUID.fromString("ae29d126-b073-11e7-810c-0469f8dc10a5"), "Key ID"));
        assertEquals(expected, actual);
    }
}