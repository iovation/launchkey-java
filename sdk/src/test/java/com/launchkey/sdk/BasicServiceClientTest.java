package com.launchkey.sdk;

import com.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

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
public class BasicServiceClientTest {

    private Transport transport;
    private ServiceClient serviceClient;

    @Before
    public void setUp() throws Exception {
        transport = mock(Transport.class);
        serviceClient = new BasicServiceClient(transport, UUID.randomUUID());
    }

    @After
    public void tearDown() throws Exception {
        serviceClient = null;
        transport = null;
    }

    @Test
    public void testGetServiceServiceReturnsServiceService() throws Exception {
        assertNotNull(serviceClient.getServiceService());
    }
}
