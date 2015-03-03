package com.launchkey.sdk.http;

import com.launchkey.sdk.Util;
import com.launchkey.sdk.crypto.Crypto;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

public class AuthController extends HttpController implements AuthControllerInterface {
    private static final String PING_PATH = "/ping";
    private static final String AUTHS_PATH = "/auths";
    private static final String LOGS_PATH = "/logs";
    private static final String POLL_PATH = "/poll";
    private static final String USERS_PATH = "/users";

    public AuthController(HttpClient httpClient) {
        super(httpClient);
    }

    public AuthController(HttpClient httpClient, boolean isStaging) {
        super(httpClient, isStaging);
    }

    @Override
    public JSONResponse pingGet() {
        StringBuilder url = new StringBuilder(this.serverUrl);
        url.append(PING_PATH);
        HttpGet get = new HttpGet(url.toString());
        try {
            return httpClient.execute(get, new JSONResponseHandler());
        }
        catch(IOException e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public JSONResponse authsPost(String launchKeyTime, String publicKey, String userName, boolean session, boolean userPushId) {
        StringBuilder url = new StringBuilder(this.serverUrl);
        url.append(AUTHS_PATH);
        HttpPost post = new HttpPost(url.toString());
        try {
            ArrayList<NameValuePair> params = this.defaultPostParams(launchKeyTime, publicKey);
            params.add(new BasicNameValuePair("username", userName));
            params.add(new BasicNameValuePair("session", String.valueOf(session)));
            params.add(new BasicNameValuePair("user_push_id", String.valueOf(userPushId)));

            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            return httpClient.execute(post, new JSONResponseHandler());
        }
        catch(Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public JSONResponse pollGet(String launchKeyTime, String publicKey, String authRequest) {
        try {
            StringBuilder url = new StringBuilder(this.serverUrl);
            url.append(POLL_PATH);
            ArrayList<NameValuePair> params = this.defaultPostParams(launchKeyTime, publicKey);
            params.add(new BasicNameValuePair("auth_request", authRequest));
            url.append("?").append(URLEncodedUtils.format(params, "UTF-8"));
            HttpGet get = new HttpGet(url.toString());
            return httpClient.execute(get, new JSONResponseHandler());
        }
        catch(Exception ex) {
            return getErrorResponse(ex);
        }
    }

    @Override
    public JSONResponse logsPut(String authRequest, String launchKeyTime, String publicKey, String action, boolean status) {
        StringBuilder url = new StringBuilder(this.serverUrl);
        url.append(LOGS_PATH);
        HttpPut put = new HttpPut(url.toString());
        try {
            ArrayList<NameValuePair> params = this.defaultPostParams(launchKeyTime, publicKey);
            params.add(new BasicNameValuePair("auth_request", authRequest));
            params.add(new BasicNameValuePair("action", action));
            params.add(new BasicNameValuePair("status", String.valueOf(status)));
            put.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            return httpClient.execute(put, new JSONResponseHandler());
        }
        catch(Exception e) {
            return getErrorResponse(e);
        }
    }
    
    @Override
    public JSONResponse usersPost(String launchKeyTime, String publicKey, String identifier) {
        JSONResponse response;
        try {
            JSONObject packetObject = new JSONObject();
            packetObject.put("secret_key", new String(Util.base64Encode(getEncryptedSecretKey(secretKey, launchKeyTime, publicKey))));
            packetObject.put("app_key", appKey);
            packetObject.put("identifier", identifier);
            String packet = packetObject.toString();

            String url = new StringBuilder(this.serverUrl).append(USERS_PATH).toString();
            URIBuilder uriBuilder = new URIBuilder(url.toString());
            uriBuilder.addParameter("signature", new String(Util.base64Encode(Crypto.signWithPrivateKey(packet.getBytes(), privateKey))));

            HttpPost post = new HttpPost(uriBuilder.build());
            post.setEntity(new StringEntity(packet, ContentType.APPLICATION_JSON));
            response = httpClient.execute(post, new JSONResponseHandler());
            JSONObject responseObject = response.getJson().getJSONObject("response");
            byte[] keyIvBytes = Crypto.decryptRSA(Util.base64Decode(responseObject.getString("cipher").getBytes()), privateKey);
            byte[] key = Arrays.copyOfRange(keyIvBytes, 0, keyIvBytes.length - 16);
            byte[] iv = Arrays.copyOfRange(keyIvBytes, keyIvBytes.length - 16, keyIvBytes.length);
            byte[] data = Crypto.decryptAES(Util.base64Decode(responseObject.getString("data").getBytes()), key, iv);
            JSONObject responseJSON = JSONObject.fromObject(new String(data));
            responseObject.put("qrcode", responseJSON.get("qrcode"));
            responseObject.put("lk_identifier", responseJSON.get("lk_identifier"));
            responseObject.put("code", responseJSON.get("code"));
        } catch (Exception e) {
            response = getErrorResponse(e);
        }
        return response;
    }

    private static JSONResponse getErrorResponse(Exception e) {
        JSONObject json = new JSONObject();
        json.put("message", e.getMessage());
        json.put("message_code", "1000");
        JSONResponse response = new JSONResponse();
        response.setJson(json);
        return response;
    }
}
