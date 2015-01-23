package com.launchkey.sdk.http;

import com.launchkey.sdk.Util;
import com.launchkey.sdk.crypto.Crypto;
import net.sf.json.JSONObject;
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

    protected static final String SERVER_URL = "https://api.launchkey.com/v1";

    protected final HttpClient httpClient;
    protected String appKey;
    protected String secretKey;
    protected String privateKey;

    public HttpController(HttpClient httpClient) {
        this.httpClient = httpClient;
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
        byte[] signedData = getSignatureOnSecretKey(encryptedSecretKey, privateKey);

        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("secret_key", new String(Util.base64Encode(encryptedSecretKey), "UTF-8")));
        params.add(new BasicNameValuePair("signature", new String(Util.base64Encode(signedData), "UTF-8")));
        params.add(new BasicNameValuePair("app_key", appKey));

        return params;
    }

    /**
     * Pad a string into multiples of 16 bytes
     *
     * @return padded byte[]
     * @throws Exception
     */
    private static byte[] padString16Bytes(String s) throws Exception {
        byte[] bytes = s.getBytes("UTF-8");
        if(bytes.length % 16 != 0) {
            int bytesToPad = 16 - (bytes.length % 16);
            byte[] paddedBytes = new byte[bytes.length + bytesToPad];
            System.arraycopy(bytes, 0, paddedBytes, 0, bytes.length);
            for(int i = bytes.length; i < paddedBytes.length; i++) {
                paddedBytes[i] = " ".getBytes()[0];
            }
            return paddedBytes;
        }
        return bytes;
    }

    /**
     * Pad a string into multiples of 16 bytes
     *
     * @return padded byte[]
     * @throws Exception
     */
    private byte[] encryptWithApiPublicKey(byte[] message, String publicKey) throws Exception {
        String strippedKey = Crypto.stripPublicKeyHeaders(publicKey);
        PublicKey apiPublicKey =
                Crypto.getRSAPublicKeyFromString(strippedKey);
        Cipher rsaCipher = Crypto.getRSACipher();
        rsaCipher.init(Cipher.ENCRYPT_MODE, apiPublicKey);
        return rsaCipher.doFinal(message);
    }

    /**
     * Signs with the private key
     *
     * @return signature on the bytes[]
     * @throws Exception
     */
    private byte[] signWithPrivateKey(byte[] bytes, String privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initSign(Crypto.getRSAPrivateKeyFromString(privateKey));
        signature.update(bytes);
        return signature.sign();
    }

    /**
     * Builds the encrypted secret_key json dictionary included in calls to the LaunchKey api
     *
     * @return The encrypted secret_key json dictionary
     * @throws Exception
     */
    private byte[] getEncryptedSecretKey(String secretKey, String launchkeyTime, String publicKey) throws Exception {
        JSONObject json = new JSONObject();
        json.put("secret", secretKey);
        json.put("stamped", launchkeyTime);

        byte[] jsonBytes = padString16Bytes(json.toString());
        byte[] encryptedCipher = encryptWithApiPublicKey(jsonBytes, publicKey);

        return encryptedCipher;
    }

    /**
     * Builds the signature on the encrypted secret_key json dictionary
     *
     * @return The signature on the secret_key json dictionary
     * @throws Exception
     */
    private byte[] getSignatureOnSecretKey(byte[] secret, String privateKey) throws Exception {
        byte[] signedData = signWithPrivateKey(secret, privateKey);
        return signedData;
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
