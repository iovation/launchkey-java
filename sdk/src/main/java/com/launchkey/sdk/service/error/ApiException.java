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

import com.launchkey.sdk.error.BaseException;

/**
 * Abstract exception class from which all errors in the SDK are derived
 *
 * @deprecated Replaced by {@link BaseException}.
 */
public class ApiException extends Exception {
    /**
     * Code associated with an exception
     */
    private final int code;

    /**
     * Get the proper exception for the provided message code and message
     *
     * @param  code Error code or 0 if no HTTP status code was returned
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @return Exception that properly correlates with the code
     */
    public static ApiException fromCode(int code, String message) {
        ApiException error;

        switch (code) {
            case 40421:
            case 40433:
            case 40434:
            case 40437:
            case 50441:
            case 50446:
            case 50454:
            case 50455:
            case 50456:
            case 50457:
            case 60401:
            case 70401:
            case 70402:
                error = new InvalidRequestException(message, code);
                break;
            case 40422:
            case 40423:
            case 40425:
            case 40429:
            case 40435:
            case 50442:
            case 50443:
            case 50444:
            case 50445:
            case 50447:
            case 50449:
                error = new InvalidCredentialsException(message, code);
                break;
            case 40424:
                error = new NoPairedDevicesException(message, code);
                break;
            case 40426:
                error = new NoSuchUserException(message, code);
                break;
            case 40428:
            case 40432:
            case 50448:
            case 50452:
            case 50453:
                error = new InvalidSignatureException(message, code);
                break;
            case 40436:
                error = new RateLimitExceededException(message, code);
                break;
            case 40431:
            case 50451:
            case 70404:
                error = new ExpiredAuthRequestException(message, code);
                break;
            default:
                error = new ApiException(message, code);
        }
        return error;
    }

    public ApiException() {
        this(null, null, 0);
    }

    /**
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  code HTTP status code or 0 if no HTTP status code was returned
     */
    public ApiException(String message, int code) {
        this(message, null, code);
    }

    /**
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param  code HTTP status code or 0 if no HTTP status code was returned
     */
    public ApiException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Get the code associated with this exception
     * @return Code associated with this exception
     */
    public int getCode() {
        return code;
    }
}
