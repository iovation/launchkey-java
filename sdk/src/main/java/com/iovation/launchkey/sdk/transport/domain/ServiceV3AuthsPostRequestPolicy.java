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
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(value = {"geoFenceLocations"})
public class ServiceV3AuthsPostRequestPolicy {
    private final static int ZERO = Integer.valueOf(0);
    private final static int ONE = Integer.valueOf(1);
    private final List<MinimumRequirement> minimumRequirements;
    private final List<Map<String, Double>> geoFenceLocations;

    public ServiceV3AuthsPostRequestPolicy(int any, boolean knowledge, boolean inherence, boolean possession) {
        MinimumRequirement minimumRequirement = new MinimumRequirement(
                MinimumRequirement.Type.AUTHENTICATED,
                any,
                knowledge ? ONE : ZERO,
                inherence ? ONE : ZERO,
                possession ? ONE : ZERO
        );
        minimumRequirements = Arrays.asList(minimumRequirement);

        this.geoFenceLocations = new ArrayList<Map<String, Double>>();
    }

    public void addGeoFence(double radius, double latitude, double longitude) {
        Map<String, Double> location = new LinkedHashMap();
        location.put("radius", radius);
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        geoFenceLocations.add(location);
    }


    @JsonProperty(value = "minimum_requirements")
    public List<MinimumRequirement> getMinimumRequirements() {
        return minimumRequirements;
    }

    @JsonProperty(value = "factors")
    public List<Map<String, Object>> getFactors() {
        List<Map<String, Object>> factors = new ArrayList<Map<String, Object>>();
        if (geoFenceLocations.size() > 0) {
            Map<String, Object> factor = new LinkedHashMap();
            factor.put("factor", "geofence");
            factor.put("requirement", "forced requirement");
            factor.put("priority", Integer.valueOf(1));
            Map<String, Object> attributes = new LinkedHashMap();
            factor.put("attributes", attributes);
            attributes.put("locations", geoFenceLocations);
            factors.add(factor);
        }
        return factors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceV3AuthsPostRequestPolicy)) return false;

        ServiceV3AuthsPostRequestPolicy that = (ServiceV3AuthsPostRequestPolicy) o;

        if (getMinimumRequirements() != null ? !getMinimumRequirements().equals(that.getMinimumRequirements()) : that.getMinimumRequirements() != null)
            return false;
        return geoFenceLocations != null ? geoFenceLocations.equals(that.geoFenceLocations) : that.geoFenceLocations == null;
    }

    @Override
    public int hashCode() {
        int result = getMinimumRequirements() != null ? getMinimumRequirements().hashCode() : 0;
        result = 31 * result + (geoFenceLocations != null ? geoFenceLocations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceV3AuthsPostRequestPolicy{" +
                "minimumRequirements=" + minimumRequirements +
                ", geoFenceLocations=" + geoFenceLocations +
                '}';
    }

    public static class MinimumRequirement {
        private final Type type;
        private final int any;
        private final int knowledge;
        private final int inherence;
        private final int possession;

        public MinimumRequirement(Type type, int any, int knowledge, int inherence, int possession) {
            this.type = type;
            this.any = any;
            this.knowledge = knowledge;
            this.inherence = inherence;
            this.possession = possession;
        }

        @JsonProperty("requirement")
        public Type getType() {
            return type;
        }

        @JsonProperty("any")
        public int getAny() {
            return any;
        }

        @JsonProperty("knowledge")
        public int getKnowledge() {
            return knowledge;
        }

        @JsonProperty("inherence")
        public int getInherence() {
            return inherence;
        }

        @JsonProperty("possession")
        public int getPossession() {
            return possession;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MinimumRequirement)) return false;

            MinimumRequirement that = (MinimumRequirement) o;

            if (any != that.any) return false;
            if (knowledge != that.knowledge) return false;
            if (inherence != that.inherence) return false;
            if (possession != that.possession) return false;
            return type == that.type;

        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + any;
            result = 31 * result + knowledge;
            result = 31 * result + inherence;
            result = 31 * result + possession;
            return result;
        }

        @Override
        public String toString() {
            return "MinimumRequirements{" +
                    "type=" + type +
                    ", any=" + any +
                    ", knowledge=" + knowledge +
                    ", inherence=" + inherence +
                    ", possession=" + possession +
                    '}';
        }

        public enum Type {
            AUTHENTICATED("authenticated"),
            ENABLED("enabled");

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
    }
}
