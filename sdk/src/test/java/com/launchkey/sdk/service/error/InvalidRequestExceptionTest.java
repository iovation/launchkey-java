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
public class InvalidRequestExceptionTest {
    private ApiException x;

    @Before
    public void setUp() throws Exception {
        x = new InvalidRequestException("message", 999);
    }

    @After
    public void tearDown() throws Exception {
        x = null;
    }

    @Test
    public void testConstructorSetsMessage() throws Exception {
        assertEquals("message", x.getMessage());
    }

    @Test
    public void testConstructorSetsCode() throws Exception {
        assertEquals(999, x.getCode());
    }
}
