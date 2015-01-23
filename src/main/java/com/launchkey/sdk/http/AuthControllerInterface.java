package com.launchkey.sdk.http;

/**
 * Created by adame on 1/23/15.
 */
public interface AuthControllerInterface {
    JSONResponse pingGet();

    JSONResponse authsPost(String launchKeyTime, String publicKey, String userName, boolean session, boolean userPushId);

    JSONResponse pollGet(String launchKeyTime, String publicKey, String authRequest);

    JSONResponse logsPut(String authRequest, String launchKeyTime, String publicKey, String action, boolean status);

    void setAppKey(String appKey);

    String getAppKey();

    void setSecretKey(String secretKey);

    String getSecretKey();

    void setPrivateKey(String privateKey);

    String getPrivateKey();
}