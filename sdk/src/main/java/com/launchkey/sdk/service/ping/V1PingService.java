/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.service.ping;

import com.launchkey.sdk.cache.CacheException;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.error.*;
import com.launchkey.sdk.error.CommunicationErrorException;
import com.launchkey.sdk.error.InvalidResponseException;
import com.launchkey.sdk.service.error.*;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.PingRequest;
import com.launchkey.sdk.transport.v1.domain.PingResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class V1PingService implements PingService {
    private final Log log = LogFactory.getLog(getClass());
    private final PingResponseCache pingResponseCache;
    private final Transport transport;
    private final Crypto crypto;
    private Long timeOffset;

    public V1PingService(PingResponseCache pingResponseCache, Transport transport, Crypto crypto) {
        this.pingResponseCache = pingResponseCache;
        this.transport = transport;
        this.crypto = crypto;
    }

    @Override public RSAPublicKey getPublicKey() throws CommunicationErrorException, InvalidResponseException, InvalidStateException, PlatformErrorException {
        PingResponse pingResponse;
        try {
            pingResponse = pingResponseCache.getPingResponse();
        } catch (CacheException e) {
            log.trace("An error occurred retrieving the ping response from cache", e);
            pingResponse = null;
        }
        if (pingResponse == null) {
            pingResponse = getPingResponse();
        }

        RSAPublicKey response = crypto.getRSAPublicKeyFromPEM(pingResponse.getPublicKey());
        return response;
    }

    @Override public Date getPlatformTime() throws CommunicationErrorException, InvalidResponseException, InvalidStateException, PlatformErrorException {
        Date platformTime = new Date(System.currentTimeMillis() + getTimeOffset());
        return platformTime;
    }

    private long getTimeOffset() throws PlatformErrorException, InvalidResponseException, CommunicationErrorException {
        if (timeOffset == null) {
                PingResponse pingResponse = getPingResponse();
                Date now = new Date();
                timeOffset = pingResponse.getApiTime().getTime() - now.getTime();
        }
        return timeOffset;
    }

    private PingResponse getPingResponse() throws CommunicationErrorException, InvalidResponseException, PlatformErrorException {
        PingResponse pingResponse;

        try {
            pingResponse = transport.ping(new PingRequest());
        } catch (com.launchkey.sdk.service.error.CommunicationErrorException e) {
            throw new CommunicationErrorException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } catch (com.launchkey.sdk.service.error.InvalidResponseException e) {
            throw new InvalidResponseException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        } catch (ApiException e) {
            throw new PlatformErrorException(e.getMessage(), e.getCause(), String.valueOf(e.getCode()));
        }

        try {
            pingResponseCache.setPingResponse(pingResponse);
        } catch (CacheException e) {
            log.trace("An error occurred persisting the ping response in cache", e);
        }
        return pingResponse;
    }
}
