package com.launchkey.sdk.service.error;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
public class InvalidCallbackExceptionTest {
    private ApiException string;
    private Throwable throwable;
    private InvalidCallbackException stringThrowable;

    @Before
    public void setUp() throws Exception {
        string = new InvalidCallbackException("message");
        stringThrowable = new InvalidCallbackException("message", throwable);
    }

    @After
    public void tearDown() throws Exception {
        string = null;
    }

    @Test
    public void testStringConstructorSetsMessage() throws Exception {
        assertEquals("message", string.getMessage());
    }

    @Test
    public void testStringConstructorSetsCode() throws Exception {
        assertEquals(0, string.getCode());
    }

    @Test
    public void testStringThrowableConstructorSetsMessage() throws Exception {
        assertEquals("message", stringThrowable.getMessage());
    }

    @Test
    public void testStringThrowableConstructorSetsCode() throws Exception {
        assertEquals(0, stringThrowable.getCode());
    }

    @Test
    public void testStringThrowableConstructorSetsThrowable() throws Exception {
        assertEquals(throwable, stringThrowable.getCause());
    }
}
