package com.iovation.launchkey.sdk.error;

/**
 * Exception for an action which requires an authorization request to be active but is has been cancelled
 */
public class AuthorizationRequestCanceled extends InvalidRequestException {
    /**
     * @param message   the detail message (which is saved for later retrieval
     *                  by the {@link #getMessage()} method).
     * @param cause     the cause (which is saved for later retrieval by the
     *                  {@link #getCause()} method).  (A <tt>null</tt> value is
     *                  permitted, and indicates that the cause is nonexistent or
     *                  unknown.)
     * @param errorCode The error code received from the Platform API. It will be null if the error was not receive
     */
    public AuthorizationRequestCanceled(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
