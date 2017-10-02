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

import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverter;
import cucumber.deps.com.thoughtworks.xstream.annotations.XStreamConverters;
import cucumber.deps.com.thoughtworks.xstream.converters.SingleValueConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ServicePolicyEntity {
    private Integer requiredFactors;
    private Boolean requireKnowledgeFactor;
    private Boolean requireInherenceFactor;
    private Boolean requirePossessionFactor;
    private final List<Location> locations;
    private final List<TimeFence> timeFences;
    private Boolean jailBreakProtectionEnabled;

    public ServicePolicyEntity(Integer requiredFactors, Boolean requireKnowledgeFactor, Boolean requireInherenceFactor,
                               Boolean requirePossessionFactor,
                               List<Location> locations,
                               List<TimeFence> timeFences, Boolean jailBreakProtectionEnabled) {
        this.requiredFactors = requiredFactors;
        this.requireKnowledgeFactor = requireKnowledgeFactor;
        this.requireInherenceFactor = requireInherenceFactor;
        this.requirePossessionFactor = requirePossessionFactor;
        this.locations = locations;
        this.timeFences = timeFences;
        this.jailBreakProtectionEnabled = jailBreakProtectionEnabled;

    }

    public ServicePolicyEntity() {
        requiredFactors = null;
        requireInherenceFactor = null;
        requireKnowledgeFactor = null;
        requirePossessionFactor = null;
        locations = new ArrayList<>();
        timeFences = new ArrayList<>();
        jailBreakProtectionEnabled = null;
    }

    public Integer getRequiredFactors() {
        return requiredFactors;
    }

    public void setRequiredFactors(Integer requiredFactors) {
        this.requiredFactors = requiredFactors;
    }

    public Boolean getRequireKnowledgeFactor() {
        return requireKnowledgeFactor;
    }

    public void setRequireKnowledgeFactor(Boolean requireKnowledgeFactor) {
        this.requireKnowledgeFactor = requireKnowledgeFactor;
    }

    public Boolean getRequireInherenceFactor() {
        return requireInherenceFactor;
    }

    public void setRequireInherenceFactor(Boolean requireInherenceFactor) {
        this.requireInherenceFactor = requireInherenceFactor;
    }

    public Boolean getRequirePossessionFactor() {
        return requirePossessionFactor;
    }

    public void setRequirePossessionFactor(Boolean requirePossessionFactor) {
        this.requirePossessionFactor = requirePossessionFactor;
    }

    public Boolean getJailBreakProtectionEnabled() {
        return jailBreakProtectionEnabled;
    }

    public void setJailBreakProtectionEnabled(Boolean jailBreakProtectionEnabled) {
        this.jailBreakProtectionEnabled = jailBreakProtectionEnabled;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<TimeFence> getTimeFences() {
        return timeFences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicePolicyEntity)) return false;

        ServicePolicyEntity that = (ServicePolicyEntity) o;

        if (requiredFactors != null ? !requiredFactors.equals(that.requiredFactors) : that.requiredFactors != null)
            return false;
        if (requireKnowledgeFactor != null ? !requireKnowledgeFactor.equals(that.requireKnowledgeFactor) :
                that.requireKnowledgeFactor != null) return false;
        if (requireInherenceFactor != null ? !requireInherenceFactor.equals(that.requireInherenceFactor) :
                that.requireInherenceFactor != null) return false;
        if (requirePossessionFactor != null ? !requirePossessionFactor.equals(that.requirePossessionFactor) :
                that.requirePossessionFactor != null) return false;
        if (locations != null ? !locations.equals(that.locations) : that.locations != null) return false;
        if (timeFences != null ? !timeFences.equals(that.timeFences) : that.timeFences != null) return false;
        return jailBreakProtectionEnabled != null ? jailBreakProtectionEnabled.equals(that.jailBreakProtectionEnabled) :
                that.jailBreakProtectionEnabled == null;
    }

    @Override
    public int hashCode() {
        int result = requiredFactors != null ? requiredFactors.hashCode() : 0;
        result = 31 * result + (requireKnowledgeFactor != null ? requireKnowledgeFactor.hashCode() : 0);
        result = 31 * result + (requireInherenceFactor != null ? requireInherenceFactor.hashCode() : 0);
        result = 31 * result + (requirePossessionFactor != null ? requirePossessionFactor.hashCode() : 0);
        result = 31 * result + (locations != null ? locations.hashCode() : 0);
        result = 31 * result + (timeFences != null ? timeFences.hashCode() : 0);
        result = 31 * result + (jailBreakProtectionEnabled != null ? jailBreakProtectionEnabled.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServicePolicyEntity{" +
                "requiredFactors=" + requiredFactors +
                ", requireKnowledgeFactor=" + requireKnowledgeFactor +
                ", requireInherenceFactor=" + requireInherenceFactor +
                ", requirePossessionFactor=" + requirePossessionFactor +
                ", locations=" + locations +
                ", timeFences=" + timeFences +
                ", jailBreakProtectionEnabled=" + jailBreakProtectionEnabled +
                '}';
    }

    public static ServicePolicyEntity fromServicePolicy(ServicePolicy servicePolicy) {
        List<Location> locations = new ArrayList<>();
        for (ServicePolicy.Location location : servicePolicy.getLocations()) {
            locations.add(new Location(location.getName(), location.getLongitude(), location.getLatitude(),
                    location.getRadius()));
        }
        List<TimeFence> timeFences = new ArrayList<>();
        for (ServicePolicy.TimeFence timeFence : servicePolicy.getTimeFences()) {
            timeFences.add(new TimeFence(timeFence.getName(), timeFence.getDays(), timeFence.getStartHour(),
                    timeFence.getStartMinute(), timeFence.getEndHour(), timeFence.getEndMinute(),
                    timeFence.getTimeZone()));
        }
        return new ServicePolicyEntity(servicePolicy.getRequiredFactors(), servicePolicy.isKnowledgeFactorRequired(),
                servicePolicy.isInherenceFactorRequired(), servicePolicy.isPossessionFactorRequired(),
                locations, timeFences, servicePolicy.isJailBreakProtectionEnabled());
    }

    public ServicePolicy toServicePolicy() {
        List<ServicePolicy.Location> locationsList = new ArrayList<>();
        for (Location location : getLocations()) {
            locationsList.add(new ServicePolicy.Location(location.getName(), location.getRadius(),
                    location.getLatitude(), location.getLongitude()));
        }
        List<ServicePolicy.TimeFence> timeFencesList = new ArrayList<>();
        for (TimeFence timeFence : getTimeFences()) {
            timeFencesList.add(new ServicePolicy.TimeFence(timeFence.getName(), timeFence.getDays(),
                    timeFence.getStartHour(), timeFence.getStartMinute(), timeFence.getEndHour(),
                    timeFence.getEndMinute(), timeFence.getTimeZone()));
        }
        return new ServicePolicy(getRequiredFactors(), getRequireKnowledgeFactor(), getRequireInherenceFactor(),
                getRequirePossessionFactor(), getJailBreakProtectionEnabled(), locationsList, timeFencesList);
    }

    public final static class Location {
        private final String name;
        private final double longitude;
        private final double latitude;
        private final double radius;

        public Location(String name, double longitude, double latitude, double radius) {
            this.name = name;
            this.longitude = longitude;
            this.latitude = latitude;
            this.radius = radius;
        }

        public String getName() {
            return name;
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

    public final static class TimeFence {
        private final String name;

        @XStreamConverter(DaysConverter.class)
        private final List<ServicePolicy.Day> days;
        private final int startHour;
        private final int startMinute;
        private final int endHour;
        private final int endMinute;

        @XStreamConverter(TimeZoneConverter.class)
        private final TimeZone timeZone;

        public TimeFence(String name, List<ServicePolicy.Day> days, int startHour, int startMinute, int endHour,
                         int endMinute,
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

        public List<ServicePolicy.Day> getDays() {
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
}
