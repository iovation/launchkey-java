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

package com.launchkey.sdk.transport.v1.domain.Policy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Policy {
    private final List<MinimumRequirement> minimumRequirements;
    private final List<Factor> factors;

    public Policy(List<MinimumRequirement> minimumRequirements, List<Factor> factors) {
        this.minimumRequirements = Collections.unmodifiableList(new ArrayList<MinimumRequirement>(minimumRequirements));
        this.factors = Collections.unmodifiableList(new ArrayList<Factor>(factors));
    }

    @JsonProperty("minimum_requirements")
    public List<MinimumRequirement> getMinimumRequirements() {
        return minimumRequirements;
    }

    @JsonProperty("factors")
    public List<Factor> getFactors() {
        return factors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Policy)) return false;

        Policy policy = (Policy) o;

        if (getMinimumRequirements() != null
                ? !getMinimumRequirements().equals(policy.getMinimumRequirements())
                : policy.getMinimumRequirements() != null) {
            return false;
        }
        return getFactors() != null ? getFactors().equals(policy.getFactors()) : policy.getFactors() == null;

    }

    @Override
    public int hashCode() {
        int result = getMinimumRequirements() != null ? getMinimumRequirements().hashCode() : 0;
        result = 31 * result + (getFactors() != null ? getFactors().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "minimumRequirements=" + minimumRequirements +
                ", factors=" + factors +
                '}';
    }

    public static class MinimumRequirement {
        private final Type type;
        private final int all;
        private final int knowledge;
        private final int inherence;
        private final int possession;

        public MinimumRequirement(Type type, int all, int knowledge, int inherence, int possession) {
            this.type = type;
            this.all = all;
            this.knowledge = knowledge;
            this.inherence = inherence;
            this.possession = possession;
        }

        @JsonProperty("requirement")
        public Type getType() {
            return type;
        }

        @JsonProperty("all")
        public int getAll() {
            return all;
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

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MinimumRequirement)) return false;

            MinimumRequirement that = (MinimumRequirement) o;

            if (all != that.all) return false;
            if (knowledge != that.knowledge) return false;
            if (inherence != that.inherence) return false;
            if (possession != that.possession) return false;
            return type == that.type;

        }

        @Override public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            result = 31 * result + all;
            result = 31 * result + knowledge;
            result = 31 * result + inherence;
            result = 31 * result + possession;
            return result;
        }

        @Override public String toString() {
            return "MinimumRequirements{" +
                    "type=" + type +
                    ", all=" + all +
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
