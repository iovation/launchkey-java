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

package com.launchkey.sdk.transport;

import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.transport.domain.PublicPingGetResponse;
import com.launchkey.sdk.transport.domain.PublicPublicKeyGetResponse;

public interface Transport {
    /**
     * Get the current API time as reported by the /public/v3/ping call.
     */
    PublicPingGetResponse publicPingGet() throws CommunicationErrorException, InvalidResponseException;

    /**
     * Get the public key information for the provided public key fingerprint. If the fingerprint is null, the current
     * public key fingerprint information will be returned
     *
     * @param publicKeyFingerprint Public key fingerprint of the public key for which you are requesting information.
     *                             If null, the current public key will be requested.
     */
    PublicPublicKeyGetResponse publicPublicKeyGet(String publicKeyFingerprint) throws CommunicationErrorException, InvalidResponseException;
}
