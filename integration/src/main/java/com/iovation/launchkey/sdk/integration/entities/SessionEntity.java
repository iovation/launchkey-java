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

package com.iovation.launchkey.sdk.integration.entities;

import com.iovation.launchkey.sdk.domain.directory.Session;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

public class SessionEntity {
    private final UUID serviceId;
    private final UUID authRequest;
    private final Date created;
    private final URI serviceIcon;
    private final String serviceName;

    public SessionEntity(UUID serviceId, UUID authRequest, Date created, URI serviceIcon,
                         String serviceName) {
        this.serviceId = serviceId;
        this.authRequest = authRequest;
        this.created = created;
        this.serviceIcon = serviceIcon;
        this.serviceName = serviceName;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public UUID getAuthRequest() {
        return authRequest;
    }

    public Date getCreated() {
        return created;
    }

    public URI getServiceIcon() {
        return serviceIcon;
    }

    public String getServiceName() {
        return serviceName;
    }

    public static SessionEntity fromSession(Session session) {
        return new SessionEntity(session.getServiceId(), session.getAuthRequest(), session.getCreated(),
                session.getServiceIcon(), session.getServiceName());
    }
}
