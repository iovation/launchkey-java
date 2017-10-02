/**
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

package com.iovation.launchkey.sdk.transport.domain;

import com.iovation.launchkey.sdk.error.NoKeyFoundException;

import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.ConcurrentHashMap;

public class EntityKeyMap {
    private final ConcurrentHashMap<EntityIdentifier, ConcurrentHashMap<String, RSAPrivateKey>> store;

    public EntityKeyMap() {
        store = new ConcurrentHashMap<>();
    }

    public void addKey(EntityIdentifier entityIdentifier, String publicKeyFingerprint, RSAPrivateKey privateKey) {
        ConcurrentHashMap<String, RSAPrivateKey> entityKeys;
        if (!store.containsKey(entityIdentifier)) {
            entityKeys = new ConcurrentHashMap<>();
            store.put(entityIdentifier, entityKeys);
        } else {
            entityKeys = store.get(entityIdentifier);
        }
        entityKeys.put(publicKeyFingerprint, privateKey);
    }

    public RSAPrivateKey getKey(EntityIdentifier entityIdentifier, String publicKeyFingerprint) throws NoKeyFoundException {
        RSAPrivateKey privateKey;
        if (store.containsKey(entityIdentifier)) {
            privateKey = store.get(entityIdentifier).get(publicKeyFingerprint);
        } else {
            privateKey = null;
        }
        if (privateKey == null) {
            throw new NoKeyFoundException(
                    "Np key found for entity " + entityIdentifier.toString() + " and key ID " + publicKeyFingerprint);

        }
        return privateKey;
    }
}
