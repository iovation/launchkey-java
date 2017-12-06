package com.iovation.launchkey.sdk.domain.directory; /**
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

import com.iovation.launchkey.sdk.domain.PublicKey;
import com.iovation.launchkey.sdk.domain.servicemanager.Service;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class ServiceTest {
    private final UUID id;
    private final String name;
    private final String description;
    private final URI icon;
    private final URI callbackURL;
    private final boolean active;
    private final ArrayList<PublicKey> publicKeys;
    private Service service;


    public ServiceTest() throws URISyntaxException {
        id = UUID.randomUUID();
        name = "Expected Name";
        description = "Expected Description";
        icon = new URI("https://api.launchkey.com/icon");
        callbackURL = new URI("https://api.launchkey.com/callback-url");
        active = true;
        publicKeys = new ArrayList<>();
        publicKeys.add(new PublicKey("Fingerprint", true, new Date(), null));

    }

    @Before
    public void setUp() throws Exception {

        service = new Service(id, name, description, icon, callbackURL, active);
    }

    @Test
    public void testGetIdReturnsExpectedValue() throws Exception {
        assertEquals(id, service.getId());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(name, service.getName());
    }

    @Test
    public void testGetDescription() throws Exception {
        assertEquals(description, service.getDescription());
    }

    @Test
    public void testGetIcon() throws Exception {
        assertEquals(icon, service.getIcon());
    }

    @Test
    public void testGetCallbackURL() throws Exception {
        assertEquals(callbackURL, service.getCallbackURL());
    }

    @Test
    public void testIsActive() throws Exception {
        assertTrue(service.isActive());
    }

    @Test
    public void testEqualsIsTrueForSameObject() throws Exception {
        //noinspection EqualsWithItself
        assertTrue(service.equals(service));
    }

    @Test
    public void testEqualsIsTrueForSameID() throws Exception {
        assertTrue(service.equals(
                new Service(UUID.fromString(id.toString()), name, description, icon, callbackURL, active)));
    }

    @Test
    public void testEqualsIsFalseForDifferentID() throws Exception {
        assertFalse(service.equals(
                new Service(UUID.randomUUID(), name, description, icon, callbackURL, active)));
    }

    @Test
    public void testHashCodeIsSameForSameID() throws Exception {
        assertEquals(service.hashCode(),
                new Service(UUID.fromString(id.toString()), null, null, null, null, false).hashCode());
    }

    @Test
    public void testHashCodeIsDifferentForDifferentID() throws Exception {
        assertNotEquals(service.hashCode(),
                new Service(UUID.randomUUID(), name, description, icon, callbackURL, active).hashCode());
    }

    @Test
    public void testHashCodeIsDifferentComparedToSameUUID() throws Exception {
        assertNotEquals(service.hashCode(), id.hashCode());
    }
}