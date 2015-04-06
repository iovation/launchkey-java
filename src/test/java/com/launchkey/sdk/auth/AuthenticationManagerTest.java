package com.launchkey.sdk.auth;

import com.launchkey.sdk.TestAbstract;
import com.launchkey.sdk.http.AuthControllerInterface;
import com.launchkey.sdk.http.JSONResponse;
import net.sf.json.JSONObject;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.IsNot.not;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationManagerTest extends TestAbstract {

    @Mock
    private AuthControllerInterface authController;

    private static final String LAUNCHKEY_TIME = "LaunchKeyTime";

    private static final String AUTH_REQUEST = "AuthRequest";

    private static final String USER_HASH = "UserHash";

    private static final String USER_PUSH_ID = "UserPushId";
    
    private static final String AUTH_DATA_SAME_AUTH_REQUEST =
            "K7fjTWGiT2MYe4qP7pRDmA2zxTvw5tXAp4zB5LFWxfbw2kZW1lkq8+Y7N8AKPAx/CpXfsFX7EJYf\ntiSeM2yivuJd2LzzQtzRYTaBGo/tdoO5AxC4q9lGon1gE0L9Wa2fgeLcXB25WFTA17w4bA6TAolY\n2WhfZbXHX+ibdHUL4T3kmXacKkgcGZZShn8AgyV+erTD515L+AMVDdoCIAmWDxpW0A85FRJVZBM2\n1zpGFsErUE5QnzHcGedt/qsck81+F9QcmhSybaDPHJsKffIZh9hF4OOqeblCSs9J7LRmRNwr/Osy\nxYNuV5xMqB6pff1wyGRAmti6CvsSLxSNPACgug==\n";

    private JSONResponse pingResponse;

    private JSONObject pingResponseJson;

    private JSONResponse authsPostResponse;

    private JSONObject authsPostResponseJson;

    private JSONResponse logsPutResponse;

    private JSONResponse pollResponse;

    private JSONObject pollResponseJson;
    
    private JSONResponse usersPostResponse;
    
    private JSONObject usersPostResponseJson;

    private AuthenticationManager authenticationManager;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Before
    public void setUp() {
        pingResponseJson = new JSONObject();
        pingResponseJson.put("key", PUBLIC_KEY);
        pingResponseJson.put("launchkey_time", LAUNCHKEY_TIME);

        pingResponse = new JSONResponse();
        pingResponse.setJson(pingResponseJson);
        pingResponse.setSuccess(true);

        when(authController.pingGet()).thenReturn(pingResponse);

        authsPostResponseJson = new JSONObject();
        authsPostResponseJson.put("auth_request", AUTH_REQUEST);
        authsPostResponse = new JSONResponse();
        authsPostResponse.setJson(authsPostResponseJson);
        authsPostResponse.setSuccess(true);

        when(authController.authsPost(anyString(), anyString(), anyString(), anyBoolean(), anyBoolean()))
                .thenReturn(authsPostResponse);

        logsPutResponse = new JSONResponse();
        logsPutResponse.setSuccess(true);

        when(authController.logsPut(anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(logsPutResponse);

        pollResponse = new JSONResponse();
        pollResponse.setSuccess(true);
        when(authController.pollGet(anyString(), anyString(), anyString())).thenReturn(pollResponse);

        pollResponseJson = new JSONObject();
        pollResponse.setJson(pollResponseJson);
        pollResponseJson.put("auth", AUTH_REQUEST);
        pollResponseJson.put("user_hash", USER_HASH);
        pollResponseJson.put("user_push_id", USER_PUSH_ID);

        when(authController.getPrivateKey()).thenReturn(PRIVATE_KEY);

        usersPostResponse = new JSONResponse();
        usersPostResponse.setSuccess(true);
        JSONObject intermediateUserPostResponseJson = new JSONObject();
        usersPostResponse.setJson(intermediateUserPostResponseJson);
        usersPostResponseJson = new JSONObject();
        usersPostResponseJson.put("qrcode", "a qrcode");
        usersPostResponseJson.put("code", "a code");
        intermediateUserPostResponseJson.put("response", usersPostResponseJson);

        when(authController.usersPost(anyString(), anyString(), anyString())).thenReturn(usersPostResponse);

        authenticationManager = new AuthenticationManager(authController);
    }

    @After
    public void tearDown() {
        pingResponse = null;
        pingResponseJson = null;
        authsPostResponse = null;
        authsPostResponseJson = null;
        authenticationManager = null;
        logsPutResponse = null;
        pingResponse = null;
        pingResponseJson = null;
        usersPostResponse = null;
        usersPostResponseJson = null;
    }

    @Test
    public void testAuthorizeUsernameDefaultsPushIdToFalse() throws Exception {
        authenticationManager.authorize("username");
        verify(authController).authsPost(anyString(), anyString(), anyString(), anyBoolean(), eq(false));
    }

    @Test
    public void testAuthorizeUsernameDefaultsSessionToFalse() throws Exception {
        authenticationManager.authorize("username");
        verify(authController).authsPost(anyString(), anyString(), anyString(), eq(false), anyBoolean());
    }

    @Test
    public void testAuthorizeUsernameSendsCorrectUsername() throws Exception {
        authenticationManager.authorize("username");
        verify(authController).authsPost(anyString(), anyString(), eq("username"), anyBoolean(), anyBoolean());
    }

    @Test
    public void testAuthorizeUserNameUserPushIdDefaultsSessionIdToFalse() throws Exception {
        authenticationManager.authorize("username", true);
        verify(authController).authsPost(anyString(), anyString(), anyString(), eq(false), anyBoolean());
    }

    @Test
    public void testAuthorizeUserNameUserPushIdSendsCorrectUsername() throws Exception {
        authenticationManager.authorize("username", true);
        verify(authController).authsPost(anyString(), anyString(), eq("username"), anyBoolean(), anyBoolean());
    }

    @Test
    public void testAuthorizeUserNameUserPushIdSendsCorrectPushId() throws Exception {
        authenticationManager.authorize("username", true);
        verify(authController).authsPost(anyString(), anyString(), anyString(), anyBoolean(), eq(true));
    }

    @Test
    public void testAuthorizeUserNameSessionUserPushIdSendsCorrectSessionId() throws Exception {
        authenticationManager.authorize("username", true, true);
        verify(authController).authsPost(anyString(), anyString(), anyString(), eq(true), anyBoolean());
    }

    @Test
    public void testAuthorizeUserNameSessionUserPushIdSendsCorrectUsername() throws Exception {
        authenticationManager.authorize("username", true, true);
        verify(authController).authsPost(anyString(), anyString(), eq("username"), anyBoolean(), anyBoolean());
    }

    @Test
    public void testAuthorizeUserNameSessionUserPushIdSendsCorrectPushId() throws Exception {
        authenticationManager.authorize("username", true, true);
        verify(authController).authsPost(anyString(), anyString(), anyString(), anyBoolean(), eq(true));
    }

    @Test
    public void testAuthorizeSendsLaunchKeyTimeFromPing() throws Exception {
        authenticationManager.authorize("username");
        verify(authController).authsPost(eq(LAUNCHKEY_TIME), anyString(), anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    public void testAuthorizeSendsPublicKeyFromPing() throws Exception {
        authenticationManager.authorize("username");
        verify(authController).authsPost(anyString(), eq(PUBLIC_KEY), anyString(), anyBoolean(), anyBoolean());
    }

    @Test
    public void testAuthorizeReturnsAuthRequestFromAuth() throws Exception {
        AuthorizeResult result = authenticationManager.authorize("username");
        assertEquals(AUTH_REQUEST, result.getAuthRequest());
    }

    @Test
    public void testAuthorizeReturnsLaunchKeyTimeFromPing() throws Exception {
        AuthorizeResult result = authenticationManager.authorize("username");
        assertEquals(LAUNCHKEY_TIME, result.getLaunchkeyTime());
    }

    @Test(expected = AuthenticationException.class)
    public void testAuthorizeThrowsAuthenticationExceptionWhenPingFails() throws Exception {
        pingResponse.setSuccess(false);
        authenticationManager.authorize("username");
    }

    @Test(expected = AuthenticationException.class)
    public void testAuthorizeThrowsAuthenticationExceptionWhenAuthPostFails() throws Exception {
        authsPostResponse.setSuccess(false);
        authenticationManager.authorize("username");
    }

    @Test
    public void testLogoutSendsProvidedAuthRequestToLogsCall() throws Exception {
        authenticationManager.logout("auth request");
        verify(authController).logsPut(eq("auth request"), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testLogoutSendsAuthTimeFromPingCallToLogsCall() throws Exception {
        authenticationManager.logout("auth request");
        verify(authController).logsPut(anyString(), eq(LAUNCHKEY_TIME), anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testLogoutSendsPublicKeyFromPingCallToLogsCall() throws Exception {
        authenticationManager.logout("auth request");
        verify(authController).logsPut(anyString(), anyString(), eq(PUBLIC_KEY), anyString(), anyBoolean());
    }

    @Test
    public void testLogoutSendsActionOfRevokeFromPingCallToLogsCall() throws Exception {
        authenticationManager.logout("auth request");
        verify(authController).logsPut(anyString(), anyString(), anyString(), eq("Revoke"), anyBoolean());
    }

    @Test
    public void testLogoutSendsStatusOfTrueFromPingCallToLogsCall() throws Exception {
        authenticationManager.logout("auth request");
        verify(authController).logsPut(anyString(), anyString(), anyString(), anyString(), eq(true));
    }

    @Test
    public void testPollThrowsUserDeniedAuthenticationExceptionWhenPollResponseHasMessageCode70404() throws Exception {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("User denied request");
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
    }

    @Test
    public void testPollThrowsAuthenticationExceptionWhenPollResponseHasMessageCode70403() throws Exception {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70403");

        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("70403");
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
    }

    @Test
    public void testPollThrowsAuthenticationExceptionWhenPollResponseHasNoMessageCode() throws Exception {
        pollResponse.setSuccess(false);

        thrown.expect(AuthenticationException.class);
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
    }

    @Test
    public void testPollThrowsAuthenticationExceptionWhenAuthTokensDoNotMatch() throws Exception {
        pollResponseJson.put("auth", AUTH_DATA_SAME_AUTH_REQUEST);
        thrown.expect(AuthenticationException.class);
        thrown.expectMessage("Auth tokens do not match");
        authenticationManager.poll("AnotherAuthRequest", LAUNCHKEY_TIME);
    }

    @Test
    public void testPollSendsTheCorrectLaunchKeyTimeToPollGet() throws AuthenticationException {
        pollResponseJson.put("auth", AUTH_DATA_SAME_AUTH_REQUEST);
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).pollGet(eq(LAUNCHKEY_TIME), anyString(), anyString());
    }

    @Test
    public void testPollSendsTheCorrectPublicKeyToPollGet() throws AuthenticationException {
        pollResponseJson.put("auth", AUTH_DATA_SAME_AUTH_REQUEST);
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).pollGet(anyString(), eq(PUBLIC_KEY), anyString());
    }

    @Test
    public void testPollSendsTheCorrectAuthRequestToPollGet() throws AuthenticationException {
        pollResponseJson.put("auth", AUTH_DATA_SAME_AUTH_REQUEST);
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).pollGet(anyString(), anyString(), eq(AUTH_REQUEST));
    }

    @Test
    public void testPollLogsAuthentication() throws Exception {
        pollResponseJson.put("auth", AUTH_DATA_SAME_AUTH_REQUEST);
        authenticationManager.poll(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(AUTH_REQUEST, LAUNCHKEY_TIME, PUBLIC_KEY, "Authenticate", true);
    }

    @Test
    public void testDeorbitReturnsNullOnUnsuccessfulPing() throws Exception {
        pingResponse.setSuccess(false);
        String message = "";
        String signature = "";
        String actual = authenticationManager.deorbit(message, signature);
        assertNull(actual);
    }

    @Test
    public void testDeorbitReturnsNullOnInvalidSignature() throws Exception {
        pingResponse.setSuccess(true);
        String message = "";
        String signature = "Invalid Signature";
        String actual = authenticationManager.deorbit(message, signature);
        assertNull(actual);
    }

    @Test
    public void testDeorbitReturnsNullOnExpiredRequest() throws Exception {
        pingResponse.setSuccess(true);
        pingResponseJson.put("launchkey_time", "2000-01-01 00:05:00");
        String message = "{\"launchkey_time\": \"2000-01-01 00:00:00\", \"user_hash\": \"userHash\"}";
        String signature = "Q+DQatq/Dn92mkkfeZasmjnp9f7IzJMKufPY+zKnahTa+PuMO3MDc2VCOoLxKWGHB5gTlLjb4v/Nb0ZwWU+o4wapTmbRCrOtRG0ydhzL3S16iInnThC7KnfImUHNY8f6zQC/yzMT9U0POTKgQynOSGq8G4D1ByZS1nAUCmJPiEAenIKDptjlv5r9cKLLO8m8TNzPsO2x7awbqSliBWSjy1U6woqn/mCStHQE2h7ay4F5N1IfVr0FZd/IMRWmobffhX767G8RHIG9BMYpz+tJkWOcVz8k3vTDAOp3EunNDr4dxDtN6c8xw6eDh76/fdjwsF94jJ2gh/sHtBXB/vs7eA==";
        String actual = authenticationManager.deorbit(message, signature);
        assertNull(actual);
    }

    @Test
    public void testDeorbitReturnsUserHashOnValidRequest() throws Exception {
        pingResponse.setSuccess(true);
        pingResponseJson.put("launchkey_time", "2000-01-01 00:01:00");
        String message = "{\"launchkey_time\": \"2000-01-01 00:00:00\", \"user_hash\": \"userHash\"}";
        String signature = "Q+DQatq/Dn92mkkfeZasmjnp9f7IzJMKufPY+zKnahTa+PuMO3MDc2VCOoLxKWGHB5gTlLjb4v/Nb0ZwWU+o4wapTmbRCrOtRG0ydhzL3S16iInnThC7KnfImUHNY8f6zQC/yzMT9U0POTKgQynOSGq8G4D1ByZS1nAUCmJPiEAenIKDptjlv5r9cKLLO8m8TNzPsO2x7awbqSliBWSjy1U6woqn/mCStHQE2h7ay4F5N1IfVr0FZd/IMRWmobffhX767G8RHIG9BMYpz+tJkWOcVz8k3vTDAOp3EunNDr4dxDtN6c8xw6eDh76/fdjwsF94jJ2gh/sHtBXB/vs7eA==";
        String actual = authenticationManager.deorbit(message, signature);
        assertEquals("userHash", actual);
    }

    @Test(expected = AuthenticationException.class)
    public void testIsAuthorizedThrowsAuthenticationExceptionWhenPingFails() throws Exception {
        pingResponse.setSuccess(false);
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
    }

    @Test
    public void testIsAuthorizedReturnsTrueWhenPollSuccess() throws Exception {
        boolean actual = authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        assertTrue(actual);
    }

    @Test
    public void testIsAuthorizedReturnsFalseWhenPollSuccessIsFalse() throws Exception{
        pollResponse.setSuccess(false);
        boolean actual = authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        assertFalse(actual);
    }

    @Test
    public void testIsAuthorizedDoesNotLogUserOutWhenPollSuccessIsFalseAndMessageCodeIsNot70404() throws Exception {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70400");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController, never()).logsPut(anyString(), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testIsAuthorizedLogsUserOutWhenPollSuccessIsFalseAndMessageCodeIs70404() throws Exception {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(anyString(), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testIsAuthorizedPassesCorrectAuthRequestToPollGet() throws AuthenticationException {
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).pollGet(anyString(), anyString(), eq(AUTH_REQUEST));
    }

    @Test
    public void testIsAuthorizedPassesCorrectLaunchKeyTimeToPollGet() throws AuthenticationException {
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).pollGet(eq(LAUNCHKEY_TIME), anyString(), anyString());
    }

    @Test
    public void testIsAuthorizedPassesCorrectPublicKeyToPollGet() throws AuthenticationException {
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).pollGet(anyString(), eq(PUBLIC_KEY), anyString());
    }

    @Test
    public void testIsAuthorizedPassesCorrectAuthRequestToLogsPut() throws AuthenticationException {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(eq(AUTH_REQUEST), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testIsAuthorizedPassesCorrectLaunchKeyTimeToLogsPut() throws AuthenticationException {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(anyString(), eq(LAUNCHKEY_TIME), anyString(), anyString(), anyBoolean());
    }

    @Test
    public void testIsAuthorizedPassesCorrectPublicKeyToLogsPut() throws AuthenticationException {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(anyString(), anyString(), eq(PUBLIC_KEY), anyString(), anyBoolean());
    }

    @Test
    public void testIsAuthorizedPassesCorrectActionToLogsPut() throws AuthenticationException {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(anyString(), anyString(), anyString(), eq("Revoke"), anyBoolean());
    }

    @Test
    public void testIsAuthorizedPassesCorrectStatusToLogsPut() throws AuthenticationException {
        pollResponse.setSuccess(false);
        pollResponseJson.put("message_code", "70404");
        authenticationManager.isAuthorized(AUTH_REQUEST, LAUNCHKEY_TIME);
        verify(authController).logsPut(anyString(), anyString(), anyString(), anyString(), eq(true));
    }

    @Test
    public void testCreateWhiteLabelUserSendsLaunchKeyTimeFromPingToAuthController() throws Exception {
        authenticationManager.createWhiteLabelUser("expected");
        verify(authController).usersPost(eq(LAUNCHKEY_TIME), anyString(), anyString());
    }

    @Test
    public void testCreateWhiteLabelUserSendsPublicKeyFromPingToAuthController() throws Exception {
        authenticationManager.createWhiteLabelUser("expected");
        verify(authController).usersPost(anyString(), eq(PUBLIC_KEY), anyString());
    }

    @Test
    public void testCreateWhiteLabelUserSendsIdentifierToAuthController() throws Exception {
        authenticationManager.createWhiteLabelUser("expected");
        verify(authController).usersPost(anyString(), anyString(), eq("expected"));
    }

    @Test(expected = UserCreationException.class)
    public void testCreateWhiteLabelThrowsUserCreationExceptionWhenErrorResponseIsReturnedFromPing() throws Exception {
        pingResponse.setSuccess(false);
        authenticationManager.createWhiteLabelUser("user");
    }

    @Test
    public void testCreateWhiteLabelThrowsUserCreationExceptionWithProperErrorCodeWhenErrorResponseIsReturnedFromPing() throws Exception {
        pingResponse.setSuccess(false);
        pingResponse.getJson().put("message_code", "1111");
        try {
            authenticationManager.createWhiteLabelUser("user");
            fail("UserCreationException expected but not thrown");
        } catch (UserCreationException e) {
            assertEquals("1111", e.getCode());
        }
    }

    @Test
    public void testCreateWhiteLabelThrowsUserCreationExceptionWithProperErrorMessageWhenErrorResponseIsReturnedFromPing() throws Exception {
        pingResponse.setSuccess(false);
        pingResponse.getJson().put("message", "expected");
        try {
            authenticationManager.createWhiteLabelUser("user");
            fail("UserCreationException expected but not thrown");
        } catch (UserCreationException e) {
            assertEquals("expected", e.getMessage());
        }
    }

    @Test(expected = UserCreationException.class)
    public void testCreateWhiteLabelThrowsUserCreationExceptionWhenErrorResponseIsReturnedFromUsersPost() throws Exception {
        usersPostResponse.setSuccess(false);
        authenticationManager.createWhiteLabelUser("user");
    }

    @Test
    public void testCreateWhiteLabelThrowsUserCreationExceptionWithProperErrorCodeWhenErrorResponseIsReturnedFromUsersPost() throws Exception {
        usersPostResponse.setSuccess(false);
        usersPostResponse.getJson().put("message_code", "1111");
        try {
            authenticationManager.createWhiteLabelUser("user");
            fail("UserCreationException expected but not thrown");
        } catch (UserCreationException e) {
            assertEquals("1111", e.getCode());
        }
    }

    @Test
    public void testCreateWhiteLabelThrowsUserCreationExceptionWithProperErrorMessageWhenErrorResponseIsReturnedFromUsersPost() throws Exception {
        usersPostResponse.setSuccess(false);
        usersPostResponse.getJson().put("message", "expected");
        try {
            authenticationManager.createWhiteLabelUser("user");
            fail("UserCreationException expected but not thrown");
        } catch (UserCreationException e) {
            assertEquals("expected", e.getMessage());
        }
    }

    @Test
    public void testCreateWhiteLabelReturnsQrCodeUrlInResult() throws Exception {
        usersPostResponseJson.put("qrcode", "expected");
        usersPostResponse.getJson().put("response", usersPostResponseJson);
        WhiteLabelUserCreateResult actual = authenticationManager.createWhiteLabelUser("user");
        assertEquals("expected", actual.getQrCodeUrl());
    }

    @Test
    public void testCreateWhiteLabelReturnsManualCodeInResult() throws Exception {
        usersPostResponseJson.put("code", "expected");
        usersPostResponse.getJson().put("response", usersPostResponseJson);
        WhiteLabelUserCreateResult actual = authenticationManager.createWhiteLabelUser("user");
        assertEquals("expected", actual.getCode());
    }
}