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

import static org.junit.Assert.assertEquals;

public class KeysPostResponseTest {
    @Test
    public void getId() throws Exception {
        assertEquals("id", new KeysPostResponse("id").getId());
    }

    @Test
    public void fromJSON() throws Exception {
        assertEquals("e6:60:3f:95:ea:c8:4d:2b:98:18:c0:0c:28:e8:9f:bb",
                new ObjectMapper().readValue("{\"key_id\": \"e6:60:3f:95:ea:c8:4d:2b:98:18:c0:0c:28:e8:9f:bb\"}",
                        KeysPostResponse.class).getId());
    }
}