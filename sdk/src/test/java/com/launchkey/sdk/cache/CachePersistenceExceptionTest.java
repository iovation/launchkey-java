package com.launchkey.sdk.cache;

import org.junit.Test;

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
public class CachePersistenceExceptionTest {
    @Test
    public void testMessageConstructor() throws Exception {
        Exception e = new CachePersistenceException("Message");
        assertEquals("Message", e.getMessage());

    }

    @Test
    public void testMessageCauseConstructor() throws Exception {
        Exception c = new Exception();
        Exception e = new CachePersistenceException("Message", c);
        assertEquals("Message", e.getMessage());
        assertSame(c, e.getCause());
    }
}
