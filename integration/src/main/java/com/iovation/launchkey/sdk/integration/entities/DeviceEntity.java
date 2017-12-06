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

import java.util.Date;

public class DeviceEntity {
    private final String id;
    private final String name;
    private final int status;
    private final String type;
    private final Date created;
    private final Date updated;

    public DeviceEntity(String id, String name, int status, String type, Date created, Date updated) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.created = created;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }
}
