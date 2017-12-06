package com.iovation.launchkey.sdk.domain.servicemanager; /**
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

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ServicePolicyTest {
    @Test
    public void getRequiredFactors() throws Exception {
        assertEquals(Integer.valueOf(1), new ServicePolicy(1, false).getRequiredFactors());
    }

    @Test
    public void isJailBreakProtectionEnabledRequiredFactorsConstructor() throws Exception {
        assertEquals(true, new ServicePolicy(1, true).isJailBreakProtectionEnabled());
    }

    @Test
    public void isKnowledgeFactorRequired() throws Exception {
        assertTrue(new ServicePolicy(true, false, false, false).isKnowledgeFactorRequired());
    }

    @Test
    public void isInherenceFactorRequired() throws Exception {
        assertTrue(new ServicePolicy(false, true, false, false).isInherenceFactorRequired());
    }

    @Test
    public void isPossessionFactorRequired() throws Exception {
        assertTrue(new ServicePolicy(false, false, true, false).isPossessionFactorRequired());
    }

    @Test
    public void isJailBreakProtectionEnabledIndividualFactorsConstructor() throws Exception {
        assertEquals(true, new ServicePolicy(false, false, false, true).isJailBreakProtectionEnabled());
    }

    @Test
    public void getLocations() throws Exception {
        List<ServicePolicy.Location> expected = Arrays.asList(mock(ServicePolicy.Location.class), mock(
                ServicePolicy.Location.class));
        ServicePolicy policy = new ServicePolicy();
        policy.addLocations(expected);
        assertEquals(expected, policy.getLocations());
    }

    @Test
    public void equalsIsTrueForSameValues() throws Exception {
        assertTrue(new ServicePolicy().equals(new ServicePolicy()));
    }

    @Test
    public void equalsIsFalseForDifferentRequiredFactors() throws Exception {
        assertFalse(new ServicePolicy(1, false).equals(new ServicePolicy(2, false)));
    }

    @Test
    public void equalsIsFalseForDifferentKnowledgeFactorRequired() throws Exception {
        assertFalse(new ServicePolicy(true, false, false, false).equals(new ServicePolicy(false, false, false, false)));
    }

    @Test
    public void equalsIsFalseForDifferentInherenceFactorRequired() throws Exception {
        assertFalse(new ServicePolicy(false, true, false, false).equals(new ServicePolicy(false, false, false, false)));
    }

    @Test
    public void equalsIsFalseForDifferentPossessionFactorRequired() throws Exception {
        assertFalse(new ServicePolicy(false, false, true, false).equals(new ServicePolicy(false, false, false, false)));
    }

    @Test
    public void equalsIsFalseForDifferentJailBreakProtectedEnabled() throws Exception {
        assertFalse(new ServicePolicy(false, false, false, true).equals(new ServicePolicy(false, false, false, false)));
    }

    @Test
    public void equalsIsFalseForDifferentLocations() throws Exception {
        ServicePolicy left = new ServicePolicy();
        left.addLocations(Collections.singletonList(mock(ServicePolicy.Location.class)));
        ServicePolicy right = new ServicePolicy();
        right.addLocations(Collections.singletonList(mock(ServicePolicy.Location.class)));
        assertFalse(left.equals(right));
    }

    @Test
    public void equalsIsFalseForDifferentTimeFences() throws Exception {
        ServicePolicy left = new ServicePolicy();
        left.addTimeFences(Collections.singletonList(mock(ServicePolicy.TimeFence.class)));
        ServicePolicy right = new ServicePolicy();
        right.addTimeFences(Collections.singletonList(mock(ServicePolicy.TimeFence.class)));
        assertFalse(left.equals(right));
    }

    @Test
    public void toStringHasClassName() throws Exception {
        assertThat(new ServicePolicy().toString(), containsString(ServicePolicy.class.getSimpleName()));
    }

}