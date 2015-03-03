package com.launchkey.sdk.http;

import com.launchkey.sdk.TestAbstract;
import com.launchkey.sdk.auth.WhiteLabelUserCreateResult;
import com.launchkey.sdk.crypto.Crypto;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.codec.digest.Crypt;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.internal.matchers.Matches;
import org.mockito.runners.MockitoJUnitRunner;
import sun.misc.IOUtils;

import java.io.*;
import java.net.URLDecoder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest extends TestAbstract {

    public static final String APP_KEY = "AppKey";
    public static final String SECRET_KEY = "SecretKey";
    public static final String LAUNCH_KEY_TIME = "LaunchKeyTime";

    @Mock
    private HttpClient httpClient;

    private AuthControllerInterface authController;

    @Before
    public void setUp() {
        authController = new AuthController(httpClient);
        authController.setPrivateKey(PRIVATE_KEY);
        authController.setSecretKey(SECRET_KEY);
        authController.setAppKey(APP_KEY);
    }

    @After
    public void tearDown() {
        authController = null;
    }

    @Test
    public void testPingGetReturnsErrorResponseOnIoException() throws Exception {
        String message = "Expected error message";
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenThrow(new IOException(message));
        JSONResponse actual = authController.pingGet();
        assertFalse("Unexpected success value", actual.isSuccess());
        assertEquals("Unexpected message_code", "1000", actual.getJson().get("message_code"));
        assertEquals("Unexpected message", message, actual.getJson().get("message"));
    }

    @Test
    public void testPingUsesGetAction() throws Exception {
        authController.pingGet();
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("GET", captor.getValue().getMethod());
    }

    @Test
    public void testPingUsesCorrectApiEndpoint() throws Exception {
        authController.pingGet();
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("https://api.launchkey.com/v1/ping", captor.getValue().getURI().toASCIIString());
    }

    @Test
    public void testAuthsPostUsesPostAction() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("POST", captor.getValue().getMethod());
    }

    @Test
    public void testAuthsPostUsesCorrectApiEndpoint() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("https://api.launchkey.com/v1/auths", captor.getValue().getURI().toASCIIString());
    }

    @Test
    public void testAuthsPostSendsSecretKey() throws Exception {
        authController.authsPost(LAUNCH_KEY_TIME, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing secret key",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("secret_key=")
        );
    }

    @Test
    public void testAuthsPostSendsSignature() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing signature",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("signature=")
        );
    }

    @Test
    public void testAuthsPostSendsCorrectAppKey() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for app key",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("app_key=" + APP_KEY)
        );
    }

    @Test
    public void testAuthsPostSendsCorrectUsernameValue() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, "name", false, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for username",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("username=name")
        );
    }

    @Test
    public void testAuthsPostSendsCorrectSessionValueForTrue() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, true, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for session",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("session=true")
        );
    }

    @Test
    public void testAuthsPostSendsCorrectSessionValueForFalse() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for session",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("session=false")
        );
    }

    @Test
    public void testAuthsPostSendsCorrectUserPushIdValueForTrue() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, true);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for user_push_id",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("user_push_id=true")
        );
    }

    @Test
    public void testAuthsPostSendsCorrectUserPushIdValueForFalse() throws Exception {
        authController.authsPost(null, PUBLIC_KEY, null, false, false);
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for user_push_id",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("user_push_id=false")
        );
    }

    @Test
    public void testPollGetUsesGetAction() throws Exception {
        authController.pollGet("LaunchKeyTime", PUBLIC_KEY, "AuthRequest");
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("GET", captor.getValue().getMethod());
    }

    @Test
    public void testPollGetUsesCorrectApiEndpoint() throws Exception {
        authController.pollGet("LaunchKeyTime", PUBLIC_KEY, "AuthRequest");
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(captor.getValue().getURI().toASCIIString(), startsWith("https://api.launchkey.com/v1/poll"));
    }

    @Test
    public void testPollGetSendsSignature() throws Exception {
        authController.pollGet("LaunchKeyTime", PUBLIC_KEY, "AuthRequest");
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(captor.getValue().getURI().toASCIIString(), containsString("signature="));
    }

    @Test
    public void testPollGetSendsAppKey() throws Exception {
        authController.pollGet("LaunchKeyTime", PUBLIC_KEY, "AuthRequest");
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(captor.getValue().getURI().toASCIIString(), containsString("app_key=" + APP_KEY));
    }

    @Test
    public void testPollGetSendsAuthRequest() throws Exception {
        authController.pollGet("LaunchKeyTime", PUBLIC_KEY, "AuthRequest");
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(captor.getValue().getURI().toASCIIString(), containsString("auth_request=AuthRequest"));
    }

    @Test
    public void testLogsPutUsesPutAction() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("PUT", captor.getValue().getMethod());
    }

    @Test
    public void testLogsPutUsesCorrectApiEndpoint() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpUriRequest> captor = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("https://api.launchkey.com/v1/logs", captor.getValue().getURI().toASCIIString());
    }

    @Test
    public void testLogsPutSendsSecretKey() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing secret key",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("secret_key=")
        );
    }

    @Test
    public void testLogsPutSendsSignature() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing signature",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("signature=")
        );
    }

    @Test
    public void testLogsPutSendsCorrectAppKey() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for app key",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("app_key=" + APP_KEY)
        );
    }

    @Test
    public void testLogsPutSendsCorrectAuthRequestValue() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for auth_request",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("auth_request=AuthRequest")
        );
    }

    @Test
    public void testLogsPutSendsCorrectActionValue() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for action",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("action=Action")
        );
    }

    @Test
    public void testLogsPutSendsCorrectStatusValueForTrue() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", true);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for status",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("status=true")
        );
    }

    @Test
    public void testLogsPutSendsCorrectStatusValueForFalse() throws Exception {
        authController.logsPut("AuthRequest", "LaunchKeyTime", PUBLIC_KEY, "Action", false);
        ArgumentCaptor<HttpPut> captor = ArgumentCaptor.forClass(HttpPut.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for status",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("status=false")
        );
    }

    @Test
    public void testUsersPostUsesCorrectUrl() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        String actual = captor.getValue().getURI().toASCIIString();
        assertTrue(
                "Unexpected URL: " + actual,
                actual.matches("https://api.launchkey.com/v1/users(\\?.*)?")
        );
    }

    @Test
    public void testUsersPostUsesCorrectMethod() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("POST", captor.getValue().getMethod());
    }

    @Test
    public void testUsersPostSendsStringContent() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(captor.getValue().getEntity(), instanceOf(StringEntity.class));
    }


    @Test
    public void testUsersPostSetsContentTypeToApplicationJSON() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                captor.getValue().getEntity().getContentType().getValue(),
                containsString("application/json")
        );
    }

    @Test
    public void testUsersPostSendsSignatureInQueryParameters() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing signature",
                captor.getValue().getURI().getQuery(),
                containsString("signature=")
        );
    }

    @Test
    public void testUsersPostSignsEntirePostBodyAsSignature() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        String[] queryPieces = captor.getValue().getURI().getQuery().split("&");
        String signatureString = null;
        for (int i=0; i < queryPieces.length; i++) {
            if (queryPieces[i].startsWith("signature=")) {
                signatureString = queryPieces[i].split("=")[1];
            }
        }

        assertNotNull("No signature found", signatureString);

        byte[] signature = BASE_64.decode(signatureString.getBytes());
        String data = readStream(captor.getValue().getEntity().getContent());
        assertTrue(
                "Invalid signature",
                Crypto.verifySignature(
                        PUBLIC_KEY, signature,
                        data.getBytes()
                )
        );
    }

    @Test
    public void testUsersPostSendsJSONObject() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertTrue(
                "Response does not appear to be JSON",
                JSONUtils.mayBeJSON(readStream(captor.getValue().getEntity().getContent()))
        );
    }

    @Test
    public void testUsersPostSendsSecretKey() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertTrue(
                "Missing secret key",
                JSONObject.fromObject(readStream(captor.getValue().getEntity().getContent())).containsKey("secret_key")
        );
    }

    @Test
    public void testUsersPostSendsCorrectAppKey() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        JSONObject body = JSONObject.fromObject(readStream(captor.getValue().getEntity().getContent()));
        assertTrue(
                "Missing app key",
                body.containsKey("app_key")
        );
        assertEquals(
                "Unexpected value for app key",
                APP_KEY,
                body.get("app_key")
        );
    }

    @Test
    public void testUsersPostSendsIdentifierValue() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        JSONObject body = JSONObject.fromObject(readStream(captor.getValue().getEntity().getContent()));
        assertTrue(
                "Missing identifier",
                body.containsKey("identifier")
        );
        assertEquals(
                "Unexpected value for identifier",
                "identifierValue",
                body.get("identifier")
        );
    }

    @Test
    public void testUsersPostReturnsErrorResponseOnIoException() throws Exception {
        String message = "Expected error message";
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenThrow(new IOException(message));
        JSONResponse actual = authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        assertFalse("Unexpected success value", actual.isSuccess());
        assertEquals("Unexpected message_code", "1000", actual.getJson().get("message_code"));
        assertEquals("Unexpected message", message, actual.getJson().get("message"));
    }

    @Test
    public void testUsersPostReturnHasResponse() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenReturn(getValidUsersPostJSONResponse());
        JSONResponse actual = authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        assertThat(actual.getJson().getJSONObject("response"), instanceOf(JSONObject.class));
    }

    @Test
    public void testUsersPostReturnsCorrectQrCode() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenReturn(getValidUsersPostJSONResponse());
        JSONResponse actual = authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        assertEquals("Expected QR Code", actual.getJson().getJSONObject("response").get("qrcode"));
    }

    @Test
    public void testUsersPostReturnsCorrectLkIdentifier() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenReturn(getValidUsersPostJSONResponse());
        JSONResponse actual = authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        assertEquals("Expected LK Identifier", actual.getJson().getJSONObject("response").get("lk_identifier"));
    }

    @Test
    public void testUsersPostReturnsCorrectCode() throws Exception {
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenReturn(getValidUsersPostJSONResponse());
        JSONResponse actual = authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        assertEquals("Expected Code", actual.getJson().getJSONObject("response").get("code"));
    }

    private String readStream(InputStream stream) throws IOException {
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(stream);
        int read;
        while (true) {
            read = in.read();
            if (read >= 0) {
                out.append((char) read);
            } else {
                break;
            }
        }

        return out.toString();
    }

    /**
     * For debugging purposes, here are the following values that should be known:
     *
     *          cipher is 32 character key + 16 character IV:
     *              encryption is RSA
     *              unencrypted value: "7dckxo104nuj5rmyjcvtcne11xl58b86ts5x93fguxah6dik"
     *          key: "7dckxo104nuj5rmyjcvtcne11xl58b86";
     *          IV: "ts5x93fguxah6dik";
     *          data is JSON string:
     *              encryption is AES/CBC with space (ASCII 32) padding
     *              unencrypted value: "{\"qrcode\": \"Expected QR Code\", \"lk_identifier\": \"Expected LK Identifier\", \"code\": \"Expected Code\"}";
     *
     * @return
     * @throws Exception
     */
    private JSONResponse getValidUsersPostJSONResponse() throws Exception {
        JSONObject wrapper = new JSONObject();
        JSONObject response = new JSONObject();
        response.put("data", "6LIqZsLfuoAjqqrlq00WmZqUr1XqMF6UqxfrZVVxF0D7Kba2bqlmCzFJN9j4vENiQmK9YnVOPWskeAjzflse+ZaxnumbpDjYblv4xNa5dF+bA5TiHP8jq7cfjn63kGYCEU4EAbGQK/cCg9weZnOsjQ==");
        response.put("cipher", "ccetrbPo6Ll9Cqc47ygBHIDvZ+vwBde094dm/NcCpw1Zsifh41/+pbI4XsDOltVLODU418qi6uVh\ngLfq+RwS/QsmflWngqFQk+JI42UJtHG51esDyaRHcLzRmLsS5yU+jjuMB0P5B5bDBUAGo//ox/T/\njEsoyDnV5IceHwAHlWXbOqTVAFVBlDDTvuJBHTZeUNgf25xFHTIml9roB0MVvXq8v0o+jp4yXfRP\no99pRe/Bt5wfzQZNIYhe9Cxftf5aSNlc/D/5/Kl/aZmcq1IdIraALQSQKSe1So/nh6P88TtmaQ3P\nVZgh6e653PDM9zRKGNyu7EWoXyNdIKJtlRv88w==\n");
        wrapper.put("response", response);

        JSONResponse jsonResponse = new JSONResponse();
        jsonResponse.setSuccess(true);
        jsonResponse.setJson(wrapper);
        return jsonResponse;
    }
}