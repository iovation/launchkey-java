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
import com.iovation.launchkey.sdk.domain.KeyType;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeysListPostResponsePublicKeyTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getId() throws Exception {
        assertEquals("ID", new KeysListPostResponsePublicKey("ID", null, null, null, false, 0).getId());
    }

    @Test
    public void getPublicKey() throws Exception {
        assertEquals("key", new KeysListPostResponsePublicKey(null, "key", null, null, false, 0).getPublicKey());
    }

    @Test
    public void getCreated() throws Exception {
        Date expected = new Date();
        assertEquals(expected, new KeysListPostResponsePublicKey(null, null, expected, null, false, 0).getCreated());
    }

    @Test
    public void getExpires() throws Exception {
        Date expected = new Date();
        assertEquals(expected, new KeysListPostResponsePublicKey(null, null, null, expected, false, 0).getExpires());
    }

    @Test
    public void isActive() throws Exception {
        assertTrue(new KeysListPostResponsePublicKey(null, null, null, null, true, 0).isActive());
    }

    @Test
    public void getKeyType() throws Exception {
        KeyType expecting_both = KeyType.BOTH;
        KeyType expecting_encryption = KeyType.ENCRYPTION;
        KeyType expecting_signature = KeyType.SIGNATURE;

        assertEquals(expecting_both, new KeysListPostResponsePublicKey(null, null, null, null, true, 0).getKeyType());
        assertEquals(expecting_encryption, new KeysListPostResponsePublicKey(null, null, null, null, true, 1).getKeyType());
        assertEquals(expecting_signature, new KeysListPostResponsePublicKey(null, null, null, null, true, 2).getKeyType());
    }

    @Test
    public void getKeyTypeReturnsBothWhenReceivingUnknownKeyType() throws Exception {
        KeyType expected = KeyType.BOTH;
        assertEquals(expected, new KeysListPostResponsePublicKey(null, null, null, null, true, 3).getKeyType());
    }

    @Test
    public void fromJsonGetsProperId() throws Exception {
        assertEquals("Expected ID",
                objectMapper.readValue("{\"id\": \"Expected ID\"}", KeysListPostResponsePublicKey.class).getId());
    }

    @Test
    public void fromJsonGetsProperPublicKey() throws Exception {
        assertEquals("Expected PK",
                objectMapper.readValue("{\"public_key\": \"Expected PK\"}", KeysListPostResponsePublicKey.class)
                        .getPublicKey());
    }

    @Test
    public void fromJsonGetsProperDateCreated() throws Exception {
        Date expected = new Date(946684800000L);
        assertEquals(expected, objectMapper
                .readValue("{\"date_created\": \"2000-01-01T00:00:00Z\"}", KeysListPostResponsePublicKey.class)
                .getCreated());
    }

    @Test
    public void fromJsonGetsProperExpirationDate() throws Exception {
        Date expected = new Date(946684800000L);
        assertEquals(expected, objectMapper
                .readValue("{\"date_expires\": \"2000-01-01T00:00:00Z\"}", KeysListPostResponsePublicKey.class)
                .getExpires());
    }

    @Test
    public void fromJsonGetsProperActive() throws Exception {
        assertTrue(objectMapper.readValue("{\"active\": true}", KeysListPostResponsePublicKey.class).isActive());
    }
}