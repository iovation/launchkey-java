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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OrganizationV3DirectoriesListPostResponseTest {
    @Test
    public void getDirectories() throws Exception {
        List<OrganizationV3DirectoriesListPostResponseDirectory> expected = Arrays.asList(mock(OrganizationV3DirectoriesListPostResponseDirectory.class), mock(OrganizationV3DirectoriesListPostResponseDirectory.class));
        assertEquals(expected, new OrganizationV3DirectoriesListPostResponse(expected).getDirectories());
    }
}