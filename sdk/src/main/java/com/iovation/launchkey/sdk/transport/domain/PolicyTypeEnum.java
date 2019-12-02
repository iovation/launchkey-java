package com.iovation.launchkey.sdk.transport.domain;

public enum PolicyTypeEnum {
    METHOD_AMOUNT, FACTORS, COND_GEO, LEGACY;

    public String toString() {
        switch (this) {
            case LEGACY:
                return "LEGACY";
            case FACTORS:
                return "FACTORS";
            case COND_GEO:
                return "COND_GEO";
            case METHOD_AMOUNT:
                return "METHOD_AMOUNT";
            default:
                throw new IllegalStateException("PolicyType enum in toString does not have a switch case");
        }
    }
}
