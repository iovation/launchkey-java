package com.launchkey.sdk.crypto.jwe;

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
public class JWEFailureTest {
    private String message;
    private Throwable cause;
    private JWEFailure exception;

    @Before
    public void setUp() throws Exception {
        message = "Expected Message";
        cause = new Throwable();
        exception = new JWEFailure(message, cause);
    }

    @After
    public void tearDown() throws Exception {
        message = null;
        cause = null;
        exception = null;
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void getCause() throws Exception {
        assertEquals(cause, exception.getCause());
    }
}
