package com.iovation.launchkey.sdk.integration.mobile.driver;

public class NullMobileDriver implements SampleAppMobileDriver {
    @Override
    public void unlinkDevice() {

    }

    @Override
    public void linkDevice(String sdkKey, String linkingCode) {

    }

    @Override
    public void linkDevice(String sdkKey, String linkingCode, String deviceName) {

    }

    @Override
    public void approveRequest() {

    }

    @Override
    public void denyRequest() {

    }

    @Override
    public void receiveAndAcknowledgeAuthFailure() {

    }

    @Override
    public void waitUntilLinkingIsFinished() {

    }
}
