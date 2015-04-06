package com.launchkey.sdk.auth;

/**
 * Copyright (C) 2015 LaunchKey, Inc. All Rights Reserved.
 *
 * @author Adam Englander <adam@launchkey.com>
 */
public class WhiteLabelUserCreateResult {
    /**
     *  The URL to a QR Code for the device to scan
     */
    private String qrCodeUrl;

    /**
     * Code for the user to type into their device for manual verification if they are unable to scan the QR Code
     */
    private String code;

    /**
     * @param qrCodeUrl The URL to a QR Code for the device to scan
     * @param code Code for the user to type into their device for manual verification if they are unable to scan the
     *             QR Code
     */
    public WhiteLabelUserCreateResult(String qrCodeUrl, String code) {
        this.qrCodeUrl = qrCodeUrl;
        this.code = code;
    }

    /**
     * @return The URL to a QR Code for the device to scan
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * @return Code for the user to type into their device for manual verification if they are unable to scan the QR Code
     */
    public String getCode() {
        return code;
    }
}
