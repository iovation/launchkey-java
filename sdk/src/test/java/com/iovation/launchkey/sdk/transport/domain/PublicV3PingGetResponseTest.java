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

package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class PublicV3PingGetResponseTest {
    private PublicV3PingGetResponse publicPingGetResponse;

    @Rule public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Date apiTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2010-01-01T00:00:00Z");
        publicPingGetResponse = new PublicV3PingGetResponse(apiTime);
    }

    @After
    public void tearDown() throws Exception {
        publicPingGetResponse = null;
    }

    @Test
    public void jsonParseWSetsExpectedTimeValue() throws Exception {
        Date expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2010-01-01T00:00:00Z");
        Date actual = new ObjectMapper().readValue(
                "{\"api_time\": \"2010-01-01T00:00:00Z\"}",
                PublicV3PingGetResponse.class
        ).getApiTime();
        assertEquals(expected, actual);
    }

    @Test
    public void jsonParseWithNoApiTimeErrors() throws Exception {
        thrown.expect(JsonMappingException.class);
        new ObjectMapper().readValue("{}", PublicV3PingGetResponse.class);
    }

    @Test
    public void jsonParseWithExtraAttributeDoesNotError() throws Exception {
        new ObjectMapper().readValue(
                "{\"api_time\": \"2000-01-01T00:00:00Z\", \"extra\": true}",
                PublicV3PingGetResponse.class
        );
    }

    @Test
    public void testApiTimeGetterReturnsValueFromConstructor() throws Exception {
        assertEquals(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2010-01-01T00:00:00Z"),
                publicPingGetResponse.getApiTime()
        );
    }
}