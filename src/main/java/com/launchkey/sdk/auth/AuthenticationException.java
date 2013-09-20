package com.launchkey.sdk.auth;

public class AuthenticationException extends Exception {

    public AuthenticationException(String message, String code) {
        super(message + ". Error code: " + code);
    }
}
