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

package com.launchkey.sdk.cache;

import java.util.concurrent.ConcurrentHashMap;

public class HashCache implements Cache {
    private final ConcurrentHashMap<String, String> store;

    public HashCache() {
        store = new ConcurrentHashMap<String, String>();
    }

    @Override
    public String get(String key) throws CacheException {
        try {
            return store.get(key);
        } catch (Exception e) {
            throw new CacheException("Cache error on get!", e);
        }
    }

    @Override
    public void put(String key, String value) throws CacheException {
        try {
            store.put(key, value);
        } catch (Exception e) {
            throw new CacheException("Cache error on put!", e);
        }
    }
}
