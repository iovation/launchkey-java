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
 * Exception thrown when the data passed to a Server Sent Event handler does not contain the data that will allow
 * for processing of any of the known callback methods.
 */
public class InvalidCallbackException extends LaunchKeyException {
    /**
     * @see LaunchKeyException#LaunchKeyException(String, int)
     */
    public InvalidCallbackException(String message) {
        super(message, 0);
    }

    /**
     * @see LaunchKeyException#LaunchKeyException(String, Throwable, int)
     */
    public InvalidCallbackException(String message, Throwable cause) {
        super(message, cause, 0);
    }
}
