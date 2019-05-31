/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Class to represent an authentication policy which will be utilized for providing dynamic policies per request.
 */
public class AuthPolicy {
    private final Integer requiredFactors;
    private final Boolean requireKnowledgeFactor;
    private final Boolean requireInherenceFactor;
    private final Boolean requirePossessionFactor;
    private final List<Location> locations;
    private final Boolean enableJailbreakProtection;

    /**
     * Create an service policy which only has geofence location requirements.
     *
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(List<Location> locations) {
        this(null, locations);
    }

    /**
     * Create an service which requires a number factors to be used.
     *
     * @param requiredFactors The number of unique factors to require.
     */
    public AuthPolicy(int requiredFactors) {
        this(requiredFactors, new ArrayList<Location>());
    }

    /**
     * Create an service policy that includes the number of factors and geofence locations
     *
     * @param requiredFactors The number of unique factors to require.
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(Integer requiredFactors, List<Location> locations) {
        this(requiredFactors, null, locations);
    }

    /**
     * Create an service policy that includes the number of factors and geofence locations
     *
     * @param requiredFactors The number of unique factors to require.
     * @param enableJailbreakProtection Enable protection against jailbroken or rooted devices responding to an
     *                                  authorization request.
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(Integer requiredFactors, Boolean enableJailbreakProtection, List<Location> locations) {
        this.requiredFactors = requiredFactors;
        this.enableJailbreakProtection = enableJailbreakProtection;
        if (locations == null) {
            this.locations = null;
        } else {
            this.locations = Collections.unmodifiableList(new ArrayList<>(locations));
        }
        requirePossessionFactor = null;
        requireInherenceFactor = null;
        requireKnowledgeFactor = null;
    }

    /**
     * Create an getServiceService policy that determines which getServiceService factors are required.
     *
     * @param requireKnowledgeFactor Is a knowledge factor required
     * @param requireInherenceFactor Is an inherence factor required
     * @param requirePossessionFactor Is a possession factor required
     */
    public AuthPolicy(Boolean requireKnowledgeFactor, Boolean requireInherenceFactor, Boolean requirePossessionFactor) {
        this(requireKnowledgeFactor, requireInherenceFactor, requirePossessionFactor, new ArrayList<Location>());
    }

    /**
     * Create an getServiceService policy that includes determination of which getServiceService factors are required as well as geofence locations.
     *
     * @param requireKnowledgeFactor Is a knowledge factor required
     * @param requireInherenceFactor Is an inherence factor required
     * @param requirePossessionFactor Is a possession factor required
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(Boolean requireKnowledgeFactor, Boolean requireInherenceFactor, Boolean requirePossessionFactor,
                      List<Location> locations) {
        this(requireKnowledgeFactor, requireInherenceFactor, requirePossessionFactor, null, locations);
    }

    /**
     * Create an getServiceService policy that includes determination of which getServiceService factors are required as well as geofence locations.
     *
     * @param requireKnowledgeFactor Is a knowledge factor required
     * @param requireInherenceFactor Is an inherence factor required
     * @param requirePossessionFactor Is a possession factor required
     * @param enableJailbreakProtection Enable protection against jailbroken or rooted devices responding to an
     *                                  authorization request.
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(Boolean requireKnowledgeFactor, Boolean requireInherenceFactor, Boolean requirePossessionFactor,
                      Boolean enableJailbreakProtection, List<Location> locations) {
        this.requiredFactors = null;
        this.requireKnowledgeFactor = requireKnowledgeFactor;
        this.requireInherenceFactor = requireInherenceFactor;
        this.requirePossessionFactor = requirePossessionFactor;
        this.enableJailbreakProtection = enableJailbreakProtection;
        this.locations = Collections.unmodifiableList(new ArrayList<>(locations));
    }

    /**
     * Get the number of service factors required by this policy
     *
     * @return The number of service factors required by this policy
     */
    public Integer getRequiredFactors() {
        return requiredFactors;
    }

    /**
     * Is a knowledge factor required by this policy
     *
     * @return Is a knowledge factor required by this policy
     */
    public Boolean isKnowledgeFactorRequired() {
        return requireKnowledgeFactor;
    }

    /**
     * Is an inherence factor required by this policy
     *
     * @return Is an inherence factor required by this policy
     */
    public Boolean isInherenceFactorRequired() {
        return requireInherenceFactor;
    }

    /**
     * Is a possession factor required by this policy
     *
     * @return Is an possession factor required by this policy
     */
    public Boolean isPossessionFactorRequired() {
        return requirePossessionFactor;
    }

    /**
     * @return Is jail break protection enabled
     */
    public Boolean isJailbreakProtectionEnabled() {
        return enableJailbreakProtection;
    }

    /**
     * Which geofence locations are acceptable for this policy
     *
     * @return The geofence locations that are acceptable for this policy
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Location utilized for Geofencing
     */
    public static class Location {
        private final double longitude;
        private final double latitude;
        private final double radius;
        private final String name;

        /**
         * @param radius Radius of the geolocation in meters
         * @param latitude Latitude of the geolocation
         * @param longitude Longitude of the geolocation
         */
        public Location(double radius, double latitude, double longitude) {
            this(null, radius, latitude, longitude);
        }

        /**
         * @param name Name of the geolocation
         * @param radius Radius of the geolocation in meters
         * @param latitude Latitude of the geolocation
         * @param longitude Longitude of the geolocation
         */
        public Location(String name, double radius, double latitude, double longitude) {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
            this.name = name;
        }

        /**
         * Get the name of the geolocation
         *
         * @return Name of the geolocation
         */
        public String getName() {
            return name;
        }

        /**
         * Get the longitude of the geolocation
         *
         * @return The longitude of the geolocation
         */
        public double getLongitude() {
            return longitude;
        }

        /**
         * Get the latitude of the geolocation
         *
         * @return The latitude of the geolocation
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Get the radius in meters
         *
         * @return The radius in meters
         */
        public double getRadius() {
            return radius;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Location)) return false;
            Location location = (Location) o;
            return Double.compare(location.getLongitude(), getLongitude()) == 0 &&
                    Double.compare(location.getLatitude(), getLatitude()) == 0 &&
                    Double.compare(location.getRadius(), getRadius()) == 0 &&
                    Objects.equals(getName(), location.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getLongitude(), getLatitude(), getRadius(), getName());
        }

        @Override
        public String toString() {
            return "Location{" +
                    "longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", radius=" + radius +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthPolicy)) return false;
        AuthPolicy that = (AuthPolicy) o;
        return Objects.equals(getRequiredFactors(), that.getRequiredFactors()) &&
                Objects.equals(requireKnowledgeFactor, that.requireKnowledgeFactor) &&
                Objects.equals(requireInherenceFactor, that.requireInherenceFactor) &&
                Objects.equals(requirePossessionFactor, that.requirePossessionFactor) &&
                Objects.equals(getLocations(), that.getLocations()) &&
                Objects.equals(enableJailbreakProtection, that.enableJailbreakProtection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRequiredFactors(), requireKnowledgeFactor, requireInherenceFactor, requirePossessionFactor, getLocations(), enableJailbreakProtection);
    }

    @Override
    public String toString() {
        return "AuthPolicy{" +
                "requiredFactors=" + requiredFactors +
                ", requireKnowledgeFactor=" + requireKnowledgeFactor +
                ", requireInherenceFactor=" + requireInherenceFactor +
                ", requirePossessionFactor=" + requirePossessionFactor +
                ", locations=" + locations +
                ", enableJailbreakProtection=" + enableJailbreakProtection +
                '}';
    }
}
