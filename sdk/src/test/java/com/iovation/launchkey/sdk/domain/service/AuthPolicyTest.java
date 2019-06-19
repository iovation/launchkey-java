package com.iovation.launchkey.sdk.domain.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

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
public class AuthPolicyTest {
    @Test
    public void getRequiredFactorsReturnsValueForRequiredFactorsConstructor() throws Exception {
        assertEquals(Integer.valueOf(99), new AuthPolicy(99).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsValueForRequiredFactorsLocationsConstructor() throws Exception {
        assertEquals(Integer.valueOf(99), new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsNullForLocationsConstructor() throws Exception {
        assertEquals(null, new AuthPolicy(new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsNullForFactorRequiredConstructor() throws Exception {
        assertEquals(null, new AuthPolicy(true, true, true).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsNullForFactorRequiredLocationsConstructor() throws Exception {
        assertEquals(null, new AuthPolicy(true, true, true, new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsNullForFactorRequiredJailbreakProtectionLocationsConstructor() throws Exception {
        assertEquals(null, new AuthPolicy(true, true, true, true, new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsNullForRequiredFactorsConstructor() throws Exception {
        assertNull(new AuthPolicy(99).isKnowledgeFactorRequired());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsNullForRequiredFactorsLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isKnowledgeFactorRequired());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsNullForLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(new ArrayList<AuthPolicy.Location>()).isKnowledgeFactorRequired());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsNulllForForRequiredFactorsJailbreakProtectionLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, true, new ArrayList<AuthPolicy.Location>()).isKnowledgeFactorRequired());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsValueForFactorRequiredConstructor() throws Exception {
        assertTrue(new AuthPolicy(true, true, true).isKnowledgeFactorRequired());

    }

    @Test
    public void isKnowledgeFactorRequiredReturnsValueForFactorRequiredLocationsConstructor() throws Exception {
        assertTrue(new AuthPolicy(true, true, true, new ArrayList<AuthPolicy.Location>()).isKnowledgeFactorRequired());
    }

    @Test
    public void isInherenceFactorRequiredReturnsNullForRequiredFactorsConstructor() throws Exception {
        assertNull(new AuthPolicy(99).isInherenceFactorRequired());
    }

    @Test
    public void isInherenceFactorRequiredReturnsNulllForForRequiredFactorsJailbreakProtectionLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, true, new ArrayList<AuthPolicy.Location>()).isInherenceFactorRequired());
    }

    @Test
    public void isInherenceFactorRequiredReturnsFalseForRequiredFactorsLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isInherenceFactorRequired());
    }

    @Test
    public void isInherenceFactorRequiredReturnsValueForFactorRequiredConstructor() throws Exception {
        assertTrue(new AuthPolicy(true, true, true).isInherenceFactorRequired());

    }

    @Test
    public void isInherenceFactorRequiredReturnsValueForFactorRequiredLocationsConstructor() throws Exception {
        assertTrue(new AuthPolicy(true, true, true, new ArrayList<AuthPolicy.Location>()).isInherenceFactorRequired());
    }

    @Test
    public void isPossessionFactorRequiredReturnsNullForRequiredFactorsConstructor() throws Exception {
        assertNull(new AuthPolicy(99).isPossessionFactorRequired());
    }

    @Test
    public void isPossessionFactorRequiredReturnsNullForRequiredFactorsLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isPossessionFactorRequired());
    }

    @Test
    public void isPossessionFactorRequiredReturnsNullForLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(new ArrayList<AuthPolicy.Location>()).isPossessionFactorRequired());
    }

    @Test
    public void isossessionFactorRequiredReturnsNulllForForRequiredFactorsJailbreakProtectionLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, true, new ArrayList<AuthPolicy.Location>()).isPossessionFactorRequired());
    }

    @Test
    public void isPossessionFactorRequiredReturnsValueForFactorRequiredConstructor() throws Exception {
        assertTrue(new AuthPolicy(true, true, true).isPossessionFactorRequired());

    }

    @Test
    public void isPossessionFactorRequiredReturnsValueForFactorRequiredLocationsConstructor() throws Exception {
        assertTrue(new AuthPolicy(true, true, true, new ArrayList<AuthPolicy.Location>()).isPossessionFactorRequired());
    }

    @Test
    public void isJailbreakProtectionEnabledReturnsNullForRequiredFactorsConstructor() throws Exception {
        assertNull(new AuthPolicy(99).isJailbreakProtectionEnabled());
    }

