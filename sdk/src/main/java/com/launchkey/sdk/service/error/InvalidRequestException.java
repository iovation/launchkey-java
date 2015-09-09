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
 * This exception is thrown when the data sent to the API was not the correct data for the endpoint.
 */
public class InvalidRequestException extends LaunchKeyException {
    /**
     * @see LaunchKeyException#LaunchKeyException(String, int)
     */
    public InvalidRequestException(String message, int code) {
        super(message, code);
    }
}
