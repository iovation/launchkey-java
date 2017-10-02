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

import java.util.List;

public class KeysListPostResponse {
    private final List<KeysListPostResponsePublicKey> publicKeys;

    public KeysListPostResponse(
            List<KeysListPostResponsePublicKey> publicKeys) {
        this.publicKeys = publicKeys;
    }

    public List<KeysListPostResponsePublicKey> getPublicKeys() {
        return publicKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeysListPostResponse)) return false;

        KeysListPostResponse that = (KeysListPostResponse) o;

        return publicKeys != null ? publicKeys.equals(that.publicKeys) : that.publicKeys == null;
    }

    @Override
    public String toString() {
        return "KeysListPostResponse{" +
                "publicKeys=" + publicKeys +
                '}';
    }
}
