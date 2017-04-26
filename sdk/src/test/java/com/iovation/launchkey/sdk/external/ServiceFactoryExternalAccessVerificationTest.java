/**
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

package com.iovation.launchkey.sdk.external;

import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.transport.Transport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;

public class ServiceFactoryExternalAccessVerificationTest {

    ServiceFactory factory = null;

    @Before
    public void setUp() throws Exception {
        factory = new ServiceFactory(mock(Transport.class), UUID.randomUUID());
    }

    @After
    public void tearDown() throws Exception {
        factory = null;
    }

    @Test
    public void testMakeServiceClient() throws Exception {
        factory.makeServiceClient();
    }
}
