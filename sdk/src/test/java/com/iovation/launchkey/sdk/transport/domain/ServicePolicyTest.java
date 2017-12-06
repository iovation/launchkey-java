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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iovation.launchkey.sdk.transport.domain.ServicePolicy.TimeFence;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class ServicePolicyTest {
    @Test
    public void equalsIsFalseForDifferentTimeFences() throws Exception {
        ServicePolicy left = new ServicePolicy(0, false, false, false, false);
        ServicePolicy right = new ServicePolicy(0, false, false, false, false);
        right.addTimeFence(new TimeFence("name", null, 0, 0, 0, 0, null));
        assertFalse(left.equals(right));
    }

    @Test
    public void fullObjectMapperMapsAsExpected() throws Exception {
        String expected =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]," +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"geofence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"locations\":[" +
                        "{\"radius\":1.1,\"latitude\":2.1,\"longitude\":3.1}," +
                        "{\"radius\":1.2,\"latitude\":2.2,\"longitude\":3.2}" +
                        "]" +
                        "}" +
                        "},{" +
                        "\"factor\":\"device integrity\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"factor enabled\":1" +
                        "}" +
                        "},{" +
                        "\"factor\":\"timefence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"time fences\":[" +
                        "{\"name\":\"tf1\",\"days\":[\"Monday\",\"Tuesday\",\"Wednesday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}," +
                        "{\"name\":\"tf2\",\"days\":[\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}" +
                        "]" +
                        "}" +
                        "}" +
                        "]" +
                        "}";
        ServicePolicy policy = new ServicePolicy(2, true, true, true, true);
        policy.addGeoFence(1.1, 2.1, 3.1);
        policy.addGeoFence(1.2, 2.2, 3.2);
        policy.addTimeFence(
                new TimeFence("tf1", Arrays.asList("Monday", "Tuesday", "Wednesday"), 0, 23, 0, 59,
                        "America/Los_Angeles"));
        policy.addTimeFence(
                new TimeFence("tf2", Arrays.asList("Thursday", "Friday", "Saturday", "Sunday"), 0, 23, 0,
                        59, "America/Los_Angeles"));

        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void mapperMapsNoMinimumRequirementsWhenNoneAreSpecified() throws Exception {
        String expected =
                "{" +
                        "\"minimum_requirements\":[]," +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"device integrity\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"factor enabled\":1" +
                        "}" +
                        "}" +
                        "]" +
                        "}";
        ServicePolicy policy = new ServicePolicy(null, null, null, null, true);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void mapperMapsNoMinimumRequirementsAndNoFactorsWhenNoneAreSpecified() throws Exception {
        String expected =
                "{" +
                        "\"minimum_requirements\":[]," +
                        "\"factors\":[]" +
                        "}";
        ServicePolicy policy = new ServicePolicy(null, null, null, null, null);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }

    @Test
    public void objectParsesAsExpected() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]," +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"geofence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"locations\":[" +
                        "{\"radius\":1.1,\"latitude\":2.1,\"longitude\":3.1}," +
                        "{\"radius\":1.2,\"latitude\":2.2,\"longitude\":3.2}" +
                        "]" +
                        "}" +
                        "},{" +
                        "\"factor\":\"device integrity\"," +
                        "\"attributes\":{" +
                        "\"factor enabled\":\"1\"" +
                        "}" +
                        "},{" +
                        "\"factor\":\"timefence\"," +
                        "\"requirement\":\"forced requirement\"," +
                        "\"priority\":1," +
                        "\"attributes\":{" +
                        "\"time fences\":[" +
                        "{\"name\":\"tf1\",\"days\":[\"Monday\",\"Tuesday\",\"Wednesday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}," +
                        "{\"name\":\"tf2\",\"days\":[\"Thursday\",\"Friday\",\"Saturday\",\"Sunday\"],\"start hour\":0,\"end hour\":23,\"start minute\":0,\"end minute\":59,\"timezone\":\"America/Los_Angeles\"}" +
                        "]" +
                        "}" +
                        "}" +
                        "]" +
                        "}";
        ServicePolicy expected = new ServicePolicy(2, true, true, true, true);
        expected.addGeoFence(1.1, 2.1, 3.1);
        expected.addGeoFence(1.2, 2.2, 3.2);
        expected.addTimeFence(
                new TimeFence("tf1", Arrays.asList("Monday", "Tuesday", "Wednesday"), 0, 23, 0, 59,
                        "America/Los_Angeles"));
        expected.addTimeFence(
                new TimeFence("tf2", Arrays.asList("Thursday", "Friday", "Saturday", "Sunday"), 0, 23, 0,
                        59, "America/Los_Angeles"));
        ServicePolicy actual = new ObjectMapper().readValue(json, ServicePolicy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void objectParsesNoMinimumRequirementsWhenThereAreNone() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[]," +
                        "\"factors\":[" +
                        "{" +
                        "\"factor\":\"device integrity\"," +
                        "\"attributes\":{" +
                        "\"factor enabled\":\"1\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}";
        ServicePolicy expected = new ServicePolicy(null, null, null, null, true);
        ServicePolicy actual = new ObjectMapper().readValue(json, ServicePolicy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void objectParsesMinimumRequirementsAllOnlyWhenOnlySpecified() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"any\":2" +
                        "}]," +
                        "\"factors\":[]" +
                        "}";
        ServicePolicy expected = new ServicePolicy(2, null, null, null, null);
        ServicePolicy actual = new ObjectMapper().readValue(json, ServicePolicy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void objectParsesMinimumRequirementsTypesOnlyWhenOnlySpecified() throws Exception {
        String json =
                "{" +
                        "\"minimum_requirements\":[{" +
                        "\"requirement\":\"authenticated\"," +
                        "\"knowledge\":1," +
                        "\"inherence\":1," +
                        "\"possession\":1" +
                        "}]," +
                        "\"factors\":[]" +
                        "}";
        ServicePolicy expected = new ServicePolicy(null, true, true, true, null);
        ServicePolicy actual = new ObjectMapper().readValue(json, ServicePolicy.class);
        assertEquals(expected, actual);
    }

    @Test
    public void getTimeFences() throws Exception {
        List<TimeFence> expected = Arrays.asList(mock(TimeFence.class), mock(TimeFence.class));
        ServicePolicy policy  = new ServicePolicy(0, false, false, false, false);
        policy.addTimeFence(expected.get(0));
        policy.addTimeFence(expected.get(1));
        assertEquals(expected, policy.getTimeFences());
    }
}