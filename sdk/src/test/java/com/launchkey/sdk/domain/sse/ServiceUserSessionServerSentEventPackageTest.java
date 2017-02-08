package com.launchkey.sdk.domain.sse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

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
public class ServiceUserSessionServerSentEventPackageTest {

    private ServiceUserSessionServerSentEventPackage serviceUserSessionServerSentEventPackage;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        serviceUserSessionServerSentEventPackage = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
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
        ServiceUserSessionServerSentEventPackage actual = mapper.readValue(json, ServiceUserSessionServerSentEventPackage.class);
        assertEquals(serviceUserSessionServerSentEventPackage, actual);
    }

    @Test
    public void testJSONParseAllowsUnknown() throws Exception {
        String json = "{\"api_time\":\"1970-01-01T00:00:00Z\",\"service_user_hash\":\"User Hash\"," +
                "\"unknown\": \"Unknown Value\"}";
        ServiceUserSessionServerSentEventPackage actual = mapper.readValue(json, ServiceUserSessionServerSentEventPackage.class);
        assertEquals(serviceUserSessionServerSentEventPackage, actual);
    }

    @Test
    public void testEqualsForEqualObjectsIsTrue() throws Exception {
        ServiceUserSessionServerSentEventPackage left = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
        ServiceUserSessionServerSentEventPackage right = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
        assertTrue(left.equals(right));
    }

    @Test
    public void testEqualsForUnEqualObjectsIsFalse() throws Exception {
        ServiceUserSessionServerSentEventPackage left = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
        ServiceUserSessionServerSentEventPackage right = new ServiceUserSessionServerSentEventPackage(new Date(1L), "User Hash");
        assertFalse(left.equals(right));
    }

    @Test
    public void testHashCodeForEqualObjectsAreEqual() throws Exception {
        ServiceUserSessionServerSentEventPackage left = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
        ServiceUserSessionServerSentEventPackage right = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
        assertEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testHasCodeForUnEqualObjectsIsNotEqual() throws Exception {
        ServiceUserSessionServerSentEventPackage left = new ServiceUserSessionServerSentEventPackage(new Date(0L), "User Hash");
        ServiceUserSessionServerSentEventPackage right = new ServiceUserSessionServerSentEventPackage(new Date(1L), "User Hash");
        assertNotEquals(left.hashCode(), right.hashCode());
    }

    @Test
    public void testToStringContainsClassName() throws Exception {
        assertThat(serviceUserSessionServerSentEventPackage.toString(), containsString(ServiceUserSessionServerSentEventPackage.class.getSimpleName()));
    }
}
