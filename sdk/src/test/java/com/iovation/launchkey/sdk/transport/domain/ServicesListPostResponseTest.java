package com.iovation.launchkey.sdk.transport.domain;
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ServicesListPostResponseTest {

    private static final ServicesListPostResponseService a =
            new ServicesListPostResponseService(UUID.randomUUID(), null, null, null, null, false);
    private static final List<ServicesListPostResponseService> as = Arrays.asList(a, a);
    private static final ServicesListPostResponseService b =
            new ServicesListPostResponseService(UUID.randomUUID(), null, null, null, null, false);
    private static final List<ServicesListPostResponseService> bs = Arrays.asList(b, b);

    @Test
    public void getServices() throws Exception {
        List<ServicesListPostResponseService> services = Arrays.asList(a, b);
        assertEquals(services, new ServicesListPostResponse(services).getServices());
    }
}
