package com.iovation.launchkey.sdk.transport.domain;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OrganizationV3DirectoriesListPostResponseDirectoryTest {
    private OrganizationV3DirectoriesListPostResponseDirectory directory;

    @Before
    public void setUp() throws Exception {
        directory = new OrganizationV3DirectoriesListPostResponseDirectory(
                UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), "Expected Name", true,
                Collections.singletonList(UUID.fromString("4137af5c-b460-11e7-9bcd-0469f8dc10a5")),
                Collections.singletonList(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5")),
                "Expected Android Key", "Expected iOS Certificate Fingerprint", true, URI.create("https://a.b"));
    }

    @Test
    public void getName() throws Exception {
        assertEquals("Expected Name", directory.getName());
    }

    @Test
    public void getId() throws Exception {
        assertEquals(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), directory.getId());
    }

    @Test
    public void getAndroidKey() throws Exception {
        assertEquals("Expected Android Key", directory.getAndroidKey());
    }

    @Test
    public void getIosCertificateFingerprint() throws Exception {
        assertEquals("Expected iOS Certificate Fingerprint", directory.getIosCertificateFingerprint());
    }

    @Test
    public void isActive() throws Exception {
        assertTrue(directory.isActive());
    }

    @Test
    public void isDenialContextInquiryEnabled() throws Exception {
        assertTrue(directory.isDenialContextInquiryEnabled());
    }

    @Test
    public void testGetWebhookUrl() {
        assertEquals(URI.create("https://a.b"), directory.getWebhookUrl());
    }

    @Test
    public void fromJSONParsesCorrectId() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals(UUID.fromString("fac25a3c-af79-49df-bd65-777e9c86e288"), actual.getId());
    }

    @Test
    public void fromJSONParsesCorrectName() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals("Expected Name", actual.getName());
    }

    @Test
    public void fromJSONParsesCorrectServiceIds() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals(Collections.singletonList(UUID.fromString("4137af5c-b460-11e7-9bcd-0469f8dc10a5")),
                actual.getServiceIds());
    }

    @Test
    public void fromJSONParsesCorrectSdkKeys() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals(Collections.singletonList(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5")),
                actual.getSdkKeys());
    }

    @Test
    public void fromJSONParsesCorrectAndroidKey() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals("Expected Android Key", actual.getAndroidKey());
    }

    @Test
    public void fromJSONParsesCorrectIosCertificateFingerprint() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals("Expected iOS Certificate Fingerprint", actual.getIosCertificateFingerprint());
    }

    @Test
    public void fromJSONParsesCorrectActive() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertTrue(actual.isActive());
    }

    @Test
    public void fromJSONParsesCorrectDenialContextInquiryEnabled() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true," +
                        "\"denial_context_inquiry_enabled\":true}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertTrue(actual.isDenialContextInquiryEnabled());
    }

    @Test
    public void fromJSONParsesCorrectWebhookUrl() throws Exception {
        OrganizationV3DirectoriesListPostResponseDirectory actual = new ObjectMapper().readValue(
                "{\"id\":\"fac25a3c-af79-49df-bd65-777e9c86e288\",\"name\":\"Expected Name\"," +
                        "\"service_ids\":[\"4137af5c-b460-11e7-9bcd-0469f8dc10a5\"]," +
                        "\"sdk_keys\":[\"6a033e54-b460-11e7-a723-0469f8dc10a5\"]," +
                        "\"android_key\":\"Expected Android Key\"," +
                        "\"ios_certificate_fingerprint\":\"Expected iOS Certificate Fingerprint\",\"active\":true," +
                        "\"denial_context_inquiry_enabled\":true," +
                        "\"webhook_url\":\"https://a.b\"}",
                OrganizationV3DirectoriesListPostResponseDirectory.class);
        assertEquals(URI.create("https://a.b"), actual.getWebhookUrl());
    }
}