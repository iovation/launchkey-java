package com.launchkey.sdk.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

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
    private static final String NAME = "name";
    private static final String TYPE = "Type";
    private static final Date CREATED = new Date(System.currentTimeMillis() - 10000);
    private static final Date UPDATED = new Date(System.currentTimeMillis() - 90000);
    private Device device;

    @Before
    public void setUp() throws Exception {
        device = new Device(NAME, DeviceStatus.LINKED, TYPE, CREATED, UPDATED);
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
    public void equalsIsTrueForEqivalentObject() throws Exception {
        assertTrue(device.equals(new Device(NAME, DeviceStatus.LINKED, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentName() throws Exception {
        assertFalse(device.equals(new Device(null, DeviceStatus.LINKED, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentStatus() throws Exception {
        assertFalse(device.equals(new Device(NAME, DeviceStatus.UNLINK_PENDING, TYPE, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentType() throws Exception {
        assertFalse(device.equals(new Device(NAME, DeviceStatus.LINKED, null, CREATED, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentCreated() throws Exception {
        assertFalse(device.equals(new Device(NAME, DeviceStatus.LINKED, TYPE, null, UPDATED)));
    }

    @Test
    public void equalsIsFalseForDifferentUpdated() throws Exception {
        assertFalse(device.equals(new Device(NAME, DeviceStatus.LINKED, TYPE, CREATED, null)));
    }

    @Test
    public void hashCodeIsEqualForSameObject() throws Exception {
        assertEquals(device.hashCode(), device.hashCode());
    }

    @Test
    public void hashCodeIsEqualForEquivalentObject() throws Exception {
        assertEquals(device.hashCode(), new Device(NAME, DeviceStatus.LINKED, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentName() throws Exception {
        assertNotEquals(device.hashCode(), new Device(null, DeviceStatus.LINKED, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentStatus() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, DeviceStatus.UNLINK_PENDING, TYPE, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentType() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, DeviceStatus.LINKED, null, CREATED, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentCreated() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, DeviceStatus.LINKED, TYPE, null, UPDATED).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentUpdated() throws Exception {
        assertNotEquals(device.hashCode(), new Device(NAME, DeviceStatus.LINKED, TYPE, CREATED, null).hashCode());
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertTrue(device.toString().contains(device.getClass().getSimpleName()));
    }

}
