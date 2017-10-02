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
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServicesGetResponseTest {
    private static final ServicesGetResponseService a =
            new ServicesGetResponseService(UUID.randomUUID(), null, null, null, null, false);
    private static final List<ServicesGetResponseService> as = Arrays.asList(a, a);
    private static final ServicesGetResponseService b =
            new ServicesGetResponseService(UUID.randomUUID(), null, null, null, null, false);
    private static final List<ServicesGetResponseService> bs = Arrays.asList(b, b);

    @Test
    public void getServices() throws Exception {
        List<ServicesGetResponseService> services =
                Arrays.asList(new ServicesGetResponseService(null, null, null, null, null, false),
                        new ServicesGetResponseService(null, null, null, null, null, false));
        assertEquals(services, new ServicesGetResponse(services).getServices());
    }

    @Test
    public void equalsIsTrueForSameValue() throws Exception {
        assertTrue(new ServicesGetResponse(as).equals(new ServicesGetResponse(as)));
    }

    @Test
    public void equalsIsFalseForDifferentValues() throws Exception {
        assertFalse(new ServicesGetResponse(as).equals(new ServicesGetResponse(bs)));
    }

    @Test
    public void toStringContainsClass() throws Exception {
        assertThat(new ServicesGetResponse(as).toString(),
                containsString(ServicesGetResponse.class.getSimpleName()));
    }
}