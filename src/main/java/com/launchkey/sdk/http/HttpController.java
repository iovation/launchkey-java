package com.launchkey.sdk.http;

import com.launchkey.sdk.Util;
import com.launchkey.sdk.crypto.Crypto;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.Crypt;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HttpController {
    private static final Logger LOG = Logger.getLogger(HttpController.class.getName());

    protected static final String PROD_SERVER_URL = "https://api.launchkey.com/v1";
    protected static final String STAGING_SERVER_URL = "https://staging-api.launchkey.com/v1";

    protected final HttpClient httpClient;
    protected final String serverUrl;
    protected String appKey;
    protected String secretKey;
    protected String privateKey;

    public HttpController(HttpClient httpClient) {
        this(httpClient, false);
    }

    public HttpController(HttpClient httpClient, boolean isStaging) {
        this.httpClient = httpClient;
        serverUrl = isStaging ? STAGING_SERVER_URL : PROD_SERVER_URL;
    }

    protected static final class JSONCallbackResponseHandler implements ResponseHandler<Integer> {
        private final JSONHttpCallback callback;

        public JSONCallbackResponseHandler(JSONHttpCallback callback) {
            this.callback = callback;
        }

        public Integer handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.fromObject(responseString);
            if(statusCode == 200 || statusCode == 201) {
                callback.onSuccess(jsonObject);
            }
            else {
                callback.onSuccess(jsonObject);
            }
            return statusCode;
        }
    }

    protected static final class JSONResponseHandler implements ResponseHandler<JSONResponse> {
        public JSONResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseString = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSONObject.fromObject(responseString);
            JSONResponse jsonResponse = new JSONResponse();
            jsonResponse.setJson(jsonObject);
            if(statusCode == 200 || statusCode == 201) {
                jsonResponse.setSuccess(true);
            }
            else {
                jsonResponse.setSuccess(false);
            }
            return jsonResponse;
        }
    }


    protected ArrayList<NameValuePair> defaultPostParams(String launchKeyTime, String publicKey) throws Exception {
        byte[] encryptedSecretKey = getEncryptedSecretKey(secretKey, launchKeyTime, publicKey);
        byte[] signedData = Crypto.signWithPrivateKey(encryptedSecretKey, privateKey);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("secret_key", new String(Util.base64Encode(encryptedSecretKey), "UTF-8")));
        params.add(new BasicNameValuePair("signature", new String(Util.base64Encode(signedData), "UTF-8")));
        params.add(new BasicNameValuePair("app_key", appKey));

        return params;
    }

    /**
     * Builds the encrypted secret_key json dictionary included in calls to the LaunchKey api
     *
     * @return The encrypted secret_key json dictionary
     * @throws Exception
     */
    protected byte[] getEncryptedSecretKey(String secretKey, String launchkeyTime, String publicKey) throws Exception {
        JSONObject json = new JSONObject();
        json.put("secret", secretKey);
        json.put("stamped", launchkeyTime);

        byte[] encryptedCipher = Crypto.encryptRSA(json.toString().getBytes(), publicKey);

        return encryptedCipher;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
