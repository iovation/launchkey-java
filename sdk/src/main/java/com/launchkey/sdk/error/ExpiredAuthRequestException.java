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

package com.launchkey.sdk.error;

/**
 * Exception thrown when the service request with which you attempted to interact is no longer active. This can be due to
 * the user not responding to the getServiceService request and it has expired, the user has denied the getServiceService request, or the user
 * had approved the service request and then ended the session.
 */
public class ExpiredAuthRequestException extends BaseException {

    /**
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @param  errorCode The error code received from the Platform API. It will be null if the error was not receive
     *                   from the Platform API.
     */
    public ExpiredAuthRequestException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
