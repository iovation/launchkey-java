package com.iovation.launchkey.sdk.error; /**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Test;

import static org.junit.Assert.*;

public class CryptographyErrorTest {
    @Test
    public void getMessage() throws Exception {
        assertEquals("Hello", new CryptographyError("Hello", new Exception()).getMessage());
    }

    @Test
    public void getCause() throws Exception {
        Throwable cause = new Exception();
        assertEquals(cause, new Exception(null, cause).getCause());
    }

}