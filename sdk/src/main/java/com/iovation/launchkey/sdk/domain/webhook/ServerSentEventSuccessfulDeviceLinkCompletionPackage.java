/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.domain.webhook;

import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;

import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

/**
 * A Server Sent Event callback used to inform an application that the user has remotely ended their session.
 */
public class ServerSentEventSuccessfulDeviceLinkCompletionPackage implements WebhookPackage {
    /**
     * Unique user identifier that will match the user hash in {@link AuthorizationResponse#getServiceUserHash()}
     */
    private final UUID deviceId;

    /**
     * The date and time the remote logout was requested. This value is to be used to ensure the user has not started
     * a new session since the logout was request and inadvertently ending their application session.
     */
    private final String publicKeyId;

    /**
     * The date and time the remote logout was requested. This value is to be used to ensure the user has not started
     * a new session since the logout was request and inadvertently ending their application session.
     */
    private final String publicKey;

    /**
     *
     * @param deviceId The unique identifier of the Device which successfully completed the linking process.
     * @param publicKeyId The ID the Devices public key at the time of linking
     * @param publicKey The Devices public key at the time of linking
     */
    public ServerSentEventSuccessfulDeviceLinkCompletionPackage(
            UUID deviceId,
            String publicKeyId,
            String publicKey
    ) {

        this.deviceId = deviceId;
        this.publicKeyId = publicKeyId;
        this.publicKey = publicKey;
    }

    /**
     * Get the unique identifier of the Device which successfully completed the linking process.
     * @return The unique identifier of the Device which successfully completed the linking process.
     */
    public UUID getDeviceId() {
        return deviceId;
    }

    /**
     * Get the ID the Devices public key at the time of linking
     * @return The ID the Devices public key at the time of linking
     */
    public String getPublicKeyId() {
        return publicKeyId;
    }

    /**
     * Bet the Devices public key at the time of linking
     * @return The Devices public key at the time of linking
     */
    public String getPublicKey() {
        return publicKey;
    }
}
