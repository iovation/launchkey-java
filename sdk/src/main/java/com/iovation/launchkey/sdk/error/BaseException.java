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
 * Abstract exception class from which all errors in the SDK are derived
 */
public class BaseException extends Exception {

    /**
     * Error Code associated with an exception
     */
    private final String errorCode;


    /**
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @param  errorCode HTTP status code or 0 if no HTTP status code was returned
     */
    public BaseException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Get the code associated with this exception
     * @return Code associated with this exception
     */
    public String getErrorCode() {
        return errorCode;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseException)) return false;

        BaseException that = (BaseException) o;

        if (getMessage() != null ? !getMessage().equals(that.getMessage()) : that.getMessage() != null) return false;
        if (getCause() != null ? !getCause().equals(that.getCause()) : that.getCause() != null) return false;
        return (getErrorCode() != null ? getErrorCode().equals(that.getErrorCode()) : that.getErrorCode() == null);

    }

    @Override public int hashCode() {
        int hashCode = 31 * (getMessage() != null ? getMessage().hashCode() : 0);
        hashCode += 31 * (getCause() != null ? getCause().hashCode() : 0);
        hashCode += 31 * (getErrorCode() != null ? getErrorCode().hashCode() : 0);
        return hashCode;
    }

    @Override public String toString() {
        return getClass().getSimpleName() + "{" +
                "message='" + getMessage() + "'," +
                "cause='" + getCause() + "'," +
                "errorCode='" + getErrorCode() + "'," +
                "}";
    }
}
