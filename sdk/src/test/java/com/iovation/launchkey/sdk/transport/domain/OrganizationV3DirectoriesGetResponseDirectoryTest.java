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

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrganizationV3DirectoriesGetResponseDirectoryTest {
    @Test
    public void getId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new OrganizationV3DirectoriesGetResponseDirectory(
                id, null, false, null, null, null, null, null).getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("name", new OrganizationV3DirectoriesGetResponseDirectory(
                null, "name", false, null, null, null, null, null).getName());
    }

    @Test
    public void isActive() throws Exception {
        assertTrue(new OrganizationV3DirectoriesGetResponseDirectory(
                null, null, true, null, null, null, null, null).isActive());
    }

    @Test
    public void getServiceIds() throws Exception {
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(serviceIds, new OrganizationV3DirectoriesGetResponseDirectory(
                null, null, false, serviceIds, null, null, null, null).getServiceIds());
    }

    @Test
    public void getSdkKeys() throws Exception {
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(sdkKeys, new OrganizationV3DirectoriesGetResponseDirectory(
                null, null, false, null, sdkKeys, null, null, null).getSdkKeys());
    }

    @Test
    public void getAndroidKey() throws Exception {
        assertEquals("key", new OrganizationV3DirectoriesGetResponseDirectory(
                null, null, false, null, null, "key", null, null).getAndroidKey());
    }

    @Test
    public void getIosCertificateFingerprint() throws Exception {
        assertEquals("fp", new OrganizationV3DirectoriesGetResponseDirectory(
                null, null, false, null, null, null, "fp", null).getIosCertificateFingerprint());
    }

    @Test
    public void isDenialContextInquiryEnabled() throws Exception {
        assertTrue(new OrganizationV3DirectoriesGetResponseDirectory(
                null, null, false, null, null, null, null, true).isDenialContextInquiryEnabled());
    }
}