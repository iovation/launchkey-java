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

package com.launchkey.sdk.service.error;

/**
 * Exception thrown when the response returned by the LaunchKey Engine API is not parseable or does not contain the
 * expected response data elements for the request that was made.
 */
public class InvalidResponseException extends LaunchKeyException {
    /**
     * @see LaunchKeyException#LaunchKeyException(String, int)
     */
    public InvalidResponseException(String message, int code) {
        super(message, code);
    }
    /**
     * @see LaunchKeyException#LaunchKeyException(String, Throwable, int)
     */
    public InvalidResponseException(String message, Throwable cause) {
        super(message, cause, 0);
    }
}
