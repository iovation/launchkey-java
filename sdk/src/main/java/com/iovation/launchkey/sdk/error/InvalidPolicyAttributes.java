package com.iovation.launchkey.sdk.error;

/**
 * Exception thrown when invalid attributes are set on a policy.
 */
public class InvalidPolicyAttributes extends BaseException {

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
    public InvalidPolicyAttributes(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
