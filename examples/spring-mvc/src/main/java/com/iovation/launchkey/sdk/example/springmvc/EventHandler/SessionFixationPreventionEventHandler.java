package com.iovation.launchkey.sdk.example.springmvc.EventHandler;

import com.iovation.launchkey.sdk.example.springmvc.AuthManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.authentication.session.SessionFixationProtectionEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionFixationPreventionEventHandler implements ApplicationListener<SessionFixationProtectionEvent> {
    private final AuthManager authManager;

    @Autowired
    public SessionFixationPreventionEventHandler(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override public void onApplicationEvent(SessionFixationProtectionEvent event) {
        authManager.transitionSession(event.getOldSessionId(), event.getNewSessionId());
    }
}
