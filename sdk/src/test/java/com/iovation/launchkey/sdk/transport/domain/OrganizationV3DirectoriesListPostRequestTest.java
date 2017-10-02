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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class OrganizationV3DirectoriesListPostRequestTest {
    private OrganizationV3DirectoriesListPostRequest request;

    @Before
    public void setUp() throws Exception {
        request = new OrganizationV3DirectoriesListPostRequest(Arrays.asList(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"),
                UUID.fromString("f9842d14-5734-4d09-b1d3-2ca64413d4a6")));
    }

    @Test
    public void getServiceIds() throws Exception {
        List<UUID> expected = Arrays.asList(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"),
                UUID.fromString("f9842d14-5734-4d09-b1d3-2ca64413d4a6"));
        assertEquals(expected, request.getDirectoryIds());
    }

    @Test
    public void toJSON() throws Exception {
        String actual = new ObjectMapper().writeValueAsString(request);
        String expected = "{\"directory_ids\":[\"fac25a3c-af79-49df-bd65-777e9c86e288\"," +
                "\"f9842d14-5734-4d09-b1d3-2ca64413d4a6\"]}";
        assertEquals(expected, actual);
    }

}