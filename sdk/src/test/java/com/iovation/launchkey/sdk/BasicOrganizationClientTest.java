package com.iovation.launchkey.sdk;

import com.iovation.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class BasicOrganizationClientTest {

    private OrganizationClient organizationClient;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        UUID uuid = UUID.randomUUID();
        Transport transport = mock(Transport.class);
        organizationClient = new BasicOrganizationClient(transport, uuid);
    }

    @After
    public void tearDown() throws Exception {
        organizationClient = null;
    }

    @Test
    public void testGetDirectoryServiceWithNullThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationClient.getDirectoryService(null);
    }

    @Test
    public void testGetDirectoryServiceWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationClient.getDirectoryService("Not A UUID");
    }

    @Test
    public void testGetDirectoryServiceWithValidUUIDReturnsService() throws Exception {
        assertNotNull(organizationClient.getDirectoryService(UUID.randomUUID().toString()));
    }

    @Test
    public void testGetServiceServiceWithNullThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationClient.getServiceService(null);
    }

    @Test
    public void testGetServiceServiceWithInvalidUUIDThrowsIllegalArgument() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        organizationClient.getServiceService("Not A UUID");
    }

    @Test
    public void testGetServiceServiceWithValidUUIDReturnsService() throws Exception {
        assertNotNull(organizationClient.getServiceService(UUID.randomUUID().toString()));
    }


}
