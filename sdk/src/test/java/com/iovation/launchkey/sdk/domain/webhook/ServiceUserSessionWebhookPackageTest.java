package com.iovation.launchkey.sdk.domain.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

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
public class ServiceUserSessionWebhookPackageTest {

    private ServiceUserSessionEndWebhookPackage serviceUserSessionServerSentEventPackage;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        serviceUserSessionServerSentEventPackage = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        mapper = new ObjectMapper();
        mapper.setDateFormat(new ISO8601DateFormat());
    }

    @After
    public void tearDown() throws Exception {
        serviceUserSessionServerSentEventPackage = null;
    }

    @Test
    public void testGetLogoutRequested() throws Exception {
        assertEquals(new Date(0L), serviceUserSessionServerSentEventPackage.getLogoutRequested());
    }

    @Test
    public void testGetUserHash() throws Exception {
        assertEquals("User Hash", serviceUserSessionServerSentEventPackage.getServiceUserHash());
    }

    @Test
    public void testJSONParsable() throws Exception {
        String json = "{\"api_time\":\"1970-01-01T00:00:00Z\",\"service_user_hash\":\"User Hash\"}";
        ServiceUserSessionEndWebhookPackage actual = mapper.readValue(json, ServiceUserSessionEndWebhookPackage.class);
        assertEquals(serviceUserSessionServerSentEventPackage, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"api_time\":\"1970-01-01T00:00:00Z\",\"service_user_hash\":\"User Hash\"," +
                "\"unknown\": \"Unknown Value\"}";
        ServiceUserSessionEndWebhookPackage actual = mapper.readValue(json, ServiceUserSessionEndWebhookPackage.class);
        assertEquals(serviceUserSessionServerSentEventPackage, actual);
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        ServiceUserSessionEndWebhookPackage left = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        ServiceUserSessionEndWebhookPackage right = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        ServiceUserSessionEndWebhookPackage left = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        ServiceUserSessionEndWebhookPackage right = new ServiceUserSessionEndWebhookPackage(new Date(1L), "User Hash");
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        ServiceUserSessionEndWebhookPackage left = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        ServiceUserSessionEndWebhookPackage right = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        ServiceUserSessionEndWebhookPackage left = new ServiceUserSessionEndWebhookPackage(new Date(0L), "User Hash");
        ServiceUserSessionEndWebhookPackage right = new ServiceUserSessionEndWebhookPackage(new Date(1L), "User Hash");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(serviceUserSessionServerSentEventPackage.toString(), containsString(ServiceUserSessionEndWebhookPackage.class.getSimpleName()));
    }
}
