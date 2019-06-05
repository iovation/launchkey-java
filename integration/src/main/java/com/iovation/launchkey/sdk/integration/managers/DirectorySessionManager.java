/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.integration.managers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.domain.directory.Session;
import com.iovation.launchkey.sdk.integration.entities.SessionEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class DirectorySessionManager {

    private final List<SessionEntity> currentSessionList = new ArrayList<>();
    private final DirectoryManager directoryManager;

    @Inject
    public DirectorySessionManager(DirectoryManager directoryManager) {
        this.directoryManager = directoryManager;
    }

    public void retrieveSessionList(String userIdentifier) throws Throwable {
        List<SessionEntity> sessionList = new ArrayList<>();
        for (Session session : directoryManager.getDirectoryClient().getAllServiceSessions(userIdentifier)) {
            sessionList.add(SessionEntity.fromSession(session));
        }
        currentSessionList.clear();
        currentSessionList.addAll(sessionList);
    }

    public List<SessionEntity> getCurrentSessionList() {
        return currentSessionList;
    }

    public void endAllSessionsForUser(String userIdentifier) throws Throwable {
        directoryManager.getDirectoryClient().endAllServiceSessions(userIdentifier);
    }
}
