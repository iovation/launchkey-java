package com.iovation.launchkey.sdk.integration.mobile.driver.android;

import com.iovation.launchkey.sdk.integration.mobile.driver.SampleAppMobileDriver;
import org.openqa.selenium.Capabilities;

import java.net.URL;

public class SampleAppAndroidDriver extends BaseAndroidDriver implements SampleAppMobileDriver {

    private boolean firstAlertDismissed = false;

    public SampleAppAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    public void resetTheApp() {
        super.resetTheApp();
        firstAlertDismissed = false;
    }

    private void approveAlertOnAppLaunch() {
        if (!firstAlertDismissed) {
            firstAlertDismissed = true;
            pressElementWithName("OK");
        }
    }

    @Override
    public void unlinkDevice() {
        scrollToAndPressElementWithName("Unlink 2 (Custom UI)");
    }

    private void openLinkingMenu() {
        scrollToAndPressElementWithName("Link (Custom UI - Manual)");
    }

    private void fillLinkingCode(String linkingCode) {
        findElementWithName("Linking code").sendKeys(linkingCode);
    }

    private void fillAuthenticatorSDKKey(String sdkKey) {
        findElementWithName("Auth SDK Key").sendKeys(sdkKey);
    }

    private void typeInDeviceName(String deviceName) {
        pressElementWithName("Use custom device name");

        if (deviceName != null && !deviceName.isEmpty()) {
            findElementById("demo_link_edit_name").sendKeys(deviceName);
        }
    }

    private void submitLinkingForm() {
        findElementById("demo_link_button").click();
    }

    @Override
    public void linkDevice(String sdkKey, String linkingCode) {
        linkDevice(sdkKey, linkingCode, null);
    }

    @Override
    public void linkDevice(String sdkKey, String linkingCode, String deviceName) {
        approveAlertOnAppLaunch();
        try {
            findElementWithName("Auth SDK Demo (Device is Unlinked)");
        } catch (Throwable t) {
            resetTheApp();
            approveAlertOnAppLaunch();
        }
        openLinkingMenu();
        fillLinkingCode(linkingCode);
        fillAuthenticatorSDKKey(sdkKey);
        typeInDeviceName(deviceName);
        submitLinkingForm();
        waitUntilLinkingIsFinished();
    }

    private void openAuthMenu() {
        scrollToAndPressElementWithName("Check for Requests (XML)");
    }

    private void tapRefresh() {
        findElementById("menu_refresh").click();
    }

    private void approveAuth() {
        longPressThisElementInItsCenterMillis(findElementById("auth_info_action_positive"), 1500);
    }

    private void denyAuth() {
        longPressThisElementInItsCenterMillis(findElementById("auth_info_action_negative"), 1500);
        pressElementWithName("I don't approve");
        longPressThisElementInItsCenterMillis(findElementById("auth_do_action_negative"), 1500);
    }

    @Override
    public void approveRequest() {
        openAuthMenu();
        tapRefresh();
        approveAuth();
    }

    @Override
    public void denyRequest() {
        openAuthMenu();
        tapRefresh();
        denyAuth();
    }

    private void dismissFailureMessage() {
        pressElementWithName("DISMISS");
    }

    @Override
    public void receiveAndAcknowledgeAuthFailure() {
        openAuthMenu();
        tapRefresh();
        dismissFailureMessage();
    }


    @Override
    public void waitUntilLinkingIsFinished()  {
        waitForViewWithIdToDisappear("android.R.id.progress", 20);
    }

}

