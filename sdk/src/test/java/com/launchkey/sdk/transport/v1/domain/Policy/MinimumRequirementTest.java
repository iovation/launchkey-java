package com.launchkey.sdk.transport.v1.domain.Policy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;
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
public class MinimumRequirementTest {
    private Policy.MinimumRequirement minumumRequirement;

    @Before
    public void setUp() throws Exception {
        minumumRequirement = new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 1, 2, 3, 4);
    }

    @After
    public void tearDown() throws Exception {
        minumumRequirement = null;
    }

    @Test
    public void getType() throws Exception {
        assertEquals(Policy.MinimumRequirement.Type.ENABLED, minumumRequirement.getType());
    }

    @Test
    public void getAny() throws Exception {
        assertEquals(1, minumumRequirement.getAny());
    }

    @Test
    public void getKnowledge() throws Exception {
        assertEquals(2, minumumRequirement.getKnowledge());
    }

    @Test
    public void getInherence() throws Exception {
        assertEquals(3, minumumRequirement.getInherence());
    }

    @Test
    public void getPossession() throws Exception {
        assertEquals(4, minumumRequirement.getPossession());
    }

    @Test
    public void equalsWhenObjectsAreEqual() throws Exception {
        Policy.MinimumRequirement o = new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 1, 2, 3, 4);
        assertEquals(minumumRequirement, o);
    }

    @Test
    public void notEqualsWhenObjectsAreNotEqual() throws Exception {
        Policy.MinimumRequirement o = new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 0, 2, 3, 4);
        assertNotEquals(minumumRequirement, o);
    }

    @Test
    public void hashCodeEqualWhenObectsAreEqual() throws Exception {
        Policy.MinimumRequirement o = new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 1, 2, 3, 4);
        assertEquals(minumumRequirement, o);
    }

    @Test
    public void hashCodeNotEqualWhenObectsAreNotEqual() throws Exception {
        Policy.MinimumRequirement o = new Policy.MinimumRequirement(Policy.MinimumRequirement.Type.ENABLED, 0, 2, 3, 4);
        assertNotEquals(minumumRequirement, o);
    }

    @Test
    public void toStringHasClassName() throws Exception {
        assertThat(minumumRequirement.toString(), containsString(Policy.MinimumRequirement.class.getSimpleName()));
    }

    @Test
    public void jsonEncode() throws Exception {
        String expected = "{" +
                    "\"requirement\":\"enabled\"," +
                    "\"any\":1," +
                    "\"knowledge\":2," +
                    "\"inherence\":3," +
                    "\"possession\":4" +
                "}";
        ObjectMapper om = new ObjectMapper();
        assertEquals(expected, om.writeValueAsString(minumumRequirement));
    }
}