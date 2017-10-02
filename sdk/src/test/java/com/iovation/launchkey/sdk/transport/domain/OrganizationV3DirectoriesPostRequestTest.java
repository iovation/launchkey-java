package com.iovation.launchkey.sdk.transport.domain; /**
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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrganizationV3DirectoriesPostRequestTest {
    private static final String name = "Expected Name";
    private OrganizationV3DirectoriesPostRequest request;

    @Before
    public void setUp() throws Exception {
        request = new OrganizationV3DirectoriesPostRequest(name);
    }

    @Test
    public void getName() throws Exception {
        assertEquals(name, request.getName());
    }

}