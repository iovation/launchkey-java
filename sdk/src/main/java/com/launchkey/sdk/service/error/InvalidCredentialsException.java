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
 * Exception for errors that occur due to API credentials being invalid when communicating with the LaunchKey Engine
 * API.
 */
public class InvalidCredentialsException extends LaunchKeyException {
    /**
     * @see LaunchKeyException#LaunchKeyException(String, int)
     */
    public InvalidCredentialsException(String message, int code) {
        super(message, code);
    }
}