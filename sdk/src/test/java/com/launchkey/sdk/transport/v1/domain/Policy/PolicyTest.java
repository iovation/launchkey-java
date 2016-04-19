package com.launchkey.sdk.transport.v1.domain.Policy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

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
public class PolicyTest {
    private Policy policy;
    private ArrayList<Factor> factors;
    private ArrayList<Policy.MinimumRequirement> minimumRequirements;

    @Before
    public void setUp() throws Exception {
        factors = new ArrayList<Factor>();
        minimumRequirements = new ArrayList<Policy.MinimumRequirement>();
        policy = new Policy(minimumRequirements, factors);
    }

    @After
    public void tearDown() throws Exception {
        factors = null;
        minimumRequirements = null;
        policy = null;
    }

    @Test
    public void getMinimumRequirements() throws Exception {
        assertEquals(minimumRequirements, policy.getMinimumRequirements());
    }


    @Test
    public void minimumRequirementsListIsNotOriginalList() throws Exception {
        assertNotSame(minimumRequirements, policy.getMinimumRequirements());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void minimumRequirementsListIsImmutable() throws Exception {
        policy.getMinimumRequirements().clear();
    }

    @Test
    public void getFactors() throws Exception {
        assertEquals(factors, policy.getFactors());
    }

    @Test
    public void factorsListIsNotOriginalList() throws Exception {
        assertNotSame(factors, policy.getFactors());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void factorsListIsImmutable() throws Exception {
        policy.getFactors().clear();
    }

    @Test
    public void equalsWhenObjectsEqual() throws Exception {
        assertEquals(policy, new Policy(minimumRequirements, factors));
    }

    @Test
    public void notEqualsWhenObjectsNotEqual() throws Exception {
        minimumRequirements.add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 1, 2, 3, 4));
        assertNotEquals(policy, new Policy(minimumRequirements, factors));
    }

    @Test
    public void hashCodeEqualWhenObjectsEqual() throws Exception {
        assertEquals(policy.hashCode(), new Policy(minimumRequirements, factors).hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenObjectsNotEqual() throws Exception {
        minimumRequirements.add(new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 1, 2, 3, 4));
        assertNotEquals(policy.hashCode(), new Policy(minimumRequirements, factors).hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(policy.toString(), containsString(Policy.class.getSimpleName()));
    }

    @Test
    public void jsonEncode() throws Exception {
        String expected = "{" +
                    "\"minimum requirement\":[]," +
                    "\"factors\":[]" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(policy);
        assertEquals(expected, actual);


    }
}