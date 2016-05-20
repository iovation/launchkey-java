/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
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
