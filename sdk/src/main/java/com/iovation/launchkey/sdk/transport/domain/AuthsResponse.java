package com.iovation.launchkey.sdk.transport.domain;

import java.util.UUID;

/**
 * Interface for objects providing authorization response elements
 */
public interface AuthsResponse {
    EntityIdentifier getRequestingEntity();

    UUID getServiceId();

    String getOrganizationUserHash();

    String getServiceUserHash();

    String getUserPushId();

    UUID getAuthorizationRequestId();

    boolean getResponse();

    String getDeviceId();

    String[] getServicePins();

    String getType();

    String getReason();

    String getDenialReason();

    AuthResponsePolicy getAuthPolicy();

    AuthMethod[] getAuthMethods();
}
