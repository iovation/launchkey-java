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

import java.util.EnumSet;
import java.util.UUID;

public class EntityIdentifier {
    private final EntityType type;
    private final UUID id;

    public EntityIdentifier(EntityType type, UUID id) {
        if (type == null) {
            throw new IllegalArgumentException("type is required");
        }
        this.type = type;
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getValue() + ":" + String.valueOf(id);
    }

    public static EntityIdentifier fromString(String entityString) {
        String[] parts = entityString.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid entity string!");
        }
        return new EntityIdentifier(EntityType.fromValue(parts[0]), UUID.fromString(parts[1]));
    }

    public enum EntityType {
        ORGANIZATION("org"), DIRECTORY("dir"), SERVICE("svc");

        private final String value;

        EntityType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static EntityType fromValue(String value) {
            for (EntityType type : EnumSet.allOf(EntityType.class)) {
                if (type.getValue().equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid value provided.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityIdentifier)) return false;

        EntityIdentifier that = (EntityIdentifier) o;

        if (type != that.type) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
