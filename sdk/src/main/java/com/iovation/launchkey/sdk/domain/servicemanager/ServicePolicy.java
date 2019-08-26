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

package com.iovation.launchkey.sdk.domain.servicemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Class to represent an authentication policy which will be utilized for providing dynamic policies per request.
 */
public class ServicePolicy implements PolicyAdapter {
    private final Integer requiredFactors;
    private final Boolean requireKnowledgeFactor;
    private final Boolean requireInherenceFactor;
    private final Boolean requirePossessionFactor;
    private final List<Location> locations;
    private final List<TimeFence> timeFences;
    private final Boolean jailBreakProtectionEnabled;

    /**
     * Create an service which requires a number factors to be used.
     *
     * @param requiredFactors The number of unique factors to require.
     * @param jailBreakProtectionEnabled Is Jail Break protection enabled
     */
    public ServicePolicy(int requiredFactors, boolean jailBreakProtectionEnabled) {
        this(requiredFactors, false, false, false, jailBreakProtectionEnabled, null, null);
    }

    /**
     * Create an getServiceService policy that determines which getServiceService factors are required.
     *
     * @param requireKnowledgeFactor Is a knowledge factor required
     * @param requireInherenceFactor Is an inherence factor required
     * @param requirePossessionFactor Is a possession factor required
     * @param jailBreakProtectionEnabled Is Jail Break protection enabled
     */
    public ServicePolicy(boolean requireKnowledgeFactor, boolean requireInherenceFactor,
                         boolean requirePossessionFactor, boolean jailBreakProtectionEnabled) {
        this(0, requireKnowledgeFactor, requireInherenceFactor, requirePossessionFactor, jailBreakProtectionEnabled,
                null, null);
    }

    public ServicePolicy() {
        this(null, null, null, null, null, null, null);
    }

    public ServicePolicy(Integer requiredFactors, Boolean requireKnowledgeFactor, Boolean requireInherenceFactor,
                         Boolean requirePossessionFactor, Boolean jailBreakProtectionEnabled, List<Location> locations,
                         List<TimeFence> timeFences) {
        this.requiredFactors = requiredFactors;
        this.requireKnowledgeFactor = requireKnowledgeFactor;
        this.requireInherenceFactor = requireInherenceFactor;
        this.requirePossessionFactor = requirePossessionFactor;
        this.jailBreakProtectionEnabled = jailBreakProtectionEnabled;
        this.locations = locations == null ? new ArrayList<Location>() : locations;
        this.timeFences = timeFences == null ? new ArrayList<TimeFence>() : timeFences;
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
     * Add geofence locations to this policy
     *
     * @param locations The geofence locations to add to this policy
     */
    public void addLocations(List<Location> locations) {
        this.locations.addAll(locations);
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
     * Add time fences to this policy
     *
     * @param timeFences TIme fences tdd to this policy
     */
    public void addTimeFences(List<TimeFence> timeFences) {
        this.timeFences.addAll(timeFences);
    }

    /**
     * Get all time fences for this policy
     *
     * @return All time fences for this policy
     */
    public List<TimeFence> getTimeFences() {
        return timeFences;
    }

    /**
     * Is Jail Break protection enabled
     *
     * @return Jail Break protection enabled
     */
    public Boolean isJailBreakProtectionEnabled() {
        return jailBreakProtectionEnabled;
    }

    /**
     * Location utilized for Geofencing
     */
    public static class Location {
        private final String name;
        private final double longitude;
        private final double latitude;
        private final double radius;

        /**
         * @param name Name of the geolocation
         * @param radius Radius of the geolocation in meters
         * @param latitude Latitude of the geolocation
         * @param longitude Longitude of the geolocation
         */
        public Location(String name, double radius, double latitude, double longitude) {
            this.name = name;
            this.radius = radius;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        /**
         * Get the name of the geolocation
         *
         * @return The name of the geolocation
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

            if (Double.compare(location.longitude, longitude) != 0) return false;
            if (Double.compare(location.latitude, latitude) != 0) return false;
            if (Double.compare(location.radius, radius) != 0) return false;
            return name != null ? name.equals(location.name) : location.name == null;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = name != null ? name.hashCode() : 0;
            temp = Double.doubleToLongBits(longitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(latitude);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(radius);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "name='" + name + '\'' +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", radius=" + radius +
                    '}';
        }
    }

    public enum Day {
        SUNDAY("Sunday"), MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"),
        FRIDAY("Friday"), SATURDAY("Saturday");

        private final String value;

        Day(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Day fromString(String value) {
            for (Day day : Day.values()) {
                if (day.value.equals(value)) {
                    return day;
                }
            }
            throw new IllegalArgumentException(value + " is not a valid day");
        }
    }

    public static class TimeFence {
        private final String name;
        private final List<Day> days;
        private final int startHour;
        private final int startMinute;
        private final int endHour;
        private final int endMinute;
        private final TimeZone timeZone;

        public TimeFence(String name, List<Day> days, int startHour, int startMinute, int endHour, int endMinute,
                         TimeZone timeZone) {
            this.name = name;
            this.days = days;
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
            this.timeZone = timeZone;
        }

        public String getName() {
            return name;
        }

        public List<Day> getDays() {
            return days;
        }

        public int getStartHour() {
            return startHour;
        }

        public int getStartMinute() {
            return startMinute;
        }

        public int getEndHour() {
            return endHour;
        }

        public int getEndMinute() {
            return endMinute;
        }

        public TimeZone getTimeZone() {
            return timeZone;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeFence)) return false;

            TimeFence timeFence = (TimeFence) o;

            if (startHour != timeFence.startHour) return false;
            if (startMinute != timeFence.startMinute) return false;
            if (endHour != timeFence.endHour) return false;
            if (endMinute != timeFence.endMinute) return false;
            if (name != null ? !name.equals(timeFence.name) : timeFence.name != null) return false;
            if (days != null ? !days.equals(timeFence.days) : timeFence.days != null) return false;
            return timeZone != null ? timeZone.equals(timeFence.timeZone) : timeFence.timeZone == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (days != null ? days.hashCode() : 0);
            result = 31 * result + startHour;
            result = 31 * result + startMinute;
            result = 31 * result + endHour;
            result = 31 * result + endMinute;
            result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "TimeFence{" +
                    "name='" + name + '\'' +
                    ", days=" + days +
                    ", startHour=" + startHour +
                    ", startMinute=" + startMinute +
                    ", endHour=" + endHour +
                    ", endMinute=" + endMinute +
                    ", timeZone=" + timeZone +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicePolicy)) return false;

        ServicePolicy that = (ServicePolicy) o;

        if (requiredFactors != that.requiredFactors) return false;
        if (requireKnowledgeFactor != that.requireKnowledgeFactor) return false;
        if (requireInherenceFactor != that.requireInherenceFactor) return false;
        if (requirePossessionFactor != that.requirePossessionFactor) return false;
        if (jailBreakProtectionEnabled != that.jailBreakProtectionEnabled) return false;
        if (locations != null ? !locations.equals(that.locations) : that.locations != null) return false;
        return timeFences != null ? timeFences.equals(that.timeFences) : that.timeFences == null;
    }

    @Override
    public String toString() {
        return "ServicePolicy{" +
                "requiredFactors=" + requiredFactors +
                ", requireKnowledgeFactor=" + requireKnowledgeFactor +
                ", requireInherenceFactor=" + requireInherenceFactor +
                ", requirePossessionFactor=" + requirePossessionFactor +
                ", locations=" + locations +
                '}';
    }
}
