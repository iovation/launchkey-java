package com.launchkey.sdk.service.error;

import com.launchkey.sdk.service.error.CommunicationErrorException;
import com.launchkey.sdk.service.error.LaunchKeyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
public class CommunicationErrorExceptionTest {
    private LaunchKeyException x;
    private Exception cause;
    private LaunchKeyException xx;

    @Before
    public void setUp() throws Exception {
        x = new CommunicationErrorException("message", 999);
        cause = new Exception("Cause");
        xx = new CommunicationErrorException("message", cause, 999);
    }

    @After
    public void tearDown() throws Exception {
        x = null;
        cause = null;
        xx = null;
    }

    @Test
    public void testMessageCodeConstructorSetsMessage() throws Exception {
        assertEquals("message", x.getMessage());
    }

    @Test
    public void testMessageCodeConstructorSetsCode() throws Exception {
        assertEquals(999, x.getCode());
    }

    @Test
    public void testMessageCodeCauseConstructorSetsMessage() throws Exception {
        assertEquals("message", xx.getMessage());
    }

    @Test
    public void testMessageCauseCodeConstructorSetsCode() throws Exception {
        assertEquals(999, xx.getCode());
    }

    @Test
    public void testMessageCauseCodeConstructorSetsCause() throws Exception {
        assertEquals(cause, xx.getCause());
    }
}