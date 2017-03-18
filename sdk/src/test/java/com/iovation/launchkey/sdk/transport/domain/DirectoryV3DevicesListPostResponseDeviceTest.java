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

import static org.junit.Assert.*;

public class DirectoryV3DevicesListPostResponseDeviceTest {
    @Test
    public void getId() throws Exception {
        UUID expected = UUID.randomUUID();
        assertEquals(expected, new DirectoryV3DevicesListPostResponseDevice(expected, "name", "type", 0).getId());
    }

    @Test
    public void getType() throws Exception {
        assertEquals("type", new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0).getType());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name", new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 0).getName());
    }

    @Test
    public void getStatus() throws Exception {
        assertEquals(1, new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 1).getStatus());
    }

    @Test
    public void equalsIsTrueForEqualObjects() throws Exception {
        UUID id = UUID.randomUUID();
        DirectoryV3DevicesListPostResponseDevice device = new DirectoryV3DevicesListPostResponseDevice(id, "name", "type", 1);
        DirectoryV3DevicesListPostResponseDevice other = new DirectoryV3DevicesListPostResponseDevice(id, "name", "type", 1);
        assertTrue(device.equals(other));
    }

    @Test
    public void equalsIsFalseForUnequalObjects() throws Exception {
        DirectoryV3DevicesListPostResponseDevice device =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 1);
        DirectoryV3DevicesListPostResponseDevice other =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 1);
        assertFalse(device.equals(other));
    }

    @Test
    public void hashCodeForEqualObjectsIsEqual() throws Exception {
        UUID id = UUID.randomUUID();
        DirectoryV3DevicesListPostResponseDevice device = new DirectoryV3DevicesListPostResponseDevice(id, "name", "type", 1);
        DirectoryV3DevicesListPostResponseDevice other = new DirectoryV3DevicesListPostResponseDevice(id, "name", "type", 1);
        assertEquals(device.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeForUnequalObjectsIsNotEqual() throws Exception {
        DirectoryV3DevicesListPostResponseDevice device =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 1);
        DirectoryV3DevicesListPostResponseDevice other =
                new DirectoryV3DevicesListPostResponseDevice(UUID.randomUUID(), "name", "type", 1);
        assertNotEquals(device.hashCode(), other.hashCode());
    }

    @Test
    public void testObjectMapperCanMap() throws Exception {
        DirectoryV3DevicesListPostResponseDevice expected = new DirectoryV3DevicesListPostResponseDevice(
                UUID.fromString("b2623e85-f59b-4c33-9d11-be647c965ede"), "bob", "ios", 1);
        DirectoryV3DevicesListPostResponseDevice actual = new ObjectMapper().readValue(
                "{\"name\": \"bob\", \"id\": \"b2623e85-f59b-4c33-9d11-be647c965ede\", \"type\": \"ios\", \"status\": 1}",
                DirectoryV3DevicesListPostResponseDevice.class
        );
        assertEquals(expected, actual);
    }
}