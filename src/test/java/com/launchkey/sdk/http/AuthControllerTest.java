package com.launchkey.sdk.http;

import com.launchkey.sdk.TestAbstract;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HTTP;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sun.misc.IOUtils;

import java.io.IOException;
import java.net.URLDecoder;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
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
        assertEquals("https://api.launchkey.com/v1/users", captor.getValue().getURI().toASCIIString());
    }

    @Test
    public void testUsersPostUsesCorrectMethod() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertEquals("POST", captor.getValue().getMethod());

    }

    @Test
    public void testUsersPostSendsSecretKey() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing secret key",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("secret_key=")
        );
    }

    @Test
    public void testUsersPostSendsSignature() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Missing signature",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("signature=")
        );
    }
    @Test
    public void testUsersPostSendsCorrectAppKey() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for app key",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("app_key=" + APP_KEY)
        );
    }

    @Test
    public void testUsersPostSendsIdentifierValue() throws Exception {
        authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        ArgumentCaptor<HttpPost> captor = ArgumentCaptor.forClass(HttpPost.class);
        verify(httpClient).execute(captor.capture(), any(ResponseHandler.class));
        assertThat(
                "Unexpected value for identifier",
                URLDecoder.decode(new String(IOUtils.readFully(captor.getValue().getEntity().getContent(), -1, false)), HTTP.UTF_8),
                containsString("identifier=identifierValue")
        );
    }
 
    @Test
    public void testUsersPostSendsErrorResponseOnIoException() throws Exception {
        String message = "Expected error message";
        when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class))).
                thenThrow(new IOException(message));
        JSONResponse actual = authController.usersPost("LaunchKeyTime", PUBLIC_KEY, "identifierValue");
        assertFalse("Unexpected success value", actual.isSuccess());
        assertEquals("Unexpected message_code", "1000", actual.getJson().get("message_code"));
        assertEquals("Unexpected message", message, actual.getJson().get("message"));
    }

}