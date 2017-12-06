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

public class OrganizationV3DirectoryKeysPostRequestTest {
    @Test
    public void getDirectoryId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new OrganizationV3DirectoryKeysPostRequest(id, null, null, false).getDirectoryId());
    }

    @Test
    public void getPublicKey() throws Exception {
        assertEquals("KEY", new OrganizationV3DirectoryKeysPostRequest(null, "KEY", null, false).getPublicKey());
    }

    @Test
    public void getExpires() throws Exception {
        Date date = new Date(0L);
        assertEquals(date, new OrganizationV3DirectoryKeysPostRequest(null, null, date, false).getExpires());

    }

    @Test
    public void isActive() throws Exception {
        assertTrue(new OrganizationV3DirectoryKeysPostRequest(null, null, null, true).isActive());
    }

    @Test
    public void toJSON() throws Exception {
        String expected = "{\"directory_id\":\"67c87654-aed9-11e7-98e9-0469f8dc10a5\",\"public_key\":\"Public Key\"," +
                "\"date_expires\":\"2001-02-03T04:05:06Z\",\"active\":true}";
        String actual = new ObjectMapper().writeValueAsString(
                new OrganizationV3DirectoryKeysPostRequest(UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5"), "Public Key",
                        new Date(981173106000L), true));
        assertEquals(expected, actual);
    }
}