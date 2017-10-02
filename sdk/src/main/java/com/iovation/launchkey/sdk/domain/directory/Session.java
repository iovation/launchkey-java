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

package com.iovation.launchkey.sdk.domain.directory;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

/**
 * Session information
 */
public class Session {
    private final UUID serviceId;
    private final String serviceName;
    private final URI serviceIcon;
    private final UUID authRequest;
    private final Date created;

    /**
     * Create a Session
     * @param serviceId ID of the Session's Service
     * @param serviceName Name of the Session's Service
     * @param serviceIcon Icon of the Session's Service
     * @param authRequest Auth Request that approved the Session
     * @param created Date the Session was created
     */
    public Session(UUID serviceId, String serviceName, URI serviceIcon, UUID authRequest, Date created) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceIcon = serviceIcon;
        this.authRequest = authRequest;
        this.created = created;
    }

    /**
     * Get the ID of the Session Service
     * @return Service ID
     */
    public UUID getServiceId() {
        return serviceId;
    }

    /**
     * Get the Name of the Session Service
     * @return Service name
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Get the icon of the Session Service
     * @return Service icon
     */
    public URI getServiceIcon() {
        return serviceIcon;
    }

    /**
     * Get the Auth Request that approved the Session. This may be null as it is not required for creating a session.
     * @return Auth Request
     */
    public UUID getAuthRequest() {
        return authRequest;
    }

    /**
     * Get the Date the Session was created
     * @return Session creation date
     */
    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;

        Session session = (Session) o;

        if (serviceId != null ? !serviceId.equals(session.serviceId) : session.serviceId != null) return false;
        if (serviceName != null ? !serviceName.equals(session.serviceName) : session.serviceName != null) return false;
        if (serviceIcon != null ? !serviceIcon.equals(session.serviceIcon) : session.serviceIcon != null) return false;
        if (authRequest != null ? !authRequest.equals(session.authRequest) : session.authRequest != null) return false;
        return created != null ? created.equals(session.created) : session.created == null;
    }

    @Override
    public int hashCode() {
        int result = serviceId != null ? serviceId.hashCode() : 0;
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (serviceIcon != null ? serviceIcon.hashCode() : 0);
        result = 31 * result + (authRequest != null ? authRequest.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Session{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", serviceIcon=" + serviceIcon +
                ", authRequest=" + authRequest +
                ", created=" + created +
                '}';
    }
}
