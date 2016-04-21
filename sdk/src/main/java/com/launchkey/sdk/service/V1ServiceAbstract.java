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

package com.launchkey.sdk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.cache.CachePersistenceException;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.LaunchKeyDateFormat;
import com.launchkey.sdk.transport.v1.domain.PingRequest;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

/**
 * Abstract V1 based service providing shared functionality between auth and whitelabel services
 */
public abstract class V1ServiceAbstract {
    protected final Transport transport;
    protected final Crypto crypto;
    protected final PingResponseCache pingResponseCache;
    protected final long appKey;
    protected final String secretKey;
    protected final Base64 base64 = new Base64(0);
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final Log log;
    protected final LaunchKeyDateFormat launchKeyDateFormat = new LaunchKeyDateFormat();

    /**
     * @param transport Transport service
     * @param crypto Crypto service
     * @param pingResponseCache Ping response cache service
     * @param appKey Application key for the requests
     * @param secretKey Secret key for the requests
     */
    public V1ServiceAbstract(
            Transport transport,
            Crypto crypto,
            PingResponseCache pingResponseCache,
            long appKey,
            String secretKey
    ) {
        this.transport = transport;
        this.secretKey = secretKey;
        this.pingResponseCache = pingResponseCache;
        this.appKey = appKey;
        this.crypto = crypto;
        this.log = LogFactory.getLog(getClass());
    }

    protected RSAPublicKey getLaunchKeyPublicKey() throws ApiException {
        PingResponse pingResponse = null;
        try {
            pingResponse = pingResponseCache.getPingResponse();
        } catch (CachePersistenceException e) {
            log.error("Error getting ping response from cache", e);
        }
        if (pingResponse == null) {
            pingResponse = transport.ping(new PingRequest());
            try {
                pingResponseCache.setPingResponse(pingResponse);
            } catch (CachePersistenceException e) {
                log.error("Error placing ping response in cache", e);
            }
        }
        return crypto.getRSAPublicKeyFromPEM(pingResponse.getPublicKey());
    }

    protected byte[] getSecret() throws ApiException {

        try {
            String json = objectMapper.writeValueAsString(new Object() {
                public final String secret = secretKey;
                public final String stamped = launchKeyDateFormat.format(new Date());
            });
            return crypto.encryptRSA(json.getBytes(), getLaunchKeyPublicKey());
        } catch (JsonProcessingException e) {
           throw new ApiException("Unable to create JSON from secret key", e, 0);
        }
    }
}
