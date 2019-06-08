package com.iovation.launchkey.sdk.example.springmvc.model;

public class LinkingData {
    private final String deviceId;
    private final String linkingCode;
    private final String qrCode;

    public LinkingData(String deviceId, String linkingCode, String qrCode) {
        this.deviceId = deviceId;
        this.linkingCode = linkingCode;
        this.qrCode = qrCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getLinkingCode() {
        return linkingCode;
    }

    public String getQrCode() {
        return qrCode;
    }
}
