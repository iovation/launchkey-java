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
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServicesPatchRequestTest {
    private ServicesPatchRequest request;

    @Before
    public void setUp() throws Exception {
        request = new ServicesPatchRequest(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), "Expected Name",
                "Expected Description", URI.create("https://foo.bar"), URI.create("https://fizz.buzz"), true);
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Expected Name", request.getName());
    }

    @Test
    public void getServiceId() throws Exception {
        assertEquals(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), request.getServiceId());
    }

    @Test
    public void getIcon() throws Exception {
        assertEquals(URI.create("https://foo.bar"), request.getIcon());
    }

    @Test
    public void getDescription() throws Exception {
        assertEquals("Expected Description", request.getDescription());
    }

    @Test
    public void getCallbackUrl() throws Exception {
        assertEquals(URI.create("https://fizz.buzz"), request.getCallbackURL());
    }

    @Test
    public void isActive() throws Exception {
        assertTrue(request.isActive());
    }

    @Test
    public void toJSON() throws Exception {
        String actual = new ObjectMapper().writeValueAsString(request);
        String expected = "{\"service_id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                "\"description\":\"Expected Description\",\"icon\":\"https://foo.bar\"," +
                "\"callback_url\":\"https://fizz.buzz\",\"active\":true}";
        assertEquals(expected, actual);
    }

}