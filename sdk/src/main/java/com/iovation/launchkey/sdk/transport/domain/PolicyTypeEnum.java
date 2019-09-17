package com.iovation.launchkey.sdk.transport.domain;

public enum PolicyTypeEnum {
    METHOD_AMOUNT, FACTORS, COND_GEO, LEGACY, TERRITORY, GEO_CIRCLE;

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
            case TERRITORY:
                return "TERRITORY";
            case GEO_CIRCLE:
                return "GEO_CIRCLE";
            default:
                throw new IllegalStateException("PolicyType enum in toString does not have a switch case");
        }
    }
}
