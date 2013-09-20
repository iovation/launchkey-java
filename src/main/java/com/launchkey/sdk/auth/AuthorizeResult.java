package com.launchkey.sdk.auth;

public class AuthorizeResult {
    private String authRequest;
    private String launchkeyTime;

    public String getAuthRequest() {
        return authRequest;
    }

    public void setAuthRequest(String authRequest) {
        this.authRequest = authRequest;
    }

    public String getLaunchkeyTime() {
        return launchkeyTime;
    }

    public void setLaunchkeyTime(String launchkeyTime) {
        this.launchkeyTime = launchkeyTime;
    }
}
