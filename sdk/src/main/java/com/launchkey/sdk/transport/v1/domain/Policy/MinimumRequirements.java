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

public class MinimumRequirements {
    private final MinimumRequirementType type;
    private final int any;
    private final int knowledge;
    private final int inherence;
    private final int possession;

    public MinimumRequirements(
            MinimumRequirementType type, int any, int knowledge, int inherence, int possession
    ) {
        this.type = type;
        this.any = any;
        this.knowledge = knowledge;
        this.inherence = inherence;
        this.possession = possession;
    }

    public MinimumRequirements(
            int any, int knowledge, int inherence, int possession
    ) {
        this(MinimumRequirementType.AUTHENTICATED, any, knowledge, inherence, possession);
    }

    public MinimumRequirements(int any) {
        this(any, 0, 0, 0);
    }

    public MinimumRequirements() {
        this(0);
    }

    public MinimumRequirementType getType() {
        return type;
    }

    public int getAny() {
        return any;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public int getInherence() {
        return inherence;
    }

    public int getPossession() {
        return possession;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinimumRequirements)) return false;

        MinimumRequirements that = (MinimumRequirements) o;

        if (any != that.any) return false;
        if (knowledge != that.knowledge) return false;
        if (inherence != that.inherence) return false;
        if (possession != that.possession) return false;
        return type == that.type;

    }

    @Override public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + any;
        result = 31 * result + knowledge;
        result = 31 * result + inherence;
        result = 31 * result + possession;
        return result;
    }

    @Override public String toString() {
        return "MinimumRequirements{" +
                "type=" + type +
                ", any=" + any +
                ", knowledge=" + knowledge +
                ", inherence=" + inherence +
                ", possession=" + possession +
                '}';
    }
}
