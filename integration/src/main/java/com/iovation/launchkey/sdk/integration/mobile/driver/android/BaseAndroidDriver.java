package com.iovation.launchkey.sdk.integration.mobile.driver.android;

import io.appium.java_client.MobileElement;
import io.appium.java_client.Setting;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

public class BaseAndroidDriver extends AndroidDriver<AndroidElement> {

    protected boolean cameraPermissionAccepted = false;
    protected boolean locationPermissionAccepted = false;

    public BaseAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);

        setSetting(Setting.IGNORE_UNIMPORTANT_VIEWS, true);
        setSetting(Setting.WAIT_FOR_IDLE_TIMEOUT, 0);
        setSetting(Setting.WAIT_FOR_SELECTOR_TIMEOUT, 1000);
    }

    public void acceptCameraPermission() {
        if (!isCameraPermissionAccepted()) {
            acceptPermission();
            cameraPermissionIsAccepted();
        }
    }

    public void acceptLocationPermission() {
        if (!isLocationPermissionAccepted()) {
            acceptPermission();
            locationPermissionIsAccepted();
        }
    }

    public void acceptPermission() {
        findElementById("com.android.packageinstaller:id/permission_allow_button")
                .click();

    }

    public void appWasReset() {
        cameraPermissionAccepted = false;
        locationPermissionAccepted = false;
    }

    public void cameraPermissionIsAccepted() {
        cameraPermissionAccepted = true;
    }

    public void denyPermission() {
        pressElementWithName("DENY");
    }

    public MobileElement findElementWithName(String name) {
        return findElementByAndroidUIAutomator("new UiSelector().textContains(\""+name+"\")");
    }

    public boolean isCameraPermissionAccepted() {
        return cameraPermissionAccepted;
    }

    public boolean isLocationPermissionAccepted() {
        return locationPermissionAccepted;
    }

    public void locationPermissionIsAccepted() {
        locationPermissionAccepted = true;
    }

    public void pressBack() {
        navigate().back();
    }

    public void pressMenu() {
        findElementByAndroidUIAutomator("new UiSelector().description(\"Navigate up\")").click();
    }

    public void pressElementWithName(String name) {
        findElementByAndroidUIAutomator("new UiSelector().textContains(\""+name+"\")").click();
    }

    public void resetTheApp() {
        resetApp();
        appWasReset();
        try {
            Thread.sleep(10000);
        } catch (Throwable t) {}
    }

    public void screenshot() throws Exception {
        File androidScreenshot = getScreenshotAs(OutputType.FILE);
        String screenShot1 = UUID.randomUUID().toString();
        BufferedImage img = ImageIO.read(androidScreenshot);
        File targetFill = new File("screenshot/android/"+ screenShot1 +".PNG");
        ImageIO.write(img, "PNG", targetFill);
    }

    public void scrollToElementWithName(String name) {
        findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+name+"\").instance(0))");
    }

    public void scrollToAndPressElementWithName(String name) {
        findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\""+name+"\").instance(0))").click();
    }

    public void waitToSeeElementWithName(String name) {
        findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + name + "\")");
    }

    protected void pressThisElementInItsCenter(MobileElement element) {
        PointOption point = new PointOption().withCoordinates(element.getCenter());

        TouchAction touch = new TouchAction(this);
        touch.press(point).release().perform();
    }

    protected void longPressThisElementInItsCenterMillis(MobileElement element, int millis) {
        PointOption point = new PointOption().withCoordinates(element.getCenter());

        Duration longPressLength = Duration.ofMillis(millis);

        LongPressOptions options = new LongPressOptions();
        options.withPosition(point);
        options.withDuration(longPressLength);

        TouchAction touch = new TouchAction(this);
        touch.longPress(options).release().perform();
    }

    protected void waitForViewWithIdToDisappear(String id, int maxWaitInSeconds) {
        WebDriverWait wait = new WebDriverWait(this, maxWaitInSeconds);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
    }

}
