package com.iovation.launchkey.sdk.domain.organization; /**
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

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class DirectoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void idCannotBeNull() throws Exception {
        new Directory(null, null, false, null, null, null, null, null, null);
    }

    @Test
    public void getId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new Directory(id, null, false, null, null, null, null, null, null).getId());
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Name", new Directory(UUID.randomUUID(), "Name", false, null, null, null, null, null, null).getName());
    }

    @Test
    public void isActive() throws Exception {
        UUID id = UUID.randomUUID();
        assertTrue(new Directory(UUID.randomUUID(), null, true, null, null, null, null, null, null).isActive());
    }

    @Test
    public void getServiceIds() throws Exception {
        List<UUID> expected = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(expected,
                new Directory(UUID.randomUUID(), null, false, expected, null, null, null, null, null).getServiceIds());
    }

    @Test
    public void getSdkKeys() throws Exception {
        List<UUID> expected = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertEquals(expected,
                new Directory(UUID.randomUUID(), null, false, null, expected, null, null, null, null).getSdkKeys());
    }

    @Test
    public void getGetAndroidKey() throws Exception {
        assertEquals("Android Key",
                new Directory(UUID.randomUUID(), null, false, null, null, "Android Key", null, null, null).getAndroidKey());
    }

    @Test
    public void getIosCertificateFingerprint() throws Exception {
        assertEquals("Fingerprint",
                new Directory(UUID.randomUUID(), null, false, null, null, null, "Fingerprint", null, null)
                        .getIosCertificateFingerprint());
    }

    @Test
    public void isDenialContextInquiryEnabled() throws Exception {
        assertTrue(new Directory(UUID.randomUUID(), null, false, null, null, null, null, true, null)
                .isDenialContextInquiryEnabled());
    }

    @Test
    public void getWebhookUrl() throws Exception {
        assertEquals(URI.create("https://a.b"),
                new Directory(UUID.randomUUID(), null, false, null, null, null, null, null, URI.create("https://a.b"))
                        .getWebhookUrl());
    }

    @Test
    public void hashcodeIsEqualForSameId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(new Directory(id, null, false, null, null, null, null, null, null).hashCode(),
                new Directory(id, null, false, null, null, null, null, null, null).hashCode());
    }

    @Test
    public void hashcodeIsNotSameAsId() throws Exception {
        UUID id = UUID.randomUUID();
        assertNotEquals(id.hashCode(), new Directory(id, null, false, null, null, null, null, null, null).hashCode());
    }

    @Test
    public void equalsIsTrueForSameValues() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertTrue(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)
                .equals(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentId() throws Exception {
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(UUID.randomUUID(), "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)
                .equals(new Directory(UUID.randomUUID(), "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentName() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)
                .equals(new Directory(id, "Other", true, serviceIds, sdkKeys, "Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentActive() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)
                .equals(new Directory(id, "Name", false, serviceIds, sdkKeys, "Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentServiceIds() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()), sdkKeys, "Key",
                "FP", null, null)
                .equals(new Directory(id, "Name", true, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()), sdkKeys,
                        "Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentSdkKeys() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(
                new Directory(id, "Name", true, serviceIds, Arrays.asList(UUID.randomUUID(), UUID.randomUUID()), "Key",
                        "FP", null, null)
                        .equals(new Directory(id, "Name", true, serviceIds,
                                Arrays.asList(UUID.randomUUID(), UUID.randomUUID()),
                                "Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentAndroidKey() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)
                .equals(new Directory(id, "Name", true, serviceIds, sdkKeys, "Other Key", "FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentIosCertFingerprint() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", null, null)
                .equals(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "Other FP", null, null)));
    }

    @Test
    public void equalsIsFalseForDifferentDenialContextInquiryEnabled() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", true, null)
                .equals(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", false, null)));
    }

    @Test
    public void equalsIsFalseForDifferentWebhookUrl() throws Exception {
        UUID id = UUID.randomUUID();
        List<UUID> serviceIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        List<UUID> sdkKeys = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        assertFalse(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", true, URI.create("https://a.b"))
                .equals(new Directory(id, "Name", true, serviceIds, sdkKeys, "Key", "FP", false, URI.create("https://a.b"))));
    }

    @Test
    public void toStringContainsClassName() throws Exception {
        assertThat(new Directory(UUID.randomUUID(), null, true, null, null, null, null, null, null).toString(),
                containsString("Directory"));
    }
}