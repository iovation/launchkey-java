package com.iovation.launchkey.sdk.client; /**
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

import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoriesPatchRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientUpdateDirectoryTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoriesPatchRequest> requestCaptor;

    public BasicOrganizationClient client;

    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
    }


    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.updateDirectory(null, false, null, null, null);
        verify(transport).organizationV3DirectoriesPatch(any(OrganizationV3DirectoriesPatchRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.updateDirectory(null, false, null, null, null);
        verify(transport).organizationV3DirectoriesPatch(any(OrganizationV3DirectoriesPatchRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        UUID id = UUID.randomUUID();
        client.updateDirectory(id, false, null, null, null);
        verify(transport).organizationV3DirectoriesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getDirectoryId());
    }

    @Test
    public void sendsActive() throws Exception {
        client.updateDirectory(null, true, null, null, null);
        verify(transport).organizationV3DirectoriesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }

    @Test
    public void sendsAndroidKey() throws Exception {
        client.updateDirectory(null, false, "AK", null, null);
        verify(transport).organizationV3DirectoriesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("AK", requestCaptor.getValue().getAndroidKey());
    }

    @Test
    public void sendsIosP12() throws Exception {
        client.updateDirectory(null, false, null, "p12", null);
        verify(transport).organizationV3DirectoriesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("p12", requestCaptor.getValue().getIosP12());
    }

    @Test
    public void sendsIsDenialContextInquiryEnabled() throws Exception {
        client.updateDirectory(null, false, null, null, true);
        verify(transport).organizationV3DirectoriesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isDenialContextInquiryEnabled());
    }

    @Test
    public void defaultsIsDenialContextInquiryEnabledOnDeprecatedMethod() throws Exception {
        client.updateDirectory(null, false, null, null);
        verify(transport).organizationV3DirectoriesPatch(requestCaptor.capture(), any(EntityIdentifier.class));
        assertNull(requestCaptor.getValue().isDenialContextInquiryEnabled());
    }
}