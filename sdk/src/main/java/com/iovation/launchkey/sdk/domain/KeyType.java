package com.iovation.launchkey.sdk.domain;

public enum KeyType {
    BOTH(0),
    ENCRYPTION(1),
    SIGNATURE(2),
    OTHER(-1);

    private final int v;

    KeyType(int v) {
        this.v = v;
    }

    public int value() {
        return v;
    }
}
