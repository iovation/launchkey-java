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

import java.util.ArrayList;
import java.util.List;

public abstract class Factor {

    private final boolean quickFail;
    private final FactorRequirementType requirement;
    private final int priority;

    public Factor(boolean quickFail, FactorRequirementType requirement, int priority) {
        this.quickFail = quickFail;
        this.requirement = requirement;
        this.priority = priority;
    }

    public Factor() {
        this(false, FactorRequirementType.FORCED, 1);
    }

    public abstract FactorType getType();

    public abstract FactoryCategory getCategory();

    public boolean isQuickFail() {
        return quickFail;
    }

    public FactorRequirementType getRequirement() {
        return requirement;
    }

    public int getPriority() {
        return priority;
    }

    public List<Attribute> getAttributes() {
        return new ArrayList<Attribute>();
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factor)) return false;

        Factor factor = (Factor) o;

        if (getType() != factor.getType()) return false;
        if (isQuickFail() != factor.isQuickFail()) return false;
        if (getPriority() != factor.getPriority()) return false;
        if (getCategory() != factor.getCategory()) return false;
        if (getRequirement() != factor.getRequirement()) return false;
        return getAttributes() != null ? getAttributes().equals(factor.getAttributes()) : factor.getAttributes() == null;

    }

    @Override public int hashCode() {
        int result = getCategory() != null ? getCategory().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (isQuickFail() ? 1 : 0);
        result = 31 * result + (getRequirement() != null ? getRequirement().hashCode() : 0);
        result = 31 * result + getPriority();
        result = 31 * result + (getAttributes() != null ? getAttributes().hashCode() : 0);
        return result;
    }
}

