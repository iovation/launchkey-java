package com.iovation.launchkey.sdk.transport.domain;
/**
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

public class ServicesListPostResponseServiceTest {
    private ServicesListPostResponseService request;

    @Before
    public void setUp() throws Exception {
        request = new ServicesListPostResponseService(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), "Expected Name",
                "Expected Description", URI.create("https://foo.bar"), URI.create("https://fizz.buzz"), true);
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Expected Name", request.getName());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), request.getId());
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
    public void fromJSONParsesCorrectId() throws Exception {
        ServicesListPostResponseService actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"description\":null,\"icon\":null," +
                        "\"callback_url\":null,\"active\":true}",
                ServicesListPostResponseService.class);
        assertEquals(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), actual.getId());
    }

    @Test
    public void fromJSONParsesCorrectName() throws Exception {
        ServicesListPostResponseService actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"description\":null,\"icon\":null," +
                        "\"callback_url\":null,\"active\":true}",
                ServicesListPostResponseService.class);
        assertEquals("Expected Name", actual.getName());
    }

    @Test
    public void fromJSONParsesCorrectDescription() throws Exception {
        ServicesListPostResponseService actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"description\":\"Expected Description\",\"icon\":null," +
                        "\"callback_url\":null,\"active\":true}",
                ServicesListPostResponseService.class);
        assertEquals("Expected Description", actual.getDescription());
    }

    @Test
    public void fromJSONParsesCorrectIcon() throws Exception {
        ServicesListPostResponseService actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"description\":null,\"icon\":\"https://foo.bar\"," +
                        "\"callback_url\":null,\"active\":true}",
                ServicesListPostResponseService.class);
        assertEquals(URI.create("https://foo.bar"), actual.getIcon());
    }

    @Test
    public void fromJSONParsesCorrectCallbackURL() throws Exception {
        ServicesListPostResponseService actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"description\":null,\"icon\":null," +
                        "\"callback_url\":\"https://foo.bar\",\"active\":true}",
                ServicesListPostResponseService.class);
        assertEquals(URI.create("https://foo.bar"), actual.getCallbackURL());
    }

    @Test
    public void fromJSONParsesCorrectActive() throws Exception {
        ServicesListPostResponseService actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"description\":null,\"icon\":null," +
                        "\"callback_url\":null,\"active\":true}",
                ServicesListPostResponseService.class);
        assertTrue(actual.isActive());
    }

}