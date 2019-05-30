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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

@JsonIgnoreProperties(value = {"geoFenceLocations", "geoFences", "timeFences", "deviceIntegrity"}, ignoreUnknown = true)
public class ServicePolicy extends AuthPolicy {
    private final List<TimeFence> timeFences;

    public ServicePolicy(Integer any, Boolean inherence, Boolean knowledge, Boolean possession, Boolean deviceIntegrity) {
        super(any, inherence, knowledge, possession, deviceIntegrity);
        timeFences = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    @JsonCreator
    public ServicePolicy(@JsonProperty("minimum_requirements") List<MinimumRequirement> minimumRequirements,
                         @JsonProperty("factors") ArrayNode factors) {
        super(minimumRequirements, factors, null, null, null);
        List<TimeFence> timeFences = new ArrayList<>();
        try {
            for (JsonNode factor : factors) {
                final String factorName = factor.get("factor").textValue();
                final JsonNode attributes = factor.get("attributes");
                if (factorName.equals("timefence")) {
                    for (JsonNode timeFence : attributes.get("time fences")) {
                        List<String> days = new ArrayList<>();
                        for (JsonNode jsonNode : timeFence.get("days")) {
                            days.add(jsonNode.asText());
                        }
                        timeFences.add(new TimeFence(
                                timeFence.get("name").asText(),
                                days,
                                timeFence.get("start hour").asInt(),
                                timeFence.get("end hour").asInt(),
                                timeFence.get("start minute").asInt(),
                                timeFence.get("end minute").asInt(),
                                timeFence.get("timezone").asText()
                        ));
                    }
                }
            }
            this.timeFences = timeFences;
        } catch (ClassCastException | NullPointerException e) {
            throw new IllegalArgumentException("The argument Factors does not conform to the specification", e);
        }
    }

    public void addTimeFence(TimeFence timeFence) {
        timeFences.add(timeFence);
    }

    public List<TimeFence> getTimeFences() {
        return timeFences;
    }

    @Override
    public List<Map<String, Object>> getFactors() {
        List<Map<String, Object>> factors = super.getFactors();
        if (!timeFences.isEmpty()) {
            Map<String, Object> factor = new LinkedHashMap<>();
            factor.put("factor", "timefence");
            factor.put("requirement", "forced requirement");
            factor.put("priority", 1);
            Map<String, Object> attributes = new LinkedHashMap<>();
            factor.put("attributes", attributes);
            attributes.put("time fences", timeFences);
            factors.add(factor);
        }
        return factors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicePolicy)) return false;
        if (!super.equals(o)) return false;

        ServicePolicy that = (ServicePolicy) o;

        return timeFences != null ? timeFences.equals(that.timeFences) : that.timeFences == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (timeFences != null ? timeFences.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServicePolicy{" +
                "timeFences=" + timeFences +
                "} " + super.toString();
    }

    @JsonPropertyOrder({"name", "days", "start hour", "end hour", "start minute", "end minute", "timezone"})
    public static class TimeFence {
        private final String name;
        private final List<String> days;
        private final int startHour;
        private final int endHour;
        private final int startMinute;
        private final int endMinute;
        private final String timezone;

        @JsonCreator
        public TimeFence(@JsonProperty("name") String name, @JsonProperty("days") List<String> days,
                         @JsonProperty("start hour") int startHour, @JsonProperty("end hour") int endHour,
                         @JsonProperty("start minute") int startMinute, @JsonProperty("end minute") int endMinute,
                         @JsonProperty("timezone") String timezone) {
            this.name = name;
            this.days = days;
            this.startHour = startHour;
            this.endHour = endHour;
            this.startMinute = startMinute;
            this.endMinute = endMinute;
            this.timezone = timezone;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("days")
        public List<String> getDays() {
            return days;
        }

        @JsonProperty("start hour")
        public int getStartHour() {
            return startHour;
        }

        @JsonProperty("end hour")
        public int getEndHour() {
            return endHour;
        }

        @JsonProperty("start minute")
        public int getStartMinute() {
            return startMinute;
        }

        @JsonProperty("end minute")
        public int getEndMinute() {
            return endMinute;
        }

        @JsonProperty("timezone")
        public String getTimezone() {
            return timezone;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TimeFence)) return false;

            TimeFence timeFence = (TimeFence) o;

            if (startHour != timeFence.startHour) return false;
            if (endHour != timeFence.endHour) return false;
            if (startMinute != timeFence.startMinute) return false;
            if (endMinute != timeFence.endMinute) return false;
            if (!Objects.equals(name, timeFence.name)) return false;
            if (!Objects.equals(days, timeFence.days)) return false;
            return Objects.equals(timezone, timeFence.timezone);
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (days != null ? days.hashCode() : 0);
            result = 31 * result + startHour;
            result = 31 * result + endHour;
            result = 31 * result + startMinute;
            result = 31 * result + endMinute;
            result = 31 * result + (timezone != null ? timezone.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "TimeFence{" +
                    "name='" + name + '\'' +
                    ", days=" + days +
                    ", startHour=" + startHour +
                    ", endHour=" + endHour +
                    ", startMinute=" + startMinute +
                    ", endMinute=" + endMinute +
                    ", timezone='" + timezone + '\'' +
                    '}';
        }
    }
}
