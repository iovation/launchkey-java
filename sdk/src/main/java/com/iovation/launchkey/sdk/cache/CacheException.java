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

package com.iovation.launchkey.sdk.cache;

/**
 * Named exception specifically to be thrown when the cache implementation cannot read or write to its persistence
 * provider.
 */
public class CacheException extends Exception {
    /**
     * @param  message the detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public CacheException(String message) {
        super(message);
    }

    /**
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
