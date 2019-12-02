package com.iovation.launchkey.sdk.error;

/**
 * Exception thrown when unknown fence type is received.
 */
public class UnknownFenceTypeException extends BaseException {

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
    public UnknownFenceTypeException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
