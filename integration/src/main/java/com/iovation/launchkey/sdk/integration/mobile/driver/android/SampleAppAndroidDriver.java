package com.iovation.launchkey.sdk.integration.mobile.driver.android;

import com.iovation.launchkey.sdk.integration.mobile.driver.SampleAppMobileDriver;
import io.appium.java_client.android.AndroidElement;
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

    @Override
    public void unlinkDevice() {
        scrollToAndPressElementWithName("Unlink 2 (Custom UI)");
    }

    private void openLinkingMenu() {
        scrollToAndPressElementWithName("Link (Default UI - Manual)");
    }

    private void fillLinkingCode(String linkingCode) {
        findElementWithName("ABCD123").sendKeys(linkingCode);
    }

    private void fillAuthenticatorSDKKey(String sdkKey) {
        AndroidElement sdk_key_input = findElementById("configs_sdk_key");
        sdk_key_input.click();
        sdk_key_input.clear();
        sdk_key_input.sendKeys(sdkKey);
        this.pressBack();
        scrollToElementWithName("RE-INITIALIZE");
        pressElementWithName("RE-INITIALIZE");
    }

    private void typeInDeviceName(String deviceName) {
        if (deviceName != null && !deviceName.isEmpty()) {
            findElementById("demo_link_edit_name").sendKeys(deviceName);
        }
    }

    private void submitLinkingForm() {
        findElementWithName("OK").click();
    }

    @Override
    public void linkDevice(String sdkKey, String linkingCode) {
        linkDevice(sdkKey, linkingCode, null);
    }

    public void setSDKKey(String sdkKey){
        scrollToElementWithName("Config Testing");
        findElementWithName("Config Testing").click();
        fillAuthenticatorSDKKey(sdkKey);
    }

    @Override
    public void linkDevice(String sdkKey, String linkingCode, String deviceName) {
        try {
            findElementWithName("Auth SDK Demo (Device is Unlinked)");
        } catch (Throwable t) {
            resetTheApp();
        }

        setSDKKey(sdkKey);
        openLinkingMenu();
        fillLinkingCode(linkingCode);
        findElementById("pair_entercode_button_done").click();
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

        // Wait for the view to animate into focus
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        approveAuth();
        dismissFailureMessage();
    }


    @Override
    public void waitUntilLinkingIsFinished()  {
        waitForViewWithIdToDisappear("android.R.id.progress", 20);
    }

}

