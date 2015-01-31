package com.launchkey.sdk.auth;

/**
 * Copyright (C) 2015 LaunchKey, Inc. All Rights Reserved.
 *
 * @author Adam Englander <adam@launchkey.com>
 */
public class UserCreationException extends Exception {
    private final String code;

    public UserCreationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public UserCreationException(String message, Throwable throwable, String code) {
        super(message, throwable);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
