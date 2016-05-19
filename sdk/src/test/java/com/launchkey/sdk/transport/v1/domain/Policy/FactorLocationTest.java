package com.launchkey.sdk.transport.v1.domain.Policy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
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
public class FactorLocationTest {
    private Factor.Location location;

    @Before
    public void setUp() throws Exception {
        location = new Factor.Location(10.0, 11.1, 12.2);
    }

    @After
    public void tearDown() throws Exception {
        location = null;
    }

    @Test
    public void getRadius() throws Exception {
        assertEquals(10.0, location.getRadius(), 0.001);
    }

    @Test
    public void getLatitude() throws Exception {
        assertEquals(11.1, location.getLatitude(), 0.001);
    }

    @Test
    public void getLongitude() throws Exception {
        assertEquals(12.2, location.getLongitude(), 0.001);
    }

    @Test
    public void equalsWhenLocationsEqual() throws Exception {
        assertEquals(location, new Factor.Location(10.0, 11.1, 12.2));
    }

    @Test
    public void notEqualsWhenLocationsNotEqual() throws Exception {
        assertNotEquals(location, new Factor.Location(1, 2, 3));
    }

    @Test
    public void hashCodeIsEqualWhenLocationsEqual() throws Exception {
        Factor.Location o = new Factor.Location(10.0, 11.1, 12.2);
        assertEquals(location.hashCode(), o.hashCode());
    }

    @Test
    public void hashCodeIsNotEqualWhenLocationsNotEqual() throws Exception {
        assertNotEquals(location.hashCode(), new Factor.Location(1, 2, 3).hashCode());
    }

    @Test
    public void toStringHasClassInValue() throws Exception {
        assertThat(location.toString(), containsString(Factor.Location.class.getSimpleName()));
    }

    @Test
    public void jsonEncode() throws Exception {
        String expected = "{" +
                    "\"radius\":10.0," +
                    "\"latitude\":11.1," +
                    "\"longitude\":12.2" +
                "}"
                ;

        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(location);
        assertEquals(expected, actual);
    }
}
