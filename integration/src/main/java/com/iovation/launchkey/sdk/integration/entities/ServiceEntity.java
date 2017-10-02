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

import com.iovation.launchkey.sdk.domain.servicemanager.Service;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ServiceEntity {
    private final UUID id;
    private final String name;
    private final String description;
    private final java.net.URI icon;
    private final URI callbackURL;
    private final Boolean active;
    private final List<PublicKeyEntity> publicKeys;

    public ServiceEntity(UUID id, String name, String description, URI icon, URI callbackURL, Boolean active) {
        if (id == null) throw new IllegalArgumentException("Argument \"id\" cannot be null.");
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.callbackURL = callbackURL;
        this.active = active;
        publicKeys = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public URI getIcon() {
        return icon;
    }

    public URI getCallbackURL() {
        return callbackURL;
    }

    public Boolean getActive() {
        return active;
    }

    public List<PublicKeyEntity> getPublicKeys() {
        return publicKeys;
    }

    public static ServiceEntity fromService(Service service) {
        return new ServiceEntity(service.getId(), service.getName(), service.getDescription(),
                service.getIcon(), service.getCallbackURL(), service.isActive());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceEntity)) return false;

        ServiceEntity that = (ServiceEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (icon != null ? !icon.equals(that.icon) : that.icon != null) return false;
        if (callbackURL != null ? !callbackURL.equals(that.callbackURL) : that.callbackURL != null) return false;
        return active != null ? active.equals(that.active) : that.active == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (callbackURL != null ? callbackURL.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon=" + icon +
                ", callbackURL=" + callbackURL +
                ", active=" + active +
                '}';
    }
}
