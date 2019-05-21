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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(value = {"geoFenceLocations", "geoFences", "deviceIntegrity"}, ignoreUnknown = true)
@JsonPropertyOrder({"minimum_requirements", "factors"})
public class AuthPolicy {
    private final static int ZERO = 0;
    private final static int ONE = 1;
    private final List<MinimumRequirement> minimumRequirements;
    private final List<Location> geoFenceLocations;
    private final Boolean deviceIntegrity;


    @Deprecated
    public AuthPolicy(Integer any, Boolean knowledge, Boolean inherence, Boolean possession) {
        this(any, knowledge, inherence, possession, null);
    }

    public AuthPolicy(Integer any, Boolean inherence, Boolean knowledge, Boolean possession, Boolean deviceIntegrity) {
        minimumRequirements = new ArrayList<>();
        if (any != null || knowledge != null || inherence != null || possession != null) {
            MinimumRequirement minimumRequirement = new MinimumRequirement(
                    MinimumRequirement.Type.AUTHENTICATED,
                    any,
                    knowledge == null ? null : knowledge.equals(Boolean.FALSE) ? ZERO : ONE,
                    inherence == null ? null : inherence.equals(Boolean.FALSE) ? ZERO : ONE,
                    possession == null ? null : possession.equals(Boolean.FALSE) ? ZERO : ONE
            );
            minimumRequirements.add(minimumRequirement);
        }
        geoFenceLocations = new ArrayList<>();
        this.deviceIntegrity = deviceIntegrity;
    }

    @JsonCreator
    public AuthPolicy(@JsonProperty("minimum_requirements") List<MinimumRequirement> minimumRequirements,
                      @JsonProperty("factors") ArrayNode factors, @JsonProperty("types") List<String> types,
                      @JsonProperty("amount") Integer amount,
                      @JsonProperty("geofences") List<AuthPolicy.Location> geofences) {
        if (minimumRequirements == null) {
            geoFenceLocations = geofences;
            deviceIntegrity = null;
            Integer knowledge;
            Integer possession;
            Integer inherence;
            if (types == null) {
                knowledge = null;
                possession = null;
                inherence = null;
            } else {
                knowledge = types.contains("knowledge") ? ONE : ZERO;
                possession = types.contains("possession") ? ONE : ZERO;
                inherence = types.contains("inherence") ? ONE : ZERO;
            }
            this.minimumRequirements = new ArrayList<>();
            MinimumRequirement minimumRequirement = new MinimumRequirement(
                    MinimumRequirement.Type.AUTHENTICATED,
                    amount,
                    knowledge,
                    inherence,
                    possession
            );
            this.minimumRequirements.add(minimumRequirement);


        } else{
            this.minimumRequirements = minimumRequirements;
            geoFenceLocations = new ArrayList<>();
            Boolean deviceIntegrity = null;
            try {
                for (JsonNode factor : factors) {
                    final String factorName = factor.get("factor").asText();
                    final JsonNode attributes = factor.get("attributes");
                    if (factorName.equals("geofence")) {
                        for (JsonNode location : attributes.get("locations")) {
                            String name = location.has("name") ? location.get("name").asText() : null;
                            addGeoFence(
                                    name,
                                    location.get("radius").asDouble(),
                                    location.get("latitude").asDouble(),
                                    location.get("longitude").asDouble());
                        }
                    } else if (factorName.equals("device integrity")) {
                        deviceIntegrity = attributes.get("factor enabled").asInt() == 1;
                    }
                }
                this.deviceIntegrity = deviceIntegrity;
            } catch (ClassCastException | NullPointerException e) {
                throw new IllegalArgumentException("The argument \"factors\" does not conform to the specification", e);
            }
        }
    }

    public void addGeoFence(double radius, double latitude, double longitude) {
        addGeoFence(null, radius, latitude, longitude);
    }

    public void addGeoFence(String name, double radius, double latitude, double longitude) {
        geoFenceLocations.add(new Location(name, radius, latitude, longitude));
    }

    public List<Location> getGeoFences() {
        return geoFenceLocations;
    }

    @JsonProperty(value = "minimum_requirements")
    public List<MinimumRequirement> getMinimumRequirements() {
        return minimumRequirements;
    }

