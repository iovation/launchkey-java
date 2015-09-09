/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
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

/**
 * Interface defining the contract for storing and retrieving cached ping responses
 */
public interface PingResponseCache {
    /**
     * Set the ping response in the cache
     * @param pingResponse
     * @throws CachePersistenceException When an error occurred saving the cached response in a persistence engine
     */
    void setPingResponse(PingResponse pingResponse) throws CachePersistenceException;

    /**
     * Get the cached ping response.  If no valid response is available, null will be returned
     * @return The cached ping response or null if no response is cached
     * @throws CachePersistenceException When an error occurred fetching the cached response from a persistence engine
     */
    PingResponse getPingResponse() throws CachePersistenceException;
}
