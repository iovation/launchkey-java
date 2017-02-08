package com.launchkey.example.springmvc;

import com.launchkey.sdk.ServiceClient;
import com.launchkey.sdk.ClientFactoryBuilder;
import com.launchkey.sdk.domain.service.AuthorizationResponse;
import com.launchkey.sdk.domain.sse.AuthorizationResponseServerSentEventPackage;
import com.launchkey.sdk.domain.sse.ServerSentEventPackage;
import com.launchkey.sdk.domain.sse.ServiceUserSessionServerSentEventPackage;
import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.service.ServiceService;
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
    final Logger log = LoggerFactory.getLogger(getClass());
    private final ServiceService serviceService;
    private final Map<String, String> sessionAuthRequestMap;
    private final Map<String, Boolean> sessionAuthenticationMap;
    private final Map<String, List<String>> userHashSessionMap;
    private final Map<String, String> sessionUserNameMap;

    @SuppressWarnings("ThrowFromFinallyBlock")
    @Autowired
    public AuthManager(LaunchkeySdkConfig appConfig) throws ConfigurationException, IOException {
        final String serviceId = appConfig.getServiceId();
        final String privateKeyLocation = appConfig.getPrivateKeyLocation();
        final String baseURL = appConfig.getBaseUrl();

        boolean halt = false;
        if (serviceId == null) {
            log.error("lk.service-id property not provided");
            halt = true;
        }
        if (privateKeyLocation == null) {
            log.error("lk.private-key-location property not provided");
            halt = true;
        }
        if (halt) throw new ConfigurationException("Missing required lk configuration");

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

        ClientFactoryBuilder builder = new ClientFactoryBuilder()
                .setJCEProvider(new BouncyCastleProvider());
        if (baseURL != null) {
            builder.setAPIBaseURL(baseURL);
        }
        ServiceClient client = builder.build().makeServiceClient(serviceId, privateKey);

        serviceService = client.getServiceService();
        sessionAuthenticationMap = Collections.synchronizedMap(new HashMap<String, Boolean>());
        sessionAuthRequestMap = new ConcurrentHashMap<String, String>();
        userHashSessionMap = new ConcurrentHashMap<String, List<String>>();
        sessionUserNameMap = new ConcurrentHashMap<String, String>();
    }

    public void login(String username, String context) throws AuthException {
        try {
            String authRequestId = this.serviceService.authorize(username, context);
            String sessionId = getSessionId();
            sessionAuthRequestMap.put(sessionId, authRequestId);
            sessionUserNameMap.put(sessionId, username);
            sessionAuthenticationMap.put(sessionId, null);
        } catch (BaseException apiException) {
            throw new AuthException("Error logging in with username: " + username, apiException);
        }
    }

    public Boolean isAuthorized() throws AuthException {
        String sessionId = getSessionId();
        if (sessionAuthenticationMap.containsKey(sessionId)) {
            return sessionAuthenticationMap.get(sessionId);
        } else {
            throw new AuthException("No getServiceService request found for this session");
        }
    }

    public void logout(String sessionId) throws AuthException {
        if (sessionAuthenticationMap.get(sessionId)) {
            sessionAuthenticationMap.put(sessionId, false);
            if (sessionUserNameMap.containsKey(sessionId)) {
                try {
                    serviceService.sessionEnd(sessionUserNameMap.get(sessionId));
                    sessionAuthenticationMap.put(sessionId, false);
                    sessionAuthRequestMap.remove(sessionId);
                    sessionUserNameMap.remove(sessionId);
                } catch (BaseException apiException) {
                    throw new AuthException(
                            "Error on logout for getServiceService request: " + sessionAuthRequestMap.get(sessionId),
                            apiException
                    );
                }
            } else {
                throw new AuthException("No getServiceService request found for this session");
            }
        }
    }

    public void handleCallback(Map<String, List<String>> headers, String body) throws AuthException {
        try {
            ServerSentEventPackage serverSentEventPackage = serviceService.handleServerSentEvent(headers, body);
            if (serverSentEventPackage instanceof AuthorizationResponseServerSentEventPackage) {
                AuthorizationResponse authorizationResponse = ((AuthorizationResponseServerSentEventPackage) serverSentEventPackage).getAuthorizationResponse();
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
                serviceService.sessionStart(sessionUserNameMap.get(sessionId));
            } else if (serverSentEventPackage instanceof ServiceUserSessionServerSentEventPackage) {
                String userHash = ((ServiceUserSessionServerSentEventPackage) serverSentEventPackage).getServiceUserHash();
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
