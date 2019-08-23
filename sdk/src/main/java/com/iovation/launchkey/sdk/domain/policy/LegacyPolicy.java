package com.iovation.launchkey.sdk.domain.policy;

import com.iovation.launchkey.sdk.domain.servicemanager.ServicePolicy;

import java.util.*;

public class LegacyPolicy implements Policy {

    private final int amount;
    private final Boolean inherence;
    private final Boolean knowledge;
    private final Boolean possession;
    private final boolean denyRootedJailbroken;
    private final List<GeoCircleFence> fences;
    private final List<TimeFence> timeFences;

    public LegacyPolicy(int amount, Boolean inherence, Boolean knowledge, Boolean possession, boolean denyRootedJailbroken, List<GeoCircleFence> fences, List<TimeFence> timeFences) {
        this.amount = amount;
        this.inherence = inherence;
        this.knowledge = knowledge;
        this.possession = possession;
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.fences = fences;
        this.timeFences = timeFences;
    }

    public int getAmount() {
        return amount;
    }

    public Boolean isInherenceRequired() {
        return inherence;
    }

    public Boolean isKnowledgeRequired() {
        return knowledge;
    }

    public Boolean isPossessionRequired() {
        return possession;
    }

    @Override
    public boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    @Override
    public boolean getDenyEmulatorSimulator() {
        return false;
    }

    public List<Fence> getFences() {
        if (fences != null) {
            return Collections.unmodifiableList(new ArrayList<Fence>(fences));
        } else {
            return null;
        }
    }

    public List<TimeFence> getTimeFences() {
        if (timeFences != null) {
            return Collections.unmodifiableList(timeFences);
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LegacyPolicy)) return false;
        LegacyPolicy that = (LegacyPolicy) o;
        return Objects.equals(getFences(), that.getFences()) &&
                Objects.equals(getDenyEmulatorSimulator(), that.getDenyEmulatorSimulator()) &&
                Objects.equals(getDenyRootedJailbroken(), that.getDenyRootedJailbroken()) &&
                Objects.equals(isInherenceRequired(), that.isInherenceRequired()) &&
                Objects.equals(isKnowledgeRequired(), that.isKnowledgeRequired()) &&
                Objects.equals(isPossessionRequired(), that.isPossessionRequired()) &&
                Objects.equals(getTimeFences(), that.getTimeFences());
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

}
