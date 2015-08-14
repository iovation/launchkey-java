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

package com.launchkey.example.springmvc.EventHandler;

import com.launchkey.example.springmvc.AuthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SessionDestroyedEventHandler implements ApplicationListener<SessionDestroyedEvent>, LogoutSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AuthManager authManager;

    @Autowired
    public SessionDestroyedEventHandler(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override public void onApplicationEvent(SessionDestroyedEvent event) {
        logout(event.getId());
    }

    @Override public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {
        logout(request.getRequestedSessionId());
    }

    private void logout(String sessionId) {
        try {
            authManager.logout(sessionId);
        } catch (AuthManager.AuthException e) {
            logger.debug("Unable to logout on session destroyed event", e);
        }
    }
}
