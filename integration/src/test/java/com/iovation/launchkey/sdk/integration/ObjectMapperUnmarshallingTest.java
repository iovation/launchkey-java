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

package com.iovation.launchkey.sdk.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.iovation.launchkey.sdk.transport.domain.PublicV3PingGetResponse;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ObjectMapperUnmarshallingTest {
    @Test
    public void datesUnmarshal() throws Exception {
        Date expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2017-11-03T20:50:43Z");
        Date actual = new ObjectMapper().setDateFormat(new StdDateFormat()).readValue(
                "{\"api_time\": \"2017-11-03T20:50:43Z\"}",
                PublicV3PingGetResponse.class
        ).getApiTime();
        assertEquals(expected, actual);
    }
}