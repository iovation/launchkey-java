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

package com.launchkey.sdk.cache;

import com.launchkey.sdk.transport.v1.domain.PingResponse;

import java.util.Date;

/**
 * Ping response cache that stores the ping response locally to the object
 */
public class LocalMemoryPingResponseCache implements PingResponseCache {

    private PingResponse pingResponse;

    private Date expiration;

    private final int ttlMillis;

    /**
     * @param ttlMillis Time To Live for cached ping responses in milliseconds
     */
    public LocalMemoryPingResponseCache(int ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    /**
     * @see PingResponseCache#setPingResponse(PingResponse)
     */
    public void setPingResponse(PingResponse pingResponse) {
        this.pingResponse = pingResponse;
        this.expiration = new Date(System.currentTimeMillis() + ttlMillis);
    }

    /**
     * @see PingResponseCache#getPingResponse()
     */
    public PingResponse getPingResponse() {
        PingResponse response = null;
        if (pingResponse != null && expiration != null && expiration.after(new Date())) {
            response = pingResponse;
        }
        return response;
    }
}
