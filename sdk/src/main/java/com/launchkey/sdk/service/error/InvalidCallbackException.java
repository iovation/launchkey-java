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
 * Exception thrown when the data passed to a Server Sent Event handler does not contain the data that will allow
 * for processing of any of the known callback methods.
 *
 * @deprecated Replaced by {@link com.launchkey.sdk.error.InvalidCallbackException}
 */
@Deprecated
public class InvalidCallbackException extends ApiException {
    /**
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public InvalidCallbackException(String message) {
        super(message, 0);
    }

    /**
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     */
    public InvalidCallbackException(String message, Throwable cause) {
        super(message, cause, 0);
    }
}
