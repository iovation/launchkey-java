package com.iovation.launchkey.sdk.example.springmvc;

import com.iovation.launchkey.sdk.FactoryFactoryBuilder;
import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.domain.service.AuthorizationRequest;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.domain.webhook.AuthorizationResponseWebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.WebhookPackage;
import com.iovation.launchkey.sdk.domain.webhook.ServiceUserSessionEndWebhookPackage;
import com.iovation.launchkey.sdk.error.BaseException;
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
    private final ServiceClient serviceClient;
    private final Map<String, String> sessionAuthRequestMap;
    private final Map<String, Boolean> sessionAuthenticationMap;
    private final Map<String, List<String>> userHashSessionMap;
    private final Map<String, String> sessionUsernameMap;

    @SuppressWarnings("ThrowFromFinallyBlock")
    @Autowired
    public AuthManager(LaunchkeySdkConfig appConfig) throws ConfigurationException, IOException {
        final String serviceId = appConfig.getServiceId();
        final String privateKeyLocation = appConfig.getPrivateKeyLocation();
        final String baseURL = appConfig.getBaseUrl();

        boolean halt = false;
        Logger log = LoggerFactory.getLogger(getClass());
        if (serviceId == null) {
            log.error("lk.service-id property not provided");
            halt = true;
        }
        if (privateKeyLocation == null) {
            log.error("lk.private-key-location property not provided");
            halt = true;
        }
        if (halt) throw new ConfigurationException("Missing required lk configuration");

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(privateKeyLocation))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        }
        String privateKey = sb.toString();

        FactoryFactoryBuilder builder = new FactoryFactoryBuilder()
                .setJCEProvider(new BouncyCastleProvider());
        if (baseURL != null) {
            builder.setAPIBaseURL(baseURL);
        }
        ServiceFactory factory = builder.build().makeServiceFactory(serviceId, privateKey);

        serviceClient = factory.makeServiceClient();
        sessionAuthenticationMap = Collections.synchronizedMap(new HashMap<String, Boolean>());
        sessionAuthRequestMap = new ConcurrentHashMap<>();
        userHashSessionMap = new ConcurrentHashMap<>();
        sessionUsernameMap = new ConcurrentHashMap<>();
    }

    void login(String username, String context) throws AuthException {
        try {
            AuthorizationRequest authorizationRequest = serviceClient.createAuthorizationRequest(username, context);
            String sessionId = getSessionId();
            sessionAuthRequestMap.put(sessionId, authorizationRequest.getId());
            sessionAuthenticationMap.put(sessionId, null);
            sessionUsernameMap.put(sessionId, username);
        } catch (BaseException apiException) {
            throw new AuthException("Error logging in with username: " + username, apiException);
        }
    }

    Boolean isAuthorized() throws AuthException {
        String sessionId = getSessionId();
        if (sessionAuthenticationMap.containsKey(sessionId)) {
            return sessionAuthenticationMap.get(sessionId);
        } else {
            throw new AuthException("No getServiceService request found for this session");
        }
    }

    public void logout(String sessionId) throws AuthException {
        if (sessionAuthenticationMap.containsKey(sessionId)) {
            sessionAuthenticationMap.put(sessionId, false);
            sessionAuthRequestMap.remove(sessionId);
            if (sessionUsernameMap.containsKey(sessionId)) {
                try {
                    serviceClient.sessionEnd(sessionUsernameMap.get(sessionId));
                    sessionUsernameMap.remove(sessionId);
                } catch (BaseException apiException) {
                    throw new AuthException(
                            "Error on logout for auth request: " + sessionAuthRequestMap.get(sessionId),
                            apiException
                    );
                }
            }
        }
    }

    void handleWebhook(Map<String, List<String>> headers, String body, String method, String path) throws AuthException {
        try {
            WebhookPackage webhookPackage = serviceClient.handleWebhook(headers, body, method, path);
            if (webhookPackage instanceof AuthorizationResponseWebhookPackage) {
                AuthorizationResponse authorizationResponse = ((AuthorizationResponseWebhookPackage) webhookPackage).getAuthorizationResponse();
                String authRequestId = authorizationResponse.getAuthorizationRequestId();
                String sessionId = null;
                for (Map.Entry<String, String> entry : sessionAuthRequestMap.entrySet()) {
                    if (entry.getValue().equals(authRequestId)) {
                        sessionId = entry.getKey();
                        break;
                    }
                }
                if (null == sessionId) {
                    throw new AuthException("No session found for getServiceService request: " + authRequestId);
                }
                sessionAuthenticationMap.put(sessionId, authorizationResponse.isAuthorized());
                List<String> sessionList = userHashSessionMap.get(authorizationResponse.getServiceUserHash());

                if (null == sessionList) { // If no session list exists for the user hash, create one in the map
                    sessionList = Collections.synchronizedList(new ArrayList<String>());
                    userHashSessionMap.put(authorizationResponse.getServiceUserHash(), sessionList);
                }

                // If the session does not already exist in the session list add it
                if (!sessionList.contains(sessionId)) {
                    sessionList.add(sessionId);
                }
                serviceClient.sessionStart(sessionUsernameMap.get(sessionId));
            } else if (webhookPackage instanceof ServiceUserSessionEndWebhookPackage) {
                String userHash = ((ServiceUserSessionEndWebhookPackage) webhookPackage).getServiceUserHash();
                for (String sessionId : userHashSessionMap.get(userHash)) {
                    logout(sessionId);
                }
                userHashSessionMap.remove(userHash);
            }
        } catch (BaseException apiException) {
            throw new AuthException("Error handling callback", apiException);
        }
    }

    public void transitionSession(String oldSessionId, String newSessionId) {
        sessionAuthenticationMap.put(newSessionId, sessionAuthenticationMap.get(oldSessionId));
        sessionAuthenticationMap.remove(oldSessionId);
        sessionAuthRequestMap.put(newSessionId, sessionAuthRequestMap.get(oldSessionId));
        sessionAuthRequestMap.remove(oldSessionId);
        sessionUsernameMap.put(newSessionId, sessionUsernameMap.get(oldSessionId));
        sessionUsernameMap.remove(oldSessionId);
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
