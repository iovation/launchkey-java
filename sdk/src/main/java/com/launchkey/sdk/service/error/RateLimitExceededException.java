/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
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
 * Exception is thrown when the number of calls allowed for the request data has been exceeded for the time limit.
 *
 * @deprecated Replaced by {@link com.launchkey.sdk.error.RateLimitExceededException}
 */
@Deprecated
public class RateLimitExceededException extends ApiException {
    /**
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param code    HTTP status code or 0 if no HTTP status code was returned
     */
    public RateLimitExceededException(String message, int code) {
        super(message, code);
    }
}
