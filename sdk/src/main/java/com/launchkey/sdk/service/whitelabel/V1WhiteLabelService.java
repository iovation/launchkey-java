/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.whitelabel;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.V1ServiceAbstract;
import com.launchkey.sdk.service.error.InvalidResponseException;
import com.launchkey.sdk.service.error.LaunchKeyException;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.UsersRequest;
import com.launchkey.sdk.transport.v1.domain.UsersResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * White Label service based on an API V1 transport
 */
public class V1WhiteLabelService extends V1ServiceAbstract implements WhiteLabelService{

    /**
     * @param transport Transport service
     * @param crypto Crypto service
     * @param pingResponseCache Ping response cache
     * @param rocketKey Rocket key for this implementation
     * @param secretKey Secret key for this implementation
     */
    public V1WhiteLabelService(
            Transport transport, Crypto crypto, PingResponseCache pingResponseCache, long rocketKey, String secretKey
    ) {
       super(transport, crypto, pingResponseCache, rocketKey, secretKey);
    }

    @Override
    public PairResponse pairUser(String identifier) throws LaunchKeyException {
        UsersResponse usersResponse = transport.users(
                new UsersRequest(
                        identifier,
                        rocketKey,
                        base64.encodeAsString(getSecret())
                )
        );
        byte[] cipher = crypto.decryptRSA(base64.decode(usersResponse.getCipher()));
        byte[] key = new byte[32];
        byte[] iv = new byte[16];
        System.arraycopy(cipher, 0, key, 0, 32);
        System.arraycopy(cipher, 32, iv, 0, 16);

        try {
            byte[] data = crypto.decryptAES(base64.decode(usersResponse.getData()), key, iv);
            PairResponse response = objectMapper.readValue(data, PairResponse.class);
            return response;
        } catch (GeneralSecurityException e) {
            throw new InvalidResponseException("Unable to decrypt pair response", e);
        } catch (IOException e) {
            throw new InvalidResponseException("Unable to parse pair response data", e);
        }
    }
}
