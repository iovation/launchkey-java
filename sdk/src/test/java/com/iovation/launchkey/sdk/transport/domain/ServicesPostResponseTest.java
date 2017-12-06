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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ServicesPostResponseTest {
    @Test
    public void getId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new ServicesPostResponse(id).getId());
    }

    @Test
    public void fromJSON() throws Exception {
        UUID id = UUID.randomUUID();
        ServicesPostResponse read = new ObjectMapper()
                .readValue("{\"id\": \"" + id.toString() + "\"}", ServicesPostResponse.class);
        assertEquals(id, read.getId());
    }
}