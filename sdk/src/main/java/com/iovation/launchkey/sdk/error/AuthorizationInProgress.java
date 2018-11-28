package com.iovation.launchkey.sdk.error;

import java.util.Date;
import java.util.Objects;

public class AuthorizationInProgress extends InvalidRequestException {
    private final String authorizationRequestId;
    private final boolean myAuthorizationRequest;
    private final Date expires;

    /**
     * @param message                The detail message (which is saved for later retrieval
     *                               by the {@link #getMessage()} method).
     * @param cause                  the cause (which is saved for later retrieval by the
     *                               {@link #getCause()} method).  (A <tt>null</tt> value is
     *                               permitted, and indicates that the cause is nonexistent or
     *                               unknown.)
     * @param errorCode              HTTP status code or 0 if no HTTP status code was returned
     * @param authorizationRequestId Identifier of the existing Authorization Request that caused this exception
     * @param myAuthorizationRequest Is the authorization in progress from the same Service requesting the new
     *                               Authorization Request
     * @param expires                When the Authorization Request identified by authorizationRequestId will expire.
     */
    public AuthorizationInProgress(
            String message, Throwable cause, String errorCode, String authorizationRequestId,
            boolean myAuthorizationRequest, Date expires) {
        super(message, cause, errorCode);
        this.authorizationRequestId = authorizationRequestId;
        this.myAuthorizationRequest = myAuthorizationRequest;
        this.expires = expires;
    }

    /**
     * Get the identifier of the existing Authorization Request that caused this exception
     *
     * @return Identifier of the existing Authorization Request that caused this exception
     */
    public String getAuthorizationRequestId() {
        return authorizationRequestId;
    }

    /**
     * Is the authorization in progress from the same Service requesting the new Authorization Request
     *
     * @return Is the authorization in progress from the same Service requesting the new Authorization Request
     */
    public boolean isMyAuthorizationRequest() {
        return myAuthorizationRequest;
    }

    /**
     * Get when the Authorization Request identified by authorizationRequestId will expire.
     *
     * @return Expiration of the Authorization Request identified by authorizationRequestId
     */
    public Date getExpires() {
        return expires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorizationInProgress)) return false;
        if (!super.equals(o)) return false;
        AuthorizationInProgress that = (AuthorizationInProgress) o;
        return isMyAuthorizationRequest() == that.isMyAuthorizationRequest() &&
                Objects.equals(getAuthorizationRequestId(), that.getAuthorizationRequestId()) &&
                Objects.equals(getExpires(), that.getExpires());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAuthorizationRequestId(), isMyAuthorizationRequest(), getExpires());
    }

    @Override
    public String toString() {
        return "AuthorizationInProgress{" +
                "authorizationRequestId='" + authorizationRequestId + '\'' +
                ", myAuthorizationRequest=" + myAuthorizationRequest +
                ", expires=" + expires +
                "} " + super.toString();
    }
}
