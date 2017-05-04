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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectoryV3DevicesListPostResponseDevice {
    private final UUID id;
    private final String name;
    private final String type;
    private final int status;
    private final Date created;
    private final Date updated;

    public DirectoryV3DevicesListPostResponseDevice(
            @JsonProperty(value = "id") UUID id,
            @JsonProperty(value = "name") String name,
            @JsonProperty(value = "type") String type,
            @JsonProperty(value = "status") int status,
            @JsonProperty(value = "created") Date created,
            @JsonProperty(value = "updated") Date updated
    ) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryV3DevicesListPostResponseDevice)) return false;

        DirectoryV3DevicesListPostResponseDevice that = (DirectoryV3DevicesListPostResponseDevice) o;

        if (getStatus() != that.getStatus()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getCreated() != null ? !getCreated().equals(that.getCreated()) : that.getCreated() != null) return false;
        return getUpdated() != null ? getUpdated().equals(that.getUpdated()) : that.getUpdated() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + getStatus();
        result = 31 * result + (getCreated() != null ? getCreated().hashCode() : 0);
        result = 31 * result + (getUpdated() != null ? getUpdated().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DirectoryV3DevicesListPostResponseDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