    @JsonProperty(value = "factors")
    public List<Map<String, Object>> getFactors() {
        List<Map<String, Object>> factors = new ArrayList<>();
        if (geoFenceLocations.size() > 0) {
            Map<String, Object> factor = new LinkedHashMap<>();
            factor.put("factor", "geofence");
            factor.put("requirement", "forced requirement");
            factor.put("priority", 1);
            Map<String, Object> attributes = new LinkedHashMap<>();
            factor.put("attributes", attributes);
            attributes.put("locations", geoFenceLocations);
            factors.add(factor);
        }
        if (deviceIntegrity != null && deviceIntegrity) {
            Map<String, Object> factor = new LinkedHashMap<>();
            factor.put("factor", "device integrity");
            factor.put("requirement", "forced requirement");
            factor.put("priority", 1);
            Map<String, Object> attributes = new LinkedHashMap<>();
            factor.put("attributes", attributes);
            attributes.put("factor enabled", 1);
            factors.add(factor);
        }
        return factors;
    }

    public Boolean getDeviceIntegrity() {
        return deviceIntegrity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthPolicy)) return false;
        AuthPolicy that = (AuthPolicy) o;
        return Objects.equals(getMinimumRequirements(), that.getMinimumRequirements()) &&
                Objects.equals(geoFenceLocations, that.geoFenceLocations) &&
                Objects.equals(getDeviceIntegrity(), that.getDeviceIntegrity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMinimumRequirements(), geoFenceLocations, getDeviceIntegrity());
    }

    @Override
    public String toString() {
        return "AuthPolicy{" +
                "minimumRequirements=" + minimumRequirements +
                ", geoFenceLocations=" + geoFenceLocations +
                ", deviceIntegrity=" + deviceIntegrity +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MinimumRequirement {
        private final Type type;
        private final Integer any;
        private final Integer knowledge;
        private final Integer inherence;
        private final Integer possession;

        @JsonCreator
        public MinimumRequirement(@JsonProperty("requirement") Type type, @JsonProperty("any") Integer any,
                                  @JsonProperty("knowledge") Integer knowledge,
                                  @JsonProperty("inherence") Integer inherence,
                                  @JsonProperty("possession") Integer possession) {
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
        public Integer getAny() {
            return any;
        }

        @JsonProperty("knowledge")
        public Integer getKnowledge() {
            return knowledge;
        }

        @JsonProperty("inherence")
        public Integer getInherence() {
            return inherence;
        }

        @JsonProperty("possession")
        public Integer getPossession() {
            return possession;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MinimumRequirement)) return false;

            MinimumRequirement that = (MinimumRequirement) o;

            if (type != that.type) return false;
            if (any != null ? !any.equals(that.any) : that.any != null) return false;
            if (knowledge != null ? !knowledge.equals(that.knowledge) : that.knowledge != null) return false;
            if (inherence != null ? !inherence.equals(that.inherence) : that.inherence != null) return false;
            return possession != null ? possession.equals(that.possession) : that.possession == null;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + (any != null ? any.hashCode() : 0);
            result = 31 * result + (knowledge != null ? knowledge.hashCode() : 0);
            result = 31 * result + (inherence != null ? inherence.hashCode() : 0);
            result = 31 * result + (possession != null ? possession.hashCode() : 0);
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


    @JsonPropertyOrder({"name", "radius", "latitude", "longitude"})
    @JsonInclude(NON_NULL)
    public static class Location {
        private final String name;
        private final double radius;
        private final double latitude;
        private final double longitude;

        @JsonCreator
        public Location(@JsonProperty("name") String name, @JsonProperty("radius") double radius,
                        @JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
            this.name = name;
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("radius")
        public double getRadius() {
            return radius;
        }

        @JsonProperty("latitude")
        public double getLatitude() {
            return latitude;
        }

        @JsonProperty("longitude")
        public double getLongitude() {
            return longitude;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Location)) return false;

            Location location = (Location) o;

            if (Double.compare(location.radius, radius) != 0) return false;
            if (Double.compare(location.latitude, latitude) != 0) return false;
            if (Double.compare(location.longitude, longitude) != 0) return false;
            return name != null ? name.equals(location.name) : location.name == null;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "name='" + name + '\'' +
                    ", radius=" + radius +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }
}
