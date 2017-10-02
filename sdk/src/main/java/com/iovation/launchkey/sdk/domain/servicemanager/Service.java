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

package com.iovation.launchkey.sdk.domain.servicemanager;


import java.net.URI;
import java.util.UUID;

@SuppressWarnings("WeakerAccess")
public class Service {
    private final UUID id;
    private final String name;
    private final String description;
    private final java.net.URI icon;
    private final URI callbackURL;
    private final boolean active;

    /**
     * Create a Service entity
     *
     * @param id Unique identifier for the Service
     * @param name Name of the Service
     * @param description Description of the Service
     * @param icon Icon to display for the Service
     * @param callbackURL Callback URL for the webhooks generated for the Service
     * @param active Flag determining if the Service is currently active
     */
    public Service(UUID id, String name, String description, URI icon, URI callbackURL, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.callbackURL = callbackURL;
        this.active = active;
    }

    /**
     * Get the unique identifier for the Service
     *
     * @return Unique identifier for the Service
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get the name of the Service
     *
     * @return Name of the Service
     */
    public String getName() {
        return name;
    }

    /**
     * Get the description of the Service
     *
     * @return Description of the Service
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the icon to display for the Service
     *
     * @return Icon to display for the Service
     */
    public URI getIcon() {
        return icon;
    }

    /**
     * Get the callback URL for the webhooks generated for the Service
     *
     * @return Callback URL for the webhooks generated for the Service
     */
    public URI getCallbackURL() {
        return callbackURL;
    }

    /**
     * Is the Service is currently active?
     *
     * @return Flag determining if the Service is currently active
     */
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service)) return false;

        Service service = (Service) o;

        if (active != service.active) return false;
        if (id != null ? !id.equals(service.id) : service.id != null) return false;
        if (name != null ? !name.equals(service.name) : service.name != null) return false;
        if (description != null ? !description.equals(service.description) : service.description != null) return false;
        if (icon != null ? !icon.equals(service.icon) : service.icon != null) return false;
        return callbackURL != null ? callbackURL.equals(service.callbackURL) : service.callbackURL == null;
    }

    @Override
    public int hashCode() {
        return 33 * getClass().hashCode() + (getId() != null ? getId().hashCode() : 0);
    }
}
