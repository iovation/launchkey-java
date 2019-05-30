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

import org.junit.Test;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class SessionTest {
    @Test
    public void getServiceId() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new Session(id, null, null, null, null).getServiceId());
    }

    @Test
    public void getServiceName() throws Exception {
        assertEquals("name", new Session(null, "name", null, null, null).getServiceName());
    }

    @Test
    public void getServiceIcon() throws Exception {
        assertEquals(URI.create("http://foo.bar"),
                new Session(null, null, URI.create("http://foo.bar"), null, null).getServiceIcon());
    }

    @Test
    public void getAuthRequest() throws Exception {
        UUID id = UUID.randomUUID();
        assertEquals(id, new Session(null, null, null, id, null).getAuthRequest());
    }

    @Test
    public void getCreated() throws Exception {
        Date date = new Date(1000L);
        assertEquals(date, new Session(null, null, null, null, date).getCreated());
    }

    @Test
    public void hashCodeEqualForSameValues() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertEquals(new Session(id, "Name", icon, auth, created).hashCode(),
                new Session(id, "Name", icon, auth, created).hashCode());
    }

    @Test
    public void hashCodeNotEqualForDifferentServiceId() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertNotEquals(new Session(id, "Name", icon, auth, created).hashCode(),
                new Session(UUID.randomUUID(), "Name", icon, auth, created).hashCode());
    }

    @Test
    public void hashCodeNotEqualForDifferentServiceName() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertNotEquals(new Session(id, "Name", icon, auth, created).hashCode(),
                new Session(id, "Other", icon, auth, created).hashCode());
    }

    @Test
    public void hashCodeNotEqualForDifferentServiceIcon() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertNotEquals(new Session(id, "Name", icon, auth, created).hashCode(),
                new Session(id, "Name", URI.create("http://bar"), auth, created).hashCode());
    }

    @Test
    public void hashCodeNotEqualForDifferentAuthRequest() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertNotEquals(new Session(id, "Name", icon, auth, created).hashCode(),
                new Session(id, "Name", icon, UUID.randomUUID(), created).hashCode());
    }

    @Test
    public void hashCodeNotEqualForDifferentCreated() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertNotEquals(new Session(id, "Name", icon, auth, created).hashCode(),
                new Session(id, "Name", icon, auth, new Date(654321L)).hashCode());
    }

    @Test
    public void equalsTrueForSameValues() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertTrue(new Session(id, "Name", icon, auth, created).equals(new Session(id, "Name", icon, auth, created)));
    }

    @Test
    public void equalsFalseForDifferentServiceId() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertFalse(new Session(id, "Name", icon, auth, created)
                .equals(new Session(UUID.randomUUID(), "Name", icon, auth, created)));
    }

    @Test
    public void equalsFalseForDifferentServiceName() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertFalse(new Session(id, "Name", icon, auth, created).equals(new Session(id, "Other", icon, auth, created)));
    }

    @Test
    public void equalsNotTrueForDifferentServiceIcon() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertFalse(new Session(id, "Name", icon, auth, created)
                .equals(new Session(id, "Name", URI.create("http://bar"), auth, created)));
    }

    @Test
    public void equalsNotTrueForDifferentAuthRequest() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertFalse(new Session(id, "Name", icon, auth, created)
                .equals(new Session(id, "Name", icon, UUID.randomUUID(), created)));
    }

    @Test
    public void equalsNotTrueForDifferentCreated() throws Exception {
        UUID id = UUID.randomUUID();
        UUID auth = UUID.randomUUID();
        URI icon = URI.create("http://foo");
        Date created = new Date(123456L);
        assertFalse(new Session(id, "Name", icon, auth, created)
                .equals(new Session(id, "Name", icon, auth, new Date(654321L))));
    }

    @Test
    public void toStringHasClassName() throws Exception {
        assertThat(new Session(null, null, null, null, null).toString(), containsString(Session.class.getSimpleName()));
    }
}