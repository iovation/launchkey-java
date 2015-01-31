package com.launchkey.sdk.http;

/**
 * Created by adame on 1/23/15.
 */
public interface AuthControllerInterface {
    JSONResponse pingGet();

    JSONResponse authsPost(String launchKeyTime, String publicKey, String userName, boolean session, boolean userPushId);

    JSONResponse pollGet(String launchKeyTime, String publicKey, String authRequest);

    JSONResponse logsPut(String authRequest, String launchKeyTime, String publicKey, String action, boolean status);

    /**
     * Create a LaunchKey user
     *  
     * @param launchKeyTime LaunchKey time from the most recent poll request
     * @param publicKey LaunchKey pubic key from the most recent poll request
     * @param identifier How the user is identified to your application. This should be a static value such as a
     * user's ID or UUID value rather than an email address which may be subject to change
     * @return JSON response whose JSONObject contains the following on success:
     *    qrcode - The URL to a QR Code for the device to scan
     *    lk_identifier - The value to store in order to push future requests to this user
     *    code - Manual code for the user to type into their device if they are unable to scan the QR Code
     */
    JSONResponse usersPost(String launchKeyTime, String publicKey, String identifier);

    void setAppKey(String appKey);

    String getAppKey();

    void setSecretKey(String secretKey);

    String getSecretKey();

    void setPrivateKey(String privateKey);

    String getPrivateKey();
}