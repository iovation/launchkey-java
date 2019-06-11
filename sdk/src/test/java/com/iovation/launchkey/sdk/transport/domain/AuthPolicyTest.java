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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class AuthPolicyTest {
    @Test
    public void objectMapperMapsAsExpectedForServicePolicy() throws Exception {
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
                        "}" +
                    "]" +
                "}";
        AuthPolicy policy = new AuthPolicy(2, true, true, true, true);
        policy.addGeoFence(1.1, 2.1, 3.1);
        policy.addGeoFence(1.2, 2.2, 3.2);
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
                        "}" +
                    "]" +
                "}";
        AuthPolicy expected = new AuthPolicy(2, true, true, true, true);
        expected.addGeoFence(1.1, 2.1, 3.1);
        expected.addGeoFence(1.2, 2.2, 3.2);
        AuthPolicy actual = new ObjectMapper().readValue(json, AuthPolicy.class);
        assertEquals(expected, actual);
    }
    @Test
    public void objectMapperMapsAsExpectedForAuthResponsePolicyWithAmount() throws Exception {
        String policy =
                "{\n" +
                        "  \"requirement\": \"amount\",\n" +
                        "  \"amount\": 2,\n" +
                        "  \"geofences\": [\n" +
                        "    {\n" +
                        "      \"name\": null,\n" +
                        "      \"latitude\": 36.120825,\n" +
                        "      \"longitude\": -115.157216,\n" +
                        "      \"radius\": 200\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
        AuthPolicy expected = new AuthPolicy(2, null, null, null, null);
        expected.addGeoFence(200.0, 36.120825, -115.157216);
        AuthPolicy actual = new ObjectMapper().readValue(policy, AuthPolicy.class);
        assertEquals(expected, actual);
    }
    @Test
    public void objectMapperMapsAsExpectedForAuthResponsePolicyWithTypes() throws Exception {
        String policy =
                "{\n" +
                        "  \"requirement\": \"types\",\n" +
                        "  \"types\": [\"knowledge\"],\n" +
                        "  \"geofences\": [\n" +
                        "    {\n" +
                        "      \"name\": null,\n" +
                        "      \"latitude\": 36.120825,\n" +
                        "      \"longitude\": -115.157216,\n" +
                        "      \"radius\": 200\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
        AuthPolicy expected = new AuthPolicy(null, false, true, false, null);
        expected.addGeoFence(200.0, 36.120825, -115.157216);
        AuthPolicy actual = new ObjectMapper().readValue(policy, AuthPolicy.class);
        assertEquals(expected, actual);
    }
    @Test
    public void objectMapperMapsAsExpectedForAuthResponsePolicyWithJustGeofences() throws Exception {
        String policy =
                "{\n" +
                        "  \"requirement\": null,\n" +
                        "  \"geofences\": [\n" +
                        "    {\n" +
                        "      \"name\": null,\n" +
                        "      \"latitude\": 36.120825,\n" +
                        "      \"longitude\": -115.157216,\n" +
                        "      \"radius\": 200\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
        AuthPolicy expected = new AuthPolicy(null, null, null, null, null);
        expected.addGeoFence(200.0, 36.120825, -115.157216);
        AuthPolicy actual = new ObjectMapper().readValue(policy, AuthPolicy.class);
        assertEquals(expected, actual);
    }
    @Test
    public void objectMapperMapsAsExpectedForAuthResponsePolicyWithJustGeofencesAndANonNullGeofenceName() throws Exception {
        String policy =
                "{\n" +
                        "  \"requirement\": null,\n" +
                        "  \"geofences\": [\n" +
                        "    {\n" +
                        "      \"name\": \"asdf\",\n" +
                        "      \"latitude\": 36.120825,\n" +
                        "      \"longitude\": -115.157216,\n" +
                        "      \"radius\": 200\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}";
        AuthPolicy expected = new AuthPolicy(null, null, null, null, null);
        expected.addGeoFence("asdf", 200.0, 36.120825, -115.157216);
        AuthPolicy actual = new ObjectMapper().readValue(policy, AuthPolicy.class);
        assertEquals(expected, actual);
    }
}