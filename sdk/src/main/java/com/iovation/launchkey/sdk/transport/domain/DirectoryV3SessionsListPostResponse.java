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

package com.iovation.launchkey.sdk.transport.domain;

import java.util.List;

public class DirectoryV3SessionsListPostResponse {
    private final List<DirectoryV3SessionsListPostResponseSession> sessions;

    public DirectoryV3SessionsListPostResponse(
            List<DirectoryV3SessionsListPostResponseSession> sessions) {
        this.sessions = sessions;
    }

    public List<DirectoryV3SessionsListPostResponseSession> getSessions() {
        return sessions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3SessionsListPostResponse)) return false;

        DirectoryV3SessionsListPostResponse that = (DirectoryV3SessionsListPostResponse) o;

        return sessions != null ? sessions.equals(that.sessions) : that.sessions == null;
    }

    @Override
    public int hashCode() {
        return sessions != null ? sessions.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DirectoryV3SessionsListPostResponse{" +
                "sessions=" + sessions +
                '}';
    }
}
