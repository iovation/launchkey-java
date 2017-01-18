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

package com.launchkey.sdk.transport.domain;

import java.security.PublicKey;

public class PublicPublicKeyGetResponse {
    public final PublicKey publicKey;

    public final String publicKeyFingerprint;

    public PublicPublicKeyGetResponse(PublicKey publicKey, String publicKeyFingerprint) {
        this.publicKey = publicKey;
        this.publicKeyFingerprint = publicKeyFingerprint;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyFingerprint() {
        return publicKeyFingerprint;
    }
}
