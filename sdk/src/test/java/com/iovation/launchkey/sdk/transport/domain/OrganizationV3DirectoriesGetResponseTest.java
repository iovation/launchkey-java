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

import java.net.URI;
import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class OrganizationV3DirectoriesGetResponseTest {
    static final List<OrganizationV3DirectoriesGetResponseDirectory> directories = Collections.singletonList(
            new OrganizationV3DirectoriesGetResponseDirectory(UUID.randomUUID(), "Name", true,
                    new ArrayList<UUID>(), new ArrayList<UUID>(), "Android Key", "IoS Fingerprint", true,
                    URI.create("https://a.b"))
    );
    private OrganizationV3DirectoriesGetResponse response;

    @Before
    public void setUp() throws Exception {
        response = new OrganizationV3DirectoriesGetResponse(directories);
    }

    @Test
    public void getDirectories() throws Exception {
        assertEquals(directories, response.getDirectories());
    }

}