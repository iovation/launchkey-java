package com.launchkey.sdk.transport.v3.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
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
public class WhiteLabelDeviceAddResponseTest {
    private static final String URL = "URL";
    private static final String CODE = "Code";
    private WhiteLabelDeviceAddResponse response;

    @Before
    public void setUp() throws Exception {
        response = new WhiteLabelDeviceAddResponse(CODE, URL);
    }

    @After
    public void tearDown() throws Exception {
        response = null;
    }

    @Test
    public void getCode() throws Exception {
        assertEquals(CODE, response.getCode());
    }

    @Test
    public void getQrCodeUrl() throws Exception {
        assertEquals(URL, response.getQrCodeUrl());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(response.toString(), containsString(response.getClass().getSimpleName()));
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        assertTrue(response.equals(response));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        assertTrue(response.equals(new WhiteLabelDeviceAddResponse(CODE, URL)));
    }

    @Test
    public void equalsIsFalseForDifferentCode() throws Exception {
        assertFalse(response.equals(new WhiteLabelDeviceAddResponse(null, URL)));
    }

    @Test
    public void equalsIsFalseForDifferentUrl() throws Exception {
        assertFalse(response.equals(new WhiteLabelDeviceAddResponse(CODE, null)));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(response.hashCode(), response.hashCode());
    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        assertEquals(response.hashCode(), new WhiteLabelDeviceAddResponse(CODE, URL).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCode() throws Exception {
        assertNotEquals(response.hashCode(), new WhiteLabelDeviceAddResponse(null, URL).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentUrl() throws Exception {
        assertNotEquals(response.hashCode(), new WhiteLabelDeviceAddResponse(CODE, null).hashCode());
    }

}
