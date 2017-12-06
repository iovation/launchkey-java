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

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrganizationV3DirectoryKeysPatchRequestTest {

    @Test
    public void getDirectoryId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new OrganizationV3DirectoryKeysPatchRequest(id, null, null, false).getDirectoryId());
    }

    @Test
    public void getKeyId() throws Exception {
        assertEquals("Key ID", new OrganizationV3DirectoryKeysPatchRequest(null, "Key ID", null, false).getKeyId());
    }

    @Test
    public void getExpires() throws Exception {
        Date expires = new Date(1000L);
        assertEquals(expires, new OrganizationV3DirectoryKeysPatchRequest(null, null, expires, false).getExpires());
    }

    @Test
    public void isActive() throws Exception {
        assertTrue(new OrganizationV3DirectoryKeysPatchRequest(null, null, null, true).isActive());
    }

    @Test
    public void toJSON() throws Exception {
        String expected = "{\"directory_id\":\"67c87654-aed9-11e7-98e9-0469f8dc10a5\",\"key_id\":\"Key ID\"," +
                "\"date_expires\":\"2001-02-03T04:05:06Z\",\"active\":true}";
        String actual = new ObjectMapper().writeValueAsString(
                new OrganizationV3DirectoryKeysPatchRequest(UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5"), "Key ID",
                        new Date(981173106000L), true));
        assertEquals(expected, actual);
    }
}