package com.launchkey.sdk.transport.v1.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ErrorResponseTest {
    private ErrorResponse errorResponse;

    @Before
    public void setUp() throws Exception {
        errorResponse = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
    }

    @After
    public void tearDown() throws Exception {
        errorResponse = null;
    }

    @Test
    public void testGetStatusCode() throws Exception {
        assertEquals(400, errorResponse.getStatusCode());
    }

    @Test
    public void testGetMessageCode() throws Exception {
        assertEquals(40422, errorResponse.getMessageCode());
    }

    @Test
    public void testGetMessage() throws Exception {
        assertEquals("Credentials incorrect for app and app secret", errorResponse.getMessage());
    }


    @Test
    public void testJSONParsable() throws Exception {
        String json = "{\"status_code\": 400, \"message\": \"Credentials incorrect for app and app secret\", \"message_code\": 40422}";
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse actual = ErrorResponse.factory(mapper.readTree(json));
        assertEquals(errorResponse, actual);
    }

    @Test
    public void testFailingErrorJSON() throws Exception {
        String json = "{\"successful\": false, \"status_code\": 400, \"message\": {\"status\": \"Value should be 'true' or 'false'\"}, \"message_code\": 50441, \"response\": \"\"}";
        ErrorResponse expected = new ErrorResponse(400, 50441, "{\"status\":\"Value should be 'true' or 'false'\"}");
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse actual = ErrorResponse.factory(mapper.readTree(json));
        assertEquals(expected, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"successful\": false, \"status_code\": 400, \"message\": \"Credentials incorrect for app and app secret\", \"message_code\": 40422, \"response\": \"\"}";
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse actual = ErrorResponse.factory(mapper.readTree(json));
        assertEquals(errorResponse, actual);
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        ErrorResponse left = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
        ErrorResponse right = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        ErrorResponse left = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
        ErrorResponse right = new ErrorResponse(401, 40422, "Credentials incorrect for app and app secret");
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        ErrorResponse left = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
        ErrorResponse right = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
        assertEquals(left.hashCode(), right.hashCode());

    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        ErrorResponse left = new ErrorResponse(400, 40422, "Credentials incorrect for app and app secret");
        ErrorResponse right = new ErrorResponse(401, 40422, "Credentials incorrect for app and app secret");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(errorResponse.toString(), containsString(ErrorResponse.class.getSimpleName()));
    }
}
