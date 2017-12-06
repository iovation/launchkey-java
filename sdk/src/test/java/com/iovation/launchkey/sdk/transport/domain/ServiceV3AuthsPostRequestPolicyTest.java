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

import static org.junit.Assert.*;

public class ServiceV3AuthsPostRequestPolicyTest {
    @Test
    public void objectMapperMapsAsExpected() throws Exception {
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
                        "}" +
                    "]" +
                "}";
        @SuppressWarnings("deprecation") ServiceV3AuthsPostRequestPolicy policy = new ServiceV3AuthsPostRequestPolicy(2, true, true, true);
        policy.addGeoFence(1.1, 2.1, 3.1);
        policy.addGeoFence(1.2, 2.2, 3.2);
        String actual = new ObjectMapper().writeValueAsString(policy);
        assertEquals(expected, actual);
    }
}