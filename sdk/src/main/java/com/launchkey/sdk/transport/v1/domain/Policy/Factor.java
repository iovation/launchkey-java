/**
 * Copyright 2016 LaunchKey, Inc.  All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.launchkey.sdk.transport.v1.domain.Policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonPropertyOrder({"factor", "requirement", "quickfail", "priority", "attributes"})
public class Factor {

    private final boolean quickFail;
    private final Requirement requirement;
    private final int priority;
    private final Attributes attributes;
    private final Type type;

    public Factor(
            Type type,
            boolean quickFail,
            Requirement requirement,
            int priority,
            Attributes attributes
    ) {
        this.quickFail = quickFail;
        this.requirement = requirement;
        this.priority = priority;
        this.attributes = attributes;
        this.type = type;
    }

    @JsonProperty("factor")
    public Type getType() {
        return type;
    }

    @JsonProperty("quickfail")
    public boolean isQuickFail() {
        return quickFail;
    }

    @JsonProperty("requirement")
    public Requirement getRequirement() {
        return requirement;
    }

    @JsonProperty("priority")
    public int getPriority() {
        return priority;
    }

    @JsonProperty("attributes")
    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factor)) return false;

        Factor factor = (Factor) o;

        if (getType() != factor.getType()) return false;
        if (isQuickFail() != factor.isQuickFail()) return false;
        if (getPriority() != factor.getPriority()) return false;
        if (getRequirement() != factor.getRequirement()) return false;
        return getAttributes() != null ? getAttributes().equals(factor.getAttributes()) : factor.getAttributes() == null;

    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (isQuickFail() ? 1 : 0);
        result = 31 * result + (getRequirement() != null ? getRequirement().hashCode() : 0);
        result = 31 * result + getPriority();
        result = 31 * result + (getAttributes() != null ? getAttributes().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "quickFail=" + quickFail +
                ", requirement=" + requirement +
                ", priority=" + priority +
                ", attributes=" + attributes +
                ", type=" + type +
                '}';
    }

    public static final class Attributes {
        private final List<Location> locations;

        public Attributes(List<Location> locations) {
            this.locations = Collections.unmodifiableList(new ArrayList<Location>(locations));
        }

        @JsonProperty("locations")
        public List<Location> getLocations() {
            return locations;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Attributes)) return false;

            Attributes that = (Attributes) o;

            return getLocations().equals(that.getLocations());

        }

        @Override public int hashCode() {
            return getLocations() != null ? getLocations().hashCode() : 0;
        }

        @Override public String toString() {
            return "FactorAttributes{" +
                    "locations=" + locations +
                    '}';
        }
    }

    public enum Requirement {
        FORCED("forced requirement"),
        ALLOWED("allowed");

        private final String value;

        Requirement(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    public enum Type {
        COMBO_LOCK("combo lock"),
        PIN_LOCK("pin lock"),
        DEVICE("device factor"),
        GEOFENCE("geofence");

        private final String value;

        Type(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    public enum Category {
        KNOWLEDGE("knowledge"),
        INHERENCE("inherence"),
        POSSESSION("possession");

        private final String value;

        Category(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    public static final class Location {
        private final double radius;
        private final double latitude;
        private final double longitude;

        public Location(double radius, double latitude, double longitude) {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getRadius() {
            return radius;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Location)) return false;

            Location location = (Location) o;

            if (Double.compare(location.radius, radius) != 0) return false;
            if (Double.compare(location.latitude, latitude) != 0) return false;
            return Double.compare(location.longitude, longitude) == 0;
        }

        @Override public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(radius);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(latitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(longitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
}

