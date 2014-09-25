package com.launchkey.sdk.auth;

import com.launchkey.sdk.Util;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.http.AuthController;
import com.launchkey.sdk.http.JSONResponse;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthenticationManager {
    private static final String AUTHENTICATE = "Authenticate";
    private static final String REVOKE = "Revoke";
    private static final String DEFAULT_ERROR_CODE = "1000";

    private final AuthController authController;
    private final String _privateKeyString;
    private String _publicKey;

    public AuthenticationManager(AuthController authController) {
        this.authController = authController;
        this._privateKeyString = authController.getPrivateKey();
    }

    /**
     * Call to authorize a username via LaunchKey
     *
     * @param username
     */
    public AuthorizeResult authorize(final String username) throws AuthenticationException {
        return authorize(username, false, false);
    }

    /**
     * Call to authorize a username via LaunchKey
     *
     * @param username
     * @param userPushId
     */
    public AuthorizeResult authorize(final String username, boolean userPushId) throws AuthenticationException {
        return authorize(username, false, userPushId);
    }

    /**
     * Call to authorize a username via LaunchKey
     * @param username
     * @param transactional
     * @param userPushId
     * @return
     * @throws AuthenticationException
     */
    public AuthorizeResult authorize(final String username, boolean transactional, boolean userPushId) throws AuthenticationException {
        JSONResponse pingResponse = this.authController.pingGet();
        if(pingResponse.isSuccess()) {
            String launchkeyTime = pingResponse.getJson().getString("launchkey_time");
            _publicKey = pingResponse.getJson().getString("key");
            JSONResponse authsPostResponse = authController.authsPost(launchkeyTime, _publicKey, username, !transactional, userPushId);
            if(authsPostResponse.isSuccess()) {
                AuthorizeResult result = new AuthorizeResult();
                result.setAuthRequest(authsPostResponse.getJson().getString("auth_request"));
                result.setLaunchkeyTime(launchkeyTime);
                return result;
            }
            else {
                throw new AuthenticationException(getErrorMessage(authsPostResponse.getJson()),
                        getErrorCode(authsPostResponse.getJson()));
            }
        }
        else {
            throw new AuthenticationException(getErrorMessage(pingResponse.getJson()),
                    getErrorCode(pingResponse.getJson()));
        }
    }

    /**
     * Call to determine whether the username is still authorized (i.e. has not remotely ended the session)
     * @param authRequest
     * @param launchkeyTime
     * @return
     * @throws AuthenticationException
     */
    public boolean isAuthorized(final String authRequest, String launchkeyTime)
            throws AuthenticationException {
        JSONResponse pingResponse = this.authController.pingGet();
        if(pingResponse.isSuccess()) {
            JSONResponse pollResponse = authController.pollGet(launchkeyTime, _publicKey, authRequest);
            if(pollResponse.isSuccess()) {
                return true;
            }
            else {
                if(pollResponse.getJson().has("message_code") &&
                        pollResponse.getJson().getString("message_code").equals("70404")) {
                    logsPutLogout(authRequest, true);
                }
                return false;
            }
        }
        else {
            throw new AuthenticationException(getErrorMessage(pingResponse.getJson()),
                    getErrorCode(pingResponse.getJson()));
        }
    }

    /**
     * Call to end a session
     *
     * @param authRequest
     */
    public boolean logout(String authRequest) throws AuthenticationException {
        return logsPutLogout(authRequest, true);
    }

    /**
     * Method to poll (Poll GET) the server for an authorization
     * @param authRequest
     * @param launchkeyTime
     * @return
     * @throws AuthenticationException
     */
    public PollResult poll(String authRequest, String launchkeyTime) throws AuthenticationException {
       JSONResponse pollGetResponse =  authController.pollGet(launchkeyTime, _publicKey, authRequest);
       if(pollGetResponse.isSuccess()) {
           String encryptedAuth = pollGetResponse.getJson().getString("auth");
           String userHash = pollGetResponse.getJson().getString("user_hash");
           String userPushId = pollGetResponse.getJson().getString("user_push_id");

           byte[] resultNot64 = null;
           String result = null;
           try {
               resultNot64 = Crypto.decryptWithPrivateKey(Util.base64Decode(encryptedAuth.getBytes("UTF-8")),
                       _privateKeyString);
               result = new String(resultNot64, "UTF-8");
           }
           catch(Exception e) {
               //no op
           }

           JSONObject jsonResult = JSONObject.fromObject(result);

           if(!authRequest.equals(jsonResult.getString("auth_request"))) {
               throw new AuthenticationException("Auth tokens do not match", DEFAULT_ERROR_CODE);
           }
           else {
               boolean action = Boolean.valueOf(jsonResult.getString("response"));
               String appPins = jsonResult.getString("app_pins");
               String deviceId = jsonResult.getString("device_id");

               return logsPutAuthenticate(userHash, authRequest, appPins, deviceId, userPushId, action);
           }

       }
        else {
           if(pollGetResponse.getJson().has("message_code")) {
               if(pollGetResponse.getJson().getString("message_code").equals("70404")) {
                   throw new AuthenticationException("User denied request", DEFAULT_ERROR_CODE);
               }
               else if(!pollGetResponse.getJson().getString("message_code").equals("70403")) {
                   throw new AuthenticationException(getErrorMessage(pollGetResponse.getJson()),
                           getErrorCode(pollGetResponse.getJson()));
               }
           }
           throw new AuthenticationException(getErrorMessage(pollGetResponse.getJson()),
                   getErrorCode(pollGetResponse.getJson()));
       }
    }

    /**
     * Does a logs PUT when the polling is complete to notify the server of the action
     * @param userHash
     * @param authRequest
     * @param appPins
     * @param deviceId
     * @param userPushId
     * @param status
     * @return
     * @throws AuthenticationException
     */
    private PollResult logsPutAuthenticate(String userHash, String authRequest, String appPins, String deviceId, String userPushId,
                                     final boolean status) throws AuthenticationException {
        JSONResponse pingResponse = this.authController.pingGet();
        if(pingResponse.isSuccess()) {
            String launchkeyTime = pingResponse.getJson().getString("launchkey_time");
            _publicKey = pingResponse.getJson().getString("key");
            JSONResponse logsPutResponse = authController.logsPut(authRequest, launchkeyTime, _publicKey, AUTHENTICATE, status);
            if(logsPutResponse.isSuccess()) {
                if(status) {
                    PollResult pollResult = new PollResult();
                    pollResult.setUserHash(userHash);
                    pollResult.setAuthRequest(authRequest);
                    pollResult.setAppPins(appPins);
                    pollResult.setDeviceId(deviceId);
                    pollResult.setUserPushId(userPushId);
                    return pollResult;
                }
                else {
                    throw new AuthenticationException("User denied request", DEFAULT_ERROR_CODE);
                }
            }
            else {
                throw new AuthenticationException(getErrorMessage(logsPutResponse.getJson()),
                        getErrorCode(logsPutResponse.getJson()));
            }
        }
        else {
            throw new AuthenticationException(getErrorMessage(pingResponse.getJson()),
                    getErrorCode(pingResponse.getJson()));
        }
    }

    /**
     * Does a logs PUT when the polling is complete to notify the server of the action
     * @param authRequest
     * @param status
     * @return
     * @throws AuthenticationException
     */
    private boolean logsPutLogout(String authRequest, boolean status) throws AuthenticationException {
        JSONResponse pingResponse = this.authController.pingGet();
        if(pingResponse.isSuccess()) {
            String launchkeyTime = pingResponse.getJson().getString("launchkey_time");
            _publicKey = pingResponse.getJson().getString("key");
            JSONResponse logsPutResponse = authController.logsPut(authRequest, launchkeyTime, _publicKey, REVOKE,
                    status);
            return logsPutResponse.isSuccess();
        }
        else {
            throw new AuthenticationException(getErrorMessage(pingResponse.getJson()),
                    getErrorCode(pingResponse.getJson()));
        }
    }

    /**
     * Verify the deorbit request by signature and timestamp
     * @param deorbit  JSON string from LaunchKey with the user_hash and launchkey_time.
     * @param signature Signature signed by API to verify the authenticity of the data found in the deorbit JSON.
     * @return the user_hash needed to identify the user and log them out, null on failure
     */
    public String deorbit(String deorbit, String signature) {
        JSONResponse pingResponse = this.authController.pingGet();
        if(pingResponse.isSuccess()) {
            String launchkeyTime = pingResponse.getJson().getString("launchkey_time");
            try {
                if(Crypto.verifySignature(this._publicKey, signature.getBytes("UTF-8"),
                        deorbit.getBytes("UTF-8"))) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date pingTime = simpleDateFormat.parse(launchkeyTime);
                    JSONObject jsonObject = JSONObject.fromObject(deorbit);
                    Date requestDate = simpleDateFormat.parse(jsonObject.getString("launchkey_time"));
                    if(pingTime.getTime() - requestDate.getTime() < 5 * 1000 * 60) {
                        //only handle requests within a 5 minute time delay
                        return jsonObject.getString("user_hash");
                    }
                }
            }
            catch(Exception e) {
                //no op
            }
        }
        return null;
    }

    private static String getErrorMessage(JSONObject response) {
        if(response.has("message")) {
            return response.getString("message");
        }
        return "";
    }

    private static String getErrorCode(JSONObject response) {
        if(response.has("message_code")) {
            return response.getString("message_code");
        }
        return "";
    }
}