    @Test
    public void isJailbreakProtectionEnabledReturnsNullForRequiredFactorsLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isJailbreakProtectionEnabled());
    }

    @Test
    public void isJailbreakProtectionEnabledReturnsNullForLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(new ArrayList<AuthPolicy.Location>()).isJailbreakProtectionEnabled());
    }

    @Test
    public void isJailbreakProtectionEnabledReturnsValueForForRequiredFactorsJailbreakProtectionLocationsConstructor() throws Exception {
        assertTrue(new AuthPolicy(99, true, new ArrayList<AuthPolicy.Location>()).isJailbreakProtectionEnabled());
    }

    @Test
    public void isJailbreakProtectionEnabledReturnsNullForFactorRequiredConstructor() throws Exception {
        assertNull(new AuthPolicy(true, true, true).isJailbreakProtectionEnabled());

    }

    @Test
    public void isJailbreakProtectionEnabledReturnsNullForFactorRequiredLocationsConstructor() throws Exception {
        assertNull(new AuthPolicy(true, true, true, new ArrayList<AuthPolicy.Location>()).isJailbreakProtectionEnabled());
    }

    @Test
    public void isJailbreakProtectionEnabledReturnsValueForFactorRequiredJailbreakProtectionLocationsConstructor() throws Exception {
        assertTrue(new AuthPolicy(false, false, false, true, new ArrayList<AuthPolicy.Location>()).isJailbreakProtectionEnabled());
    }

    @Test
    public void getLocationsReturnsEmptyListForRequiredFactorsConstructor() throws Exception {
        assertEquals(new ArrayList<AuthPolicy.Location>(), new AuthPolicy(99).getLocations());
    }

    @Test
    public void getLocationsReturnsEquivalentListForRequiredFactorsLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(99, expected).getLocations());
    }

    @Test
    public void getLocationsReturnsEquivalentListForRequiredFactorsJailbreakProtectionLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(99, true, expected).getLocations());
    }

    @Test
    public void getLocationsReturnsEquivalentListForLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(expected).getLocations());
    }

    @Test
    public void getLocationsReturnsEmptyListForFactorRequiredConstructor() throws Exception {
        assertEquals(new ArrayList<AuthPolicy.Location>(), new AuthPolicy(true, true, true).getLocations());

    }

    @Test
    public void getLocationsReturnsEquivalentListForFactorRequiredLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(true, true, true, expected).getLocations());
    }

    @Test
    public void getLocationsReturnsEquivalentListForFactorRequiredJailbreakProtectionLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(true, true, true, true, expected).getLocations());
    }

    @Test
    public void getLocationsIsNotTheSameListPassedInConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertNotSame(expected, new AuthPolicy(expected).getLocations());
    }

    @Test
    public void equalWhenObjectsEqual() throws Exception {
        AuthPolicy left = new AuthPolicy(false, true,
                false, false, new ArrayList<AuthPolicy.Location>());
        AuthPolicy right = new AuthPolicy(false, true,
                false, false, new ArrayList<AuthPolicy.Location>());
        assertEquals(left, right);
    }

    @Test
    public void notEqualWhenObjectsNotEqual() throws Exception {
        assertNotEquals(new AuthPolicy(99), new AuthPolicy(98));
    }

    @Test
    public void notEqualWhenObjectsNotEqualDueToIsJailbreakEnabled() throws Exception {
        AuthPolicy left = new AuthPolicy(false, false,
                false, false, new ArrayList<AuthPolicy.Location>());
        AuthPolicy right = new AuthPolicy(false, false,
                false, true, new ArrayList<AuthPolicy.Location>());
        assertNotEquals(left, right);
    }

    @Test
    public void hashCodeEqualWhenObjectsEqual() throws Exception {
        AuthPolicy left = new AuthPolicy(false, true,
                false, false, new ArrayList<AuthPolicy.Location>());
        AuthPolicy right = new AuthPolicy(false, true,
                false, false, new ArrayList<AuthPolicy.Location>());
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenObjectsNotEqual() throws Exception {
        AuthPolicy left = new AuthPolicy(false, true,
                false, false, new ArrayList<AuthPolicy.Location>());
        AuthPolicy right = new AuthPolicy(false, false,
                false, false, new ArrayList<AuthPolicy.Location>());
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenObjectsNotEqualDueToIsJailbreakEnabled() throws Exception {
        AuthPolicy left = new AuthPolicy(false, false,
                false, false, new ArrayList<AuthPolicy.Location>());
        AuthPolicy right = new AuthPolicy(false, false,
                false, true, new ArrayList<AuthPolicy.Location>());
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(new AuthPolicy(3).toString(), containsString(AuthPolicy.class.getSimpleName()));
    }

}
