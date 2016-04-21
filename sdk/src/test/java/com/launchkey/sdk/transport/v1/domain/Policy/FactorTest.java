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
public class FactorTest {
    private Factor factor;
    private ArrayList<Factor.Location> locations;

    @Before
    public void setUp() throws Exception {
        locations = new ArrayList<Factor.Location>();
        locations.add(new Factor.Location(10.0, 11.1, 12.2));
        locations.add(new Factor.Location(20.0, 21.1, 22.2));
        factor = new Factor(
                Factor.Type.GEOFENCE,
                true,
                Factor.Requirement.FORCED,
                5,
                new Factor.Attributes(locations)
        );
    }

    @After
    public void tearDown() throws Exception {
        factor = null;
    }

    @Test
    public void getType() throws Exception {
        assertEquals(Factor.Type.GEOFENCE, factor.getType());
    }

    @Test
    public void isQuickFail() throws Exception {
        assertTrue(factor.isQuickFail());
    }

    @Test
    public void getRequirement() throws Exception {
        assertEquals(Factor.Requirement.FORCED, factor.getRequirement());
    }

    @Test
    public void getPriority() throws Exception {
        assertEquals(5, factor.getPriority());
    }

    @Test
    public void getAttributesHasLocations() throws Exception {
        assertEquals(locations, factor.getAttributes().getLocations());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void attributesLocationsIsImmutable() throws Exception {
        factor.getAttributes().getLocations().add(new Factor.Location(1, 1, 1));
    }

    @Test
    public void equalsWithEqualObjectsReturnsTrue() throws Exception {
        Factor o = new Factor(
                Factor.Type.GEOFENCE,
                true,
                Factor.Requirement.FORCED,
                5,
                new Factor.Attributes(locations)
        );
        assertEquals(o, factor);
    }

    @Test
    public void equalsWithUnequalObjectsReturnsFalse() throws Exception {
        Factor o = new Factor(
                Factor.Type.COMBO_LOCK,
                true,
                Factor.Requirement.FORCED,
                5,
                new Factor.Attributes(locations)
        );
        assertNotEquals(o, factor);
    }

    @Test
    public void hashCodeOfEqualObjectsIsEqual() throws Exception {
        Factor o = new Factor(
                Factor.Type.GEOFENCE,
                true,
                Factor.Requirement.FORCED,
                5,
                new Factor.Attributes(locations)
        );
        assertEquals(o.hashCode(), factor.hashCode());
    }

    @Test
    public void hashCodeOfUnequalObjectsIsNotEqual() throws Exception {
        Factor o = new Factor(
                Factor.Type.COMBO_LOCK,
                true,
                Factor.Requirement.FORCED,
                5,
                new Factor.Attributes(locations)
        );
        assertNotEquals(o.hashCode(), factor.hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(factor.toString(), containsString(Factor.class.getSimpleName()));

    }

    @Test
    public void testJSONEncodeWithLocations() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String expected = "{" +
                "\"factor\":\"geofence\"," +
                "\"requirement\":\"forced requirement\"," +
                "\"quickfail\":true," +
                "\"priority\":5," +
                "\"attributes\":{" +
                "\"locations\":[" +
                "{" +
                "\"radius\":10.0," +
                "\"latitude\":11.1," +
                "\"longitude\":12.2" +
                "},{" +
                "\"radius\":20.0," +
                "\"latitude\":21.1," +
                "\"longitude\":22.2" +
                "}" +
                "]" +
                "}" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(factor);
        assertEquals(expected, actual);

    }

    @Test
    public void testJSONEncodeWithoutLocations() throws Exception {
        @SuppressWarnings("SpellCheckingInspection")
        String expected = "{" +
                "\"factor\":\"pin lock\"," +
                "\"requirement\":\"allowed\"," +
                "\"quickfail\":false," +
                "\"priority\":1," +
                "\"attributes\":{" +
                "\"locations\":[]" +
                "}" +
                "}";

        factor = new Factor(
                Factor.Type.PIN_LOCK,
                false,
                Factor.Requirement.ALLOWED,
                1,
                new Factor.Attributes(new ArrayList<Factor.Location>())
        );

        ObjectMapper mapper = new ObjectMapper();
        String actual = mapper.writeValueAsString(factor);
        assertEquals(expected, actual);

    }
}