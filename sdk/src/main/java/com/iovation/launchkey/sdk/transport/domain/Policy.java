package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@JsonPropertyOrder({"type", "deny_rooted_jailbroken", "deny_emulator_simulator", "fences", "factors", "amount",
        "inside", "outside", "minimum_requirements"})
public class Policy {
    public static final String TYPE_METHOD_AMOUNT = "METHOD_AMOUNT";
    public static final String TYPE_FACTORS = "FACTORS";
    public static final String TYPE_COND_GEO = "COND_GEO";
    public static final String TYPE_LEGACY = "LEGACY";

    @JsonProperty("type")
    private final String policyType;

    @JsonProperty("fences")
    private final List<Fence> fences;

    @JsonProperty("deny_rooted_jailbroken")
    private final Boolean denyRootedJailbroken;

    @JsonProperty("deny_emulator_simulator")
    private final Boolean denyEmulatorSimulator;

    @JsonProperty("factors")
    private final ArrayNode factors;

    @JsonProperty("amount")
    private final Integer amount;

    @JsonProperty("inside")
    private final Policy inPolicy;

    @JsonProperty("outside")
    private final Policy outPolicy;

    @JsonProperty("minimum_requirements")
    private final  List<MinimumRequirement> minimumRequirements;

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Policy(
            @JsonProperty("type") String policyTypeString,
            @JsonProperty("deny_rooted_jailbroken") Boolean denyRootedJailbroken,
            @JsonProperty("deny_emulator_simulator") Boolean denyEmulatorSimulator,
            @JsonProperty("fences") List<Fence> fences, @JsonProperty("factors") ArrayNode factors,
            @JsonProperty("amount") Integer amount, @JsonProperty("inside") Policy inPolicy,
            @JsonProperty("outside") Policy outPolicy, @JsonProperty("minimum_requirements") List<MinimumRequirement> minimumRequirements) {
        this.policyType = policyTypeString;
        this.denyRootedJailbroken = denyRootedJailbroken;
        this.denyEmulatorSimulator = denyEmulatorSimulator;
        this.fences = fences;
        this.factors = factors;
        this.amount = amount;
        this.inPolicy = inPolicy;
        this.outPolicy = outPolicy;
        this.minimumRequirements = minimumRequirements;
    }

    public String getPolicyType() { return policyType; }

    public List<Fence> getFences() {
        return fences;
    }

    public Boolean getDenyRootedJailbroken() {
        return denyRootedJailbroken;
    }

    public Boolean getDenyEmulatorSimulator() {
        return denyEmulatorSimulator;
    }

    public ArrayNode getFactors() {
        return factors;
    }

    public Integer getAmount() {
        return amount;
    }

    public Policy getInPolicy() {
        return inPolicy;
    }

    public Policy getOutPolicy() {
        return outPolicy;
    }

    public List<MinimumRequirement> getMinimumRequirements() {
        return minimumRequirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return Objects.equals(policyType, policy.policyType) &&
                Objects.equals(fences, policy.fences) &&
                Objects.equals(denyRootedJailbroken, policy.denyRootedJailbroken) &&
                Objects.equals(denyEmulatorSimulator, policy.denyEmulatorSimulator) &&
                Objects.equals(factors, policy.factors) &&
                Objects.equals(amount, policy.amount) &&
                Objects.equals(inPolicy, policy.inPolicy) &&
                Objects.equals(outPolicy, policy.outPolicy) &&
                Objects.equals(minimumRequirements, policy.minimumRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyType, fences, denyRootedJailbroken, denyEmulatorSimulator, factors, amount, inPolicy, outPolicy, minimumRequirements);
    }

    @Override
    public String toString() {
        return "Policy{" +
                "policyType='" + policyType + '\'' +
                ", fences=" + fences +
                ", denyRootedJailbroken=" + denyRootedJailbroken +
                ", denyEmulatorSimulator=" + denyEmulatorSimulator +
                ", factors=" + factors +
                ", amount=" + amount +
                ", inPolicy=" + inPolicy +
                ", outPolicy=" + outPolicy +
                ", minimumRequirements=" + minimumRequirements +
                '}';
    }

    @JsonInclude(NON_NULL)
    public static class MinimumRequirement {
        private final Type type;
        private final Integer any;
        private final Integer knowledge;
        private final Integer inherence;
        private final Integer possession;

        @JsonCreator
        @JsonIgnoreProperties(ignoreUnknown = true)
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
        @JsonIgnoreProperties(ignoreUnknown = true)
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
