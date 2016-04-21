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

public class AuthPolicy {
    private final int requiredFactors;
    private final boolean requireKnowledgeFactor;
    private final boolean requireInherenceFactor;
    private final boolean requirePossessionFactor;
    private final List<Location> locations;

    public AuthPolicy(List<Location> locations) {
        this(false, false, false, locations);
    }

    public AuthPolicy(int requiredFactors) {
        this(requiredFactors, new ArrayList<Location>());
    }

    public AuthPolicy(int requiredFactors, List<Location> locations) {
        this.requiredFactors = requiredFactors;
        this.locations = Collections.unmodifiableList(new ArrayList<Location>(locations));
        requirePossessionFactor = false;
        requireInherenceFactor = false;
        requireKnowledgeFactor = false;
    }

    public AuthPolicy(boolean requireKnowledgeFactor, boolean requireInherenceFactor, boolean requirePossessionFactor) {
        this(requireKnowledgeFactor, requireInherenceFactor, requirePossessionFactor, new ArrayList<Location>());
    }

    public AuthPolicy(boolean requireKnowledgeFactor, boolean requireInherenceFactor, boolean requirePossessionFactor, List<Location> locations) {
        this.requiredFactors = 0;
        this.requireKnowledgeFactor = requireKnowledgeFactor;
        this.requireInherenceFactor = requireInherenceFactor;
        this.requirePossessionFactor = requirePossessionFactor;
        this.locations = Collections.unmodifiableList(new ArrayList<Location>(locations));
    }

    public int getRequiredFactors() {
        return requiredFactors;
    }

    public boolean isKnowledgeFactorRequired() {
        return requireKnowledgeFactor;
    }

    public boolean isInherenceFactorRequired() {
        return requireInherenceFactor;
    }

    public boolean isPossessionFactorRequired() {
        return requirePossessionFactor;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public static class Location {
        private final double longitude;
        private final double latitude;
        private final double radius;

        public Location(double radius, double latitude, double longitude) {
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

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
