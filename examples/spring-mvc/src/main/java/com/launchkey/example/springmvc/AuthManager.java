/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.example.springmvc;

import com.launchkey.sdk.BasicClient;
import com.launchkey.sdk.Client;
import com.launchkey.sdk.service.auth.*;
import com.launchkey.sdk.service.error.ApiException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.naming.ConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthManager {
    Logger log = LoggerFactory.getLogger(getClass());
    private final AuthService authService;
    private final Map<String, String> sessionAuthRequestMap;
    private final Map<String, Boolean> sessionAuthenticationMap;
    private final Map<String, List<String>> userHashSessionMap;

    @Autowired
    public AuthManager(PlatformSdkConfig config) throws ConfigurationException, IOException {
        final Long appKey = config.getAppKey();
        final String secretKey = config.getSecretKey();
        final String privateKeyLocation = config.getPrivateKeyLocation();

        boolean halt = false;
        if (appKey == null) {
            log.error("mfa.app-key property not provided");
            halt = true;
        }
        if (secretKey == null) {
            log.error("mfa.secret-key property not provided");
            halt = true;
        }
        if (privateKeyLocation == null) {
            log.error("mfa.private-key-location property not provided");
            halt = true;
        }
        if (halt) throw new ConfigurationException("Missing required mfa configuration");

        BufferedReader br = new BufferedReader(new FileReader(privateKeyLocation));
        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        String privateKey = sb.toString();
        Client client = BasicClient.factory(
                appKey,
                secretKey,
                privateKey,
                new BouncyCastleProvider()
        );
        this.authService = client.auth();
        this.sessionAuthenticationMap = Collections.synchronizedMap(new HashMap<String, Boolean>());
        this.sessionAuthRequestMap = new ConcurrentHashMap<String, String>();
        this.userHashSessionMap = new ConcurrentHashMap<String, List<String>>();
    }

    public void login(String username, String context) throws AuthException {
        try {
            String authRequestId = this.authService.login(username, context);
            String sessionId = getSessionId();
            sessionAuthRequestMap.put(sessionId, authRequestId);
            sessionAuthenticationMap.put(sessionId, null);
        } catch (ApiException apiException) {
            throw new AuthException("Error logging in with username: " + username, apiException);
        }
    }

    public Boolean isAuthorized() throws AuthException {
        String sessionId = getSessionId();
        if (sessionAuthenticationMap.containsKey(sessionId)) {
            return sessionAuthenticationMap.get(sessionId);
        } else {
            throw new AuthException("No auth request found for this session");
        }
    }

    public void logout(String sessionId) throws AuthException {
        if (true == sessionAuthenticationMap.get(sessionId)) {
            sessionAuthenticationMap.put(sessionId, false);
            if (sessionAuthRequestMap.containsKey(sessionId)) {
                try {
                    authService.logout(sessionAuthRequestMap.get(sessionId));
                    sessionAuthenticationMap.put(sessionId, false);
                    sessionAuthRequestMap.remove(sessionId);
                } catch (ApiException apiException) {
                    throw new AuthException(
                            "Error on logout for auth request: " + sessionAuthRequestMap.get(sessionId),
                            apiException
                    );
                }
            } else {
                throw new AuthException("No auth request found for this session");
            }
        }
    }

    public void handleCallback(Map<String, String> callbackData) throws AuthException {
        try {
            CallbackResponse callbackResponse = authService.handleCallback(callbackData, 300);
            if (callbackResponse instanceof AuthResponseCallbackResponse) {
                AuthResponse authResponse = ((AuthResponseCallbackResponse) callbackResponse).getAuthResponse();
                String authRequestId = authResponse.getAuthRequestId();
                String sessionId = null;
                for (Map.Entry<String, String> entry : sessionAuthRequestMap.entrySet()) {
                    if (entry.getValue().equals(authRequestId)) {
                        sessionId = entry.getKey();
                        break;
                    }
                }
                if (null == sessionId) {
                    throw new AuthException("No session found for auth request: " + authRequestId);
                }
                sessionAuthenticationMap.put(sessionId, authResponse.isAuthorized());
                List<String> sessionList = userHashSessionMap.get(authResponse.getUserHash());

                if (null == sessionList) { // If no session list exists for the user hash, create one in the map
                    sessionList = Collections.synchronizedList(new ArrayList<String>());
                    userHashSessionMap.put(authResponse.getUserHash(), sessionList);
                }

                // If the session does not already exist in the session list add it
                if (!sessionList.contains(sessionId)) {
                    sessionList.add(sessionId);
                }
            } else if (callbackResponse instanceof DeOrbitCallbackResponse) {
                String userHash = ((DeOrbitCallbackResponse) callbackResponse).getUserHash();
                for (String sessionId : userHashSessionMap.get(userHash)) {
                    logout(sessionId);
                }
                userHashSessionMap.remove(userHash);
            }
        } catch (ApiException apiException) {
            throw new AuthException("Error handling callback", apiException);
        }
    }

    public void transitionSession(String oldSessionId, String newSessionId) {
        sessionAuthenticationMap.put(newSessionId, sessionAuthenticationMap.get(oldSessionId));
        sessionAuthenticationMap.remove(oldSessionId);
        sessionAuthRequestMap.put(newSessionId, sessionAuthRequestMap.get(oldSessionId));
        sessionAuthRequestMap.remove(oldSessionId);
        for (Map.Entry<String, List<String>> entry : userHashSessionMap.entrySet()) {
            List<String> sessions = entry.getValue();
            if (sessions.contains(oldSessionId)) {
                sessions.add(newSessionId);
                sessions.remove(oldSessionId);
            }
        }
    }

    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    public class AuthException extends Throwable {
        public AuthException(String message) {
            super(message);
        }

        public AuthException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
