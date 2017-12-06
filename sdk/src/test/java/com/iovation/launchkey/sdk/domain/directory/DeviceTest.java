package com.iovation.launchkey.sdk.domain.directory;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Copyright 2017 iovation, Inc. All rights reserved.
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
    private static final String NAME = "name";
    private static final String TYPE = "Type";
    private static final String ID = "ID";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static Date CREATED;
    private static Date UPDATED;
    private Device device;

    @BeforeClass
    public static void setUpClass() throws Exception {
        CREATED = DATE_FORMAT.parse("2017-01-01");
        UPDATED = DATE_FORMAT.parse("2017-02-02");
    }

    @Before
    public void setUp() throws Exception {

        device = new Device(ID, NAME, DeviceStatus.LINKED, TYPE, CREATED, UPDATED);
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
        assertEquals(DeviceStatus.LINKED, device.getStatus());
    }

    @Test
    public void getType() throws Exception {
        assertEquals(TYPE, device.getType());
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
        assertTrue(device.equals(new Device(ID, NAME, DeviceStatus.LINKED, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentName() throws Exception {
        assertFalse(device.equals(new Device(ID, null, DeviceStatus.LINKED, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentStatus() throws Exception {
        assertFalse(device.equals(new Device(ID, NAME, DeviceStatus.UNLINK_PENDING, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentType() throws Exception {
        assertFalse(device.equals(new Device(ID, NAME, DeviceStatus.LINKED, null, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentCreated() throws Exception {
        assertFalse(device.equals(new Device(ID, NAME, DeviceStatus.LINKED, TYPE, null, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentUpdated() throws Exception {
        assertFalse(device.equals(new Device(ID, NAME, DeviceStatus.LINKED, TYPE, CREATED, null)));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(device.hashCode(), device.hashCode());
    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        assertEquals(device.hashCode(), new Device(ID, NAME, DeviceStatus.LINKED, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentName() throws Exception {
        assertNotEquals(device.hashCode(), new Device(ID, null, DeviceStatus.LINKED, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentStatus() throws Exception {
        assertNotEquals(device.hashCode(), new Device(ID, NAME, DeviceStatus.UNLINK_PENDING, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentType() throws Exception {
        assertNotEquals(device.hashCode(), new Device(ID, NAME, DeviceStatus.LINKED, null, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentDateCreated() throws Exception {
        assertNotEquals(device.hashCode(), new Device(ID, NAME, DeviceStatus.LINKED, TYPE, null, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentDateUpdated() throws Exception {
        assertNotEquals(device.hashCode(), new Device(ID, NAME, DeviceStatus.LINKED, TYPE, CREATED, null).hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(device.toString().contains(device.getClass().getSimpleName()));
    }

}
