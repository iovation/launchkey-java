package com.launchkey.sdk.domain.directory;

import org.junit.Test;

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
public class DeviceStatusTest {
    @Test
    public void linkPendingHasStatusCodeOfZero() throws Exception {
        assertEquals(0, DeviceStatus.LINK_PENDING.getStatusCode());
    }

    @Test
    public void linkPendingIsNotActive() throws Exception {
        assertFalse(DeviceStatus.LINK_PENDING.isActive());
    }

    @Test
    public void linedHasStatusCodeOfOne() throws Exception {
        assertEquals(1, DeviceStatus.LINKED.getStatusCode());
    }

    @Test
    public void linkedIsActive() throws Exception {
        assertTrue(DeviceStatus.LINKED.isActive());
    }

    @Test
    public void unlinkPendingHasStatusCodeOfTwo() throws Exception {
        assertEquals(2, DeviceStatus.UNLINK_PENDING.getStatusCode());
    }

    @Test
    public void unlinkPendingIsActive() throws Exception {
        assertTrue(DeviceStatus.UNLINK_PENDING.isActive());
    }

    @Test
    public void fromStatusCodeReturnsProperStatusForValidCode() throws Exception {
        assertEquals(DeviceStatus.LINKED, DeviceStatus.fromCode(DeviceStatus.LINKED.getStatusCode()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromStatusCodeThrowsInvalidArgumentExceptionForInvalidCode() throws Exception {
        DeviceStatus.fromCode(-1);
    }
}
