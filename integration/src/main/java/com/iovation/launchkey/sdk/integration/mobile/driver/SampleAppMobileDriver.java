package com.iovation.launchkey.sdk.integration.mobile.driver;

public interface SampleAppMobileDriver {
    void unlinkDevice();
    void linkDevice(String sdkKey, String linkingCode);
    void linkDevice(String sdkKey, String linkingCode, String deviceName);
    void approveRequest();
    void denyRequest();
    void receiveAndAcknowledgeAuthFailure();
    void waitUntilLinkingIsFinished();
}
