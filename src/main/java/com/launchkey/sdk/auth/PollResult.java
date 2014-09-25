package com.launchkey.sdk.auth;

public class PollResult {
    private String userPushId;
    private String userHash;
    private String authRequest;
    private String appPins;
    private String deviceId;

    public String getUserPushId() {
        return userPushId;
    }

    public void setUserPushId(String userPushId) {
        this.userPushId = userPushId;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public String getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(String authRequest) {
        this.authRequest = authRequest;
    }

    public String getAppPins() {
        return appPins;
    }

    public void setAppPins(String appPins) {
        this.appPins = appPins;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
