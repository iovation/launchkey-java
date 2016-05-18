package com.launchkey.sdk.service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

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
public class DeOrbitCallbackResponseTest {

    private DeOrbitCallbackResponse deOrbitCallbackResponse;

    @Before
    public void setUp() throws Exception {
        deOrbitCallbackResponse = new DeOrbitCallbackResponse(new LogoutCallbackResponse(new Date(0L), "User Hash"));
    }

    @After
    public void tearDown() throws Exception {
        deOrbitCallbackResponse = null;
    }

    @Test
    public void testGetDeOrbitTime() throws Exception {
        assertEquals(new Date(0L), deOrbitCallbackResponse.getDeOrbitTime());
    }

    @Test
    public void testGetUserHash() throws Exception {
        assertEquals("User Hash", deOrbitCallbackResponse.getUserHash());
    }
}