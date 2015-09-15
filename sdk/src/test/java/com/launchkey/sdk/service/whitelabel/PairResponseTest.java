package com.launchkey.sdk.service.whitelabel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class PairResponseTest {

    private PairResponse pairResponse;

    @Before
    public void setUp() throws Exception {
        pairResponse = new PairResponse("zje0ja5", "https://dashboard.launchkey.com/qrcode/zje0ja5");
    }

    @After
    public void tearDown() throws Exception {
        pairResponse = null;
    }

    @Test
    public void testGetCode() throws Exception {
        assertEquals("zje0ja5", pairResponse.getCode());
    }

    @Test
    public void testGetQrCodeUrl() throws Exception {
        assertEquals("https://dashboard.launchkey.com/qrcode/zje0ja5", pairResponse.getQrCodeUrl());
    }

    @Test
    public void testJSONParseable() throws Exception {
        String json = "{\"qrcode\": \"https://dashboard.launchkey.com/qrcode/zje0ja5\",\"code\":\"zje0ja5\"}";
        ObjectMapper mapper = new ObjectMapper();
        PairResponse actual = mapper.readValue(json, PairResponse.class);
        assertEquals(pairResponse, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"qrcode\": \"https://dashboard.launchkey.com/qrcode/zje0ja5\"," +
                "\"code\":\"zje0ja5\"," +
                "\"unknown\": \"Unknown Value\"}";
        ObjectMapper mapper = new ObjectMapper();
        PairResponse actual = mapper.readValue(json, PairResponse.class);
        assertEquals(pairResponse, actual);
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        PairResponse left = new PairResponse("code", "url");
        PairResponse right = new PairResponse("code", "url");
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        PairResponse left = new PairResponse("Left", "url");
        PairResponse right = new PairResponse("Right", "url");
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        PairResponse left = new PairResponse("code", "url");
        PairResponse right = new PairResponse("code", "url");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        PairResponse left = new PairResponse("Left", "url");
        PairResponse right = new PairResponse("Right", "url");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(pairResponse.toString(), containsString(PairResponse.class.getSimpleName()));
    }
}