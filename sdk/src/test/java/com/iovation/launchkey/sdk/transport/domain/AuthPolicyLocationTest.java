package com.iovation.launchkey.sdk.transport.domain; /**
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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AuthPolicyLocationTest {
    @Test
    public void getName() throws Exception {
        assertEquals("Expected", new AuthPolicy.Location("Expected", 1.0, 2.0, 3.0).getName());
    }

    @Test
    public void getRadius() throws Exception {
        assertEquals(1.0, new AuthPolicy.Location("Expected", 1.0, 2.0, 3.0).getRadius(), 0.0);
    }

    @Test
    public void getLatitude() throws Exception {
        assertEquals(2.0, new AuthPolicy.Location("Expected", 1.0, 2.0, 3.0).getLatitude(), 0.0);
    }

    @Test
    public void getLongitude() throws Exception {
        assertEquals(3.0, new AuthPolicy.Location("Expected", 1.0, 2.0, 3.0).getLongitude(), 0.0);
    }

    @Test
    public void equalsIsTrueForSameValues() throws Exception {
        assertTrue(new AuthPolicy.Location("name", 1.0, 2.0, 3.0)
                .equals(new AuthPolicy.Location("name", 1.0, 2.0, 3.0)));
    }

    @Test
    public void equalsIsTFalseForDifferentName() throws Exception {
        assertFalse(new AuthPolicy.Location("name1", 1.0, 2.0, 3.0)
                .equals(new AuthPolicy.Location("name2", 1.0, 2.0, 3.0)));
    }

    @Test
    public void equalsIsTFalseForDifferentRadius() throws Exception {
        assertFalse(new AuthPolicy.Location("name", 1.1, 2.0, 3.0)
                .equals(new AuthPolicy.Location("name", 1.2, 2.0, 3.0)));
    }

    @Test
    public void equalsIsTFalseForDifferentLatitude() throws Exception {
        assertFalse(new AuthPolicy.Location("name", 1.0, 2.1, 3.0)
                .equals(new AuthPolicy.Location("name", 1.0, 2.2, 3.0)));
    }

    @Test
    public void equalsIsTFalseForDifferentLongitude() throws Exception {
        assertFalse(new AuthPolicy.Location("name", 1.0, 2.0, 3.1)
                .equals(new AuthPolicy.Location("name", 1.0, 2.0, 3.2)));
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(new AuthPolicy.Location(null, 1.0, 2.0, 3.0).toString(),
                containsString(AuthPolicy.Location.class.getSimpleName()));
    }

}