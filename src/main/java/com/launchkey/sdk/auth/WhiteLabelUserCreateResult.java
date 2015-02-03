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
     *  The value to store in order to push future requests to this user
     */
    private String launchKeyIdentifier;

    /**
     * Code for the user to type into their device for manual verification if they are unable to scan the QR Code
     */
    private String code;

    /**
     * @param qrCodeUrl The URL to a QR Code for the device to scan
     * @param launchKeyIdentifier The value to store in order to push future requests to this user
     * @param code Code for the user to type into their device for manual verification if they are unable to scan the
     *             QR Code
     */
    public WhiteLabelUserCreateResult(String qrCodeUrl, String launchKeyIdentifier, String code) {
        this.qrCodeUrl = qrCodeUrl;
        this.launchKeyIdentifier = launchKeyIdentifier;
        this.code = code;
    }

    /**
     * @return The URL to a QR Code for the device to scan
     */
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    /**
     * @return The value to store in order to push future requests to this user
     */
    public String getLaunchKeyIdentifier() {
        return launchKeyIdentifier;
    }

    /**
     * @return Code for the user to type into their device for manual verification if they are unable to scan the QR Code
     */
    public String getCode() {
        return code;
    }
}
