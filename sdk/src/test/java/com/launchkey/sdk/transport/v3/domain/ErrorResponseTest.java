package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ErrorResponseTest {
    private static final int STATUS_CODE = 12345;
    private static final String MESSAGE_CODE = "Expected Message Code";
    private static final String MESSAGE = "Expected Message";
    private ErrorResponse errorResponse;

    @Before
    public void setUp() throws Exception {
        errorResponse = new ErrorResponse(STATUS_CODE, MESSAGE_CODE, MESSAGE);
    }

    @After
    public void tearDown() throws Exception {
        errorResponse = null;
    }

    @Test
    public void getStatusCode() throws Exception {
        assertEquals(STATUS_CODE, errorResponse.getStatusCode());
    }

    @Test
    public void getMessageCode() throws Exception {
        assertEquals(MESSAGE_CODE, errorResponse.getMessageCode());
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals(MESSAGE, errorResponse.getMessage());
    }

    @Test
    public void equalsIsTrueForEquivalentObjects() throws Exception {
        assertTrue(errorResponse.equals(new ErrorResponse(STATUS_CODE, MESSAGE_CODE.toString(), MESSAGE.toString())));
    }

    @Test
    public void equalsIsFalseForDifferentStatusCode() throws Exception {
        assertFalse(errorResponse.equals(new ErrorResponse(STATUS_CODE + 1, MESSAGE_CODE.toString(), MESSAGE.toString())));
    }

    @Test
    public void equalsIsFalseForDifferentMessageCode() throws Exception {
        assertFalse(errorResponse.equals(new ErrorResponse(STATUS_CODE, MESSAGE_CODE + " X", MESSAGE.toString())));
    }

    @Test
    public void equalsIsFalseForDifferentMessage() throws Exception {
        assertFalse(errorResponse.equals(new ErrorResponse(STATUS_CODE, MESSAGE_CODE.toString(), MESSAGE + " X")));
    }

    @Test
    public void hashCodeIsSameForEquivalentObject() throws Exception {
        ErrorResponse other = new ErrorResponse(STATUS_CODE, MESSAGE_CODE.toString(), MESSAGE.toString());
        assertEquals(errorResponse.hashCode(), other.hashCode());
    }

    @Test
    public void hashCodeIsDifferentForDifferentObjects() throws Exception {
        ErrorResponse other = new ErrorResponse(STATUS_CODE + 1, MESSAGE_CODE.toString(), MESSAGE.toString());
        assertNotEquals(errorResponse.hashCode(), other.hashCode());
    }

    @Test
    public void toStringHasClassName() throws Exception {
        assertTrue(
                errorResponse.toString() + " should contain " + errorResponse.getClass().getSimpleName(),
                errorResponse.toString().contains(errorResponse.getClass().getSimpleName())
        );
    }

    @Test
    public void jsonParsingWorksProperly() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse expected = new ErrorResponse(400, "40422", "Credentials incorrect for app and app secret");
        String json = "{" +
                "\"successful\": false," +
                " \"status_code\": 400," +
                " \"message\": \"Credentials incorrect for app and app secret\"," +
                " \"message_code\": 40422, \"response\": \"\"" +
                "}";
        ErrorResponse actual = mapper.readValue(json, ErrorResponse.class);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenMessageCodeNotPresent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse expected = new ErrorResponse(400, "40422", "Credentials incorrect for app and app secret");
        String json = "{" +
                "\"successful\": false," +
                " \"status_code\": 400," +
                " \"message\": \"Credentials incorrect for app and app secret\"" +
                "}";
        mapper.readValue(json, ErrorResponse.class);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenMessageNotPresent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse expected = new ErrorResponse(400, "40422", "Credentials incorrect for app and app secret");
        String json = "{" +
                "\"successful\": false," +
                " \"status_code\": 400," +
                " \"message_code\": 40422, \"response\": \"\"" +
                "}";
        mapper.readValue(json, ErrorResponse.class);
    }
}
