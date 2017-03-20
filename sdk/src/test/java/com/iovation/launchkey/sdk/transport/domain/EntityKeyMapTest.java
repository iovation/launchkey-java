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

import com.iovation.launchkey.sdk.error.NoKeyFoundException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier.EntityType;

import java.security.interfaces.RSAPrivateKey;
import java.util.UUID;

public class EntityKeyMapTest {

    @Test(expected = NoKeyFoundException.class)
    public void getKeyThrowsNoKeyFoundWithNoKeys() throws Exception {
        assertNull(new EntityKeyMap().getKey(new EntityIdentifier(EntityType.DIRECTORY, UUID.randomUUID()), null));
    }

    @Test( expected = NoKeyFoundException.class)
    public void getKeyThrowsNoKeyFoundWhenWrongEntityIdentifierGiven() throws Exception {
        EntityKeyMap map = new EntityKeyMap();
        map.addKey(new EntityIdentifier(EntityType.DIRECTORY, UUID.randomUUID()), "bar", mock(RSAPrivateKey.class));
        assertNull(map.getKey(new EntityIdentifier(EntityType.DIRECTORY, UUID.randomUUID()), "bar"));
    }

    @Test(expected = NoKeyFoundException.class)
    public void getKeyThrowsNoKeyFoundWhenWrongPublicKeyFingerprintGiven() throws Exception {
        EntityKeyMap map = new EntityKeyMap();
        UUID uuid = UUID.randomUUID();
        map.addKey(new EntityIdentifier(EntityType.DIRECTORY,uuid ), "bar", mock(RSAPrivateKey.class));
        assertNull(map.getKey(new EntityIdentifier(EntityType.DIRECTORY, uuid), "baz"));
    }

    @Test
    public void getKeyReturnsExpectedKeyWhenProperEntityIdentifierAndPublicKeyFingerprintGiven() throws Exception {
        RSAPrivateKey expected = mock(RSAPrivateKey.class);
        EntityKeyMap map = new EntityKeyMap();
        UUID uuid = UUID.randomUUID();
        map.addKey(new EntityIdentifier(EntityType.DIRECTORY,uuid ), "bar", expected);
        assertEquals(expected, map.getKey(new EntityIdentifier(EntityType.DIRECTORY, uuid), "bar"));
    }
}