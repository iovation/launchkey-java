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

package com.iovation.launchkey.sdk.error;

/**
 * This exception is thrown when an error occurs communicating with the Platform API.  In this error the
 * code and message will be re-purposed to the following:
 *      code: HTTP status code or 0 if no HTTP status code was returned
 *      message: Message from the underlying transport (HTTP, TLS, TCP, IP, etc)
 */
public class CommunicationErrorException extends BaseException {

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
    public CommunicationErrorException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
