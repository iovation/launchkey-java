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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServicesPostRequestTest {
    private ServicesPostRequest request;

    @Before
    public void setUp() throws Exception {
        request = new ServicesPostRequest("Expected Name", URI.create("https://foo.bar"), "Expected Description",
                URI.create("https://fizz.buzz"), true);
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Expected Name", request.getName());
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
        String expected =  "{\"name\":\"Expected Name\",\"icon\":\"https://foo.bar\"," +
                "\"description\":\"Expected Description\",\"callback_url\":\"https://fizz.buzz\",\"active\":true}";
        assertEquals(expected, actual);
    }

}