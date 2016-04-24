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

package com.launchkey.sdk.service.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to represent an authentication policy which will be utilized for providing dynamic policies per request.
 */
public class AuthPolicy {
    private final int requiredFactors;
    private final boolean requireKnowledgeFactor;
    private final boolean requireInherenceFactor;
    private final boolean requirePossessionFactor;
    private final List<Location> locations;

    /**
     * Create an auth policy which only has geofence location requirements.
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(List<Location> locations) {
        this(false, false, false, locations);
    }

    /**
     * Create an auth which requires a number factors to be used.
     * @param requiredFactors The number of unique factors to require.
     */
    public AuthPolicy(int requiredFactors) {
        this(requiredFactors, new ArrayList<Location>());
    }

    /**
     * Create an auth policy that includes the number of factors and geofence locations
     * @param requiredFactors The number of unique factors to require.
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(int requiredFactors, List<Location> locations) {
        this.requiredFactors = requiredFactors;
        this.locations = Collections.unmodifiableList(new ArrayList<Location>(locations));
        requirePossessionFactor = false;
        requireInherenceFactor = false;
        requireKnowledgeFactor = false;
    }

    /**
     * Create an auth policy that determines which auth factors are required.
     * @param requireKnowledgeFactor Is a knowledge factor required
     * @param requireInherenceFactor Is an inherence factor required
     * @param requirePossessionFactor Is a possession factor required
     */
    public AuthPolicy(boolean requireKnowledgeFactor, boolean requireInherenceFactor, boolean requirePossessionFactor) {
        this(requireKnowledgeFactor, requireInherenceFactor, requirePossessionFactor, new ArrayList<Location>());
    }

    /**
     * Create an auth policy that includes determination of which auth factors are required as well as geofence locations.
     * @param requireKnowledgeFactor Is a knowledge factor required
     * @param requireInherenceFactor Is an inherence factor required
     * @param requirePossessionFactor Is a possession factor required
     * @param locations List of acceptable locations in which the user's device may be located.
     */
    public AuthPolicy(boolean requireKnowledgeFactor, boolean requireInherenceFactor, boolean requirePossessionFactor, List<Location> locations) {
        this.requiredFactors = 0;
        this.requireKnowledgeFactor = requireKnowledgeFactor;
        this.requireInherenceFactor = requireInherenceFactor;
        this.requirePossessionFactor = requirePossessionFactor;
        this.locations = Collections.unmodifiableList(new ArrayList<Location>(locations));
    }

    /**
     * Get the number of auth factors required by this policy
     * @return
     */
    public int getRequiredFactors() {
        return requiredFactors;
    }

    /**
     * Is a knowledge factor required by this policy
     * @return
     */
    public boolean isKnowledgeFactorRequired() {
        return requireKnowledgeFactor;
    }

    /**
     * Is an inherence factore required by this policy
     * @return
     */
    public boolean isInherenceFactorRequired() {
        return requireInherenceFactor;
    }

    /**
     * Is a posession factor required by this policy
     * @return
     */
    public boolean isPossessionFactorRequired() {
        return requirePossessionFactor;
    }

    /**
     * Which geofence locations are acceptable for this policy
     * @return
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

        /**
         * @param radius Radius of the location in meters
         * @param latitude Latitude of the geolocation
         * @param longitude Longitude of the geolocation
         */
        public Location(double radius, double latitude, double longitude) {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * Get the longitude of the geolocation
         * @return
         */
        public double getLongitude() {
            return longitude;
        }

        /**
         * Get the latitude of the geolocation
         * @return
         */
        public double getLatitude() {
            return latitude;
        }

        /**
         * Get the radius in meters
         * @return
         */
        public double getRadius() {
            return radius;
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthPolicy)) return false;

        AuthPolicy that = (AuthPolicy) o;

        if (requiredFactors != that.requiredFactors) return false;
        if (requireKnowledgeFactor != that.requireKnowledgeFactor) return false;
        if (requireInherenceFactor != that.requireInherenceFactor) return false;
        if (requirePossessionFactor != that.requirePossessionFactor) return false;
        return locations.equals(that.locations);

    }

    @Override public int hashCode() {
        int result = requiredFactors;
        result = 31 * result + (requireKnowledgeFactor ? 1 : 0);
        result = 31 * result + (requireInherenceFactor ? 1 : 0);
        result = 31 * result + (requirePossessionFactor ? 1 : 0);
        result = 31 * result + locations.hashCode();
        return result;
    }

    @Override public String toString() {
        return "AuthPolicy{" +
                "requiredFactors=" + requiredFactors +
                ", requireKnowledgeFactor=" + requireKnowledgeFactor +
                ", requireInherenceFactor=" + requireInherenceFactor +
                ", requirePossessionFactor=" + requirePossessionFactor +
                ", locations=" + locations +
                '}';
    }
}
