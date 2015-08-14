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
 * Exception thrown when an auth request is made and the user exists but does not have a paired device with which
 * to respond.
 */
public class NoPairedDevicesException extends LaunchKeyException {
    /**
     * @see LaunchKeyException#LaunchKeyException(String, int)
     */
    public NoPairedDevicesException(String message, int code) {
        super(message, code);
    }
}
