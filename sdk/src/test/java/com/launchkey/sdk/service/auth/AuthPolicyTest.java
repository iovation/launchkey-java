package com.launchkey.sdk.service.auth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
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
        assertEquals(99, new AuthPolicy(99).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsValueForRequiredFactorsLocationsConstructor() throws Exception {
        assertEquals(99, new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsZeroForLocationsConstructor() throws Exception {
        assertEquals(0, new AuthPolicy(new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsZeroForFactorRequiredConstructor() throws Exception {
        assertEquals(0, new AuthPolicy(true, true, true).getRequiredFactors());
    }

    @Test
    public void getRequiredFactorsReturnsZeroForFactorRequiredLocationsConstructor() throws Exception {
        assertEquals(0, new AuthPolicy(true, true, true, new ArrayList<AuthPolicy.Location>()).getRequiredFactors());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsFalseForRequiredFactorsConstructor() throws Exception {
        assertFalse(new AuthPolicy(99).isKnowledgeFactorRequired());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsFalseForRequiredFactorsLocationsConstructor() throws Exception {
        assertFalse(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isKnowledgeFactorRequired());
    }

    @Test
    public void isKnowledgeFactorRequiredReturnsFalseForLocationsConstructor() throws Exception {
        assertFalse(new AuthPolicy(new ArrayList<AuthPolicy.Location>()).isKnowledgeFactorRequired());
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
    public void isInherenceFactorRequiredReturnsFalseForRequiredFactorsConstructor() throws Exception {
        assertFalse(new AuthPolicy(99).isInherenceFactorRequired());
    }

    @Test
    public void isInherenceFactorRequiredReturnsFalseForRequiredFactorsLocationsConstructor() throws Exception {
        assertFalse(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isInherenceFactorRequired());
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
    public void isPossessionFactorRequiredReturnsFalseForRequiredFactorsConstructor() throws Exception {
        assertFalse(new AuthPolicy(99).isPossessionFactorRequired());
    }

    @Test
    public void isPossessionFactorRequiredReturnsFalseForRequiredFactorsLocationsConstructor() throws Exception {
        assertFalse(new AuthPolicy(99, new ArrayList<AuthPolicy.Location>()).isPossessionFactorRequired());
    }

    @Test
    public void isPossessionFactorRequiredReturnsFalseForLocationsConstructor() throws Exception {
        assertFalse(new AuthPolicy(new ArrayList<AuthPolicy.Location>()).isPossessionFactorRequired());
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
    public void getLocationsReturnsEmptyListForRequiredFactorsConstructor() throws Exception {
        assertEquals(new ArrayList<AuthPolicy.Location>(), new AuthPolicy(99).getLocations());
    }

    @Test
    public void getLocationsReturnsEquivalentListForRequiredFactorsLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<AuthPolicy.Location>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(99, expected).getLocations());
    }

    @Test
    public void getLocationsReturnsEquivalentListForLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<AuthPolicy.Location>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(expected).getLocations());
    }

    @Test
    public void getLocationsReturnsEmptyListForFactorRequiredConstructor() throws Exception {
        assertEquals(new ArrayList<AuthPolicy.Location>(), new AuthPolicy(true, true, true).getLocations());

    }

    @Test
    public void getLocationsReturnsEquivalentListForFactorRequiredLocationsConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<AuthPolicy.Location>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertEquals(expected, new AuthPolicy(true, true, true, expected).getLocations());
    }

    @Test
    public void getLocationsIsNotTheSameListPassedInConstructor() throws Exception {
        List<AuthPolicy.Location> expected = new ArrayList<AuthPolicy.Location>();
        expected.add(new AuthPolicy.Location(1.1, 2.2, 3.3));
        assertNotSame(expected, new AuthPolicy(expected).getLocations());
    }

    @Test
    public void equalWhenObjectsEqual() throws Exception {
        assertEquals(new AuthPolicy(true, false, true), new AuthPolicy(true, false, true));
    }

    @Test
    public void notEqualWhenObjectsNotEqual() throws Exception {
        assertNotEquals(new AuthPolicy(99), new AuthPolicy(98));
    }

    @Test
    public void hashCodeEqualWhenObjectsEqual() throws Exception {
        assertEquals(new AuthPolicy(false, true, false).hashCode(), new AuthPolicy(false, true, false).hashCode());
    }

    @Test
    public void hashCodeNotEqualWhenObjectsNotEqual() throws Exception {
        assertNotEquals(new AuthPolicy(99).hashCode(), new AuthPolicy(98).hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(new AuthPolicy(3).toString(), containsString(AuthPolicy.class.getSimpleName()));
    }

}
