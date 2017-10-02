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

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DirectoryV3SessionsListPostResponseSessionTest {
    @Test
    public void getServiceId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new DirectoryV3SessionsListPostResponseSession(id, null, null, null, null).getServiceId());
    }

    @Test
    public void getServiceName() throws Exception {
        assertEquals("name",
                new DirectoryV3SessionsListPostResponseSession(null, "name", null, null, null).getServiceName());
    }

    @Test
    public void getServiceIcon() throws Exception {
        URI icon = URI.create("http://foo");
        assertEquals(icon,
                new DirectoryV3SessionsListPostResponseSession(null, null, icon, null, null).getServiceIcon());
    }

    @Test
    public void getAuthRequest() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new DirectoryV3SessionsListPostResponseSession(null, null, null, id, null).getAuthRequest());
    }

    @Test
    public void getCreated() throws Exception {
        Date date = new Date(12345L);
        assertEquals(date, new DirectoryV3SessionsListPostResponseSession(null, null, null, null, date).getCreated());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void fromJSONSetsServiceId() throws Exception {
        DirectoryV3SessionsListPostResponseSession actual = new ObjectMapper().readValue(
                "{\"service_id\":\"319d2db1-3965-4f2e-89a0-26572ddbf31d\",\"service_name\":\"Service Name\"," +
                        "\"service_icon\":\"http://foo\",\"date_created\":\"1970-01-01T00:00:00Z\"}",
                DirectoryV3SessionsListPostResponseSession.class);
        assertEquals(UUID.fromString("319d2db1-3965-4f2e-89a0-26572ddbf31d"), actual.getServiceId());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void fromJSONSetsServiceName() throws Exception {
        DirectoryV3SessionsListPostResponseSession actual = new ObjectMapper().readValue(
                "{\"service_id\":\"319d2db1-3965-4f2e-89a0-26572ddbf31d\",\"service_name\":\"Service Name\"," +
                        "\"service_icon\":\"http://foo\",\"date_created\":\"1970-01-01T00:00:00Z\"}",
                DirectoryV3SessionsListPostResponseSession.class);
        assertEquals("Service Name", actual.getServiceName());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void fromJSONSetsServiceIcon() throws Exception {
        DirectoryV3SessionsListPostResponseSession actual = new ObjectMapper().readValue(
                "{\"service_id\":\"319d2db1-3965-4f2e-89a0-26572ddbf31d\",\"service_name\":\"Service Name\"," +
                        "\"service_icon\":\"http://foo\",\"date_created\":\"1970-01-01T00:00:00Z\"}",
                DirectoryV3SessionsListPostResponseSession.class);
        assertEquals(URI.create("http://foo"), actual.getServiceIcon());
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    public void fromJSONSetsCreated() throws Exception {
        DirectoryV3SessionsListPostResponseSession actual = new ObjectMapper().readValue(
                "{\"service_id\":\"319d2db1-3965-4f2e-89a0-26572ddbf31d\",\"service_name\":\"Service Name\"," +
                        "\"service_icon\":\"http://foo\",\"date_created\":\"1970-01-01T00:00:00Z\"}",
                DirectoryV3SessionsListPostResponseSession.class);
        assertEquals(new Date(0), actual.getCreated());
    }
}