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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class EntityIdentifierTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void getToStringWithServiceEntity() throws Exception {
        String expected = "svc:319d2db1-3965-4f2e-89a0-26572ddbf31d";
        String actual = new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d")).toString();
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void getToStringWithDirectoryEntity() throws Exception {
        String expected = "dir:319d2db1-3965-4f2e-89a0-26572ddbf31d";
        String actual = new EntityIdentifier(EntityIdentifier.EntityType.DIRECTORY, UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d")).toString();
        assertEquals(expected, actual);
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void getToStringWithOrganizationEntity() throws Exception {
        String expected = "org:319d2db1-3965-4f2e-89a0-26572ddbf31d";
        String actual = new EntityIdentifier(EntityIdentifier.EntityType.ORGANIZATION, UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d")).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void constructorWithNullEntityThrowsIllegalArgumentException() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        new EntityIdentifier(null, UUID.randomUUID()).toString();
    }

    @Test
    public void constructorWithNullIdThrowsIllegalArgumentException() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        new EntityIdentifier(EntityIdentifier.EntityType.SERVICE, null).toString();
    }
}