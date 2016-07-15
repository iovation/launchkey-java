package com.launchkey.sdk.transport.v3.domain;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.transport.v1.domain.Policy.Factor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DeviceTest {
    private static final Integer STATUS = 123;
    private static final String NAME = "Name";
    private static final String TYPE = "Type";
    private static final Date CREATED = new Date(System.currentTimeMillis() - 10000);
    private static final Date UPDATED = new Date(System.currentTimeMillis() - 90000);
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final ObjectMapper mapper = new ObjectMapper();
    private Device device;

    @Before
    public void setUp() throws Exception {
        device = new Device(NAME, STATUS, TYPE, CREATED, UPDATED);
    }

    @After
    public void tearDown() throws Exception {
        device = null;
    }

    @Test
    public void getName() throws Exception {
        assertEquals(NAME, device.getName());
    }

    @Test
    public void getStatus() throws Exception {
        assertEquals(STATUS, device.getStatus());
    }

    @Test
    public void getType() throws Exception {
        assertEquals(TYPE, device.getDeviceType());
    }

    @Test
    public void getCreated() throws Exception {
        assertEquals(CREATED, device.getCreated());
    }

    @Test
    public void getUpdated() throws Exception {
        assertEquals(UPDATED, device.getUpdated());
    }

    @Test
    public void equalsIsTrueForSameObject() throws Exception {
        assertTrue(device.equals(device));
    }

    @Test
    public void equalsIsTrueForEquivalentObject() throws Exception {
        assertTrue(device.equals(new Device(NAME, STATUS, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentName() throws Exception {
        assertFalse(device.equals(new Device(null, STATUS, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentStatus() throws Exception {
        assertFalse(device.equals(new Device(NAME, STATUS + 1, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentType() throws Exception {
        assertFalse(device.equals(new Device(NAME, STATUS, null, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentCreated() throws Exception {
        assertFalse(device.equals(new Device(NAME, STATUS, TYPE, null, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentUpdated() throws Exception {
        assertFalse(device.equals(new Device(NAME, STATUS, TYPE, CREATED, null)));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(device.hashCode(), device.hashCode());
    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        assertEquals(device.hashCode(), new Device(NAME, STATUS, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentName() throws Exception {
        assertNotEquals(device.hashCode(), new Device(null, STATUS, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentStatus() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, STATUS + 1, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentType() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, STATUS, null, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCreated() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, STATUS, TYPE, null, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentUpdated() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, STATUS, TYPE, CREATED, null).hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(device.toString(), containsString(device.getClass().getSimpleName()));
    }

    @Test
    public void jsonParsingWorksProperly() throws Exception {

        Device expected = new Device(NAME, STATUS, TYPE, CREATED, UPDATED);
        String json = "{\"status\": " + STATUS + "," +"" +
                "\"updated\": \"" + df.format(UPDATED) + "\"," +
                "\"name\": \"" + NAME + "\"," +
                "\"device_type\": \"" + TYPE + "\"," +
                "\"created\": \"" + df.format(CREATED) + "\"}";
        Device actual = mapper.readValue(json, Device.class);
        assertEquals(expected, actual);
    }

    @Test
    public void jsonParsingWorksProperlyWithExtraAttributes() throws Exception {

        Device expected = new Device(NAME, STATUS, TYPE, CREATED, UPDATED);
        String json = "{\"status\": " + STATUS + "," +"" +
                "\"updated\": \"" + df.format(UPDATED) + "\"," +
                "\"name\": \"" + NAME + "\"," +
                "\"device_type\": \"" + TYPE + "\"," +
                "\"created\": \"" + df.format(CREATED) + "\"," +
                "\"extra\": true}";
        Device actual = mapper.readValue(json, Device.class);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenStatusNotPresent() throws Exception {
        String json = "{" +
                "\"updated\": \"" + df.format(UPDATED) + "\"," +
                "\"name\": \"" + NAME + "\"," +
                "\"device_type\": \"" + TYPE + "\"," +
                "\"created\": \"" + df.format(CREATED) + "\"}";
        mapper.readValue(json, Device.class);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenUpdatedNotPresent() throws Exception {
        String json = "{\"status\": " + STATUS + "," +"" +
                "\"name\": \"" + NAME + "\"," +
                "\"device_type\": \"" + TYPE + "\"," +
                "\"created\": \"" + df.format(CREATED) + "\"}";
        mapper.readValue(json, Device.class);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenNameNotPresent() throws Exception {
        String json = "{\"status\": " + STATUS + "," +"" +
                "\"updated\": \"" + df.format(UPDATED) + "\"," +
                "\"device_type\": \"" + TYPE + "\"," +
                "\"created\": \"" + df.format(CREATED) + "\"}";
        mapper.readValue(json, Device.class);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenDeviceTypeNotPresent() throws Exception {
        String json = "{\"status\": " + STATUS + "," +"" +
                "\"updated\": \"" + df.format(UPDATED) + "\"," +
                "\"name\": \"" + NAME + "\"," +
                "\"created\": \"" + df.format(CREATED) + "\"}";
        mapper.readValue(json, Device.class);
    }

    @Test(expected = JsonMappingException.class)
    public void jsonParseThrowsMappingErrorWhenCreatedNotPresent() throws Exception {
        String json = "{\"status\": " + STATUS + "," +"" +
                "\"updated\": \"" + df.format(UPDATED) + "\"," +
                "\"name\": \"" + NAME + "\"," +
                "\"device_type\": \"" + TYPE + "\"" +
                "}";
        mapper.readValue(json, Device.class);
    }
}
