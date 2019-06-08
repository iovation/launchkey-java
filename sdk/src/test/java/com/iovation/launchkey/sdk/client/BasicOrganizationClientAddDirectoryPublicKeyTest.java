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

import com.iovation.launchkey.sdk.crypto.JCECrypto;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import com.iovation.launchkey.sdk.transport.domain.KeysPostResponse;
import com.iovation.launchkey.sdk.transport.domain.OrganizationV3DirectoryKeysPostRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicOrganizationClientAddDirectoryPublicKeyTest {
    private final static UUID orgId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private KeysPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<OrganizationV3DirectoryKeysPostRequest> requestCaptor;

    @SuppressWarnings("SpellCheckingInspection")
    private static final String pem = "-----BEGIN PUBLIC KEY-----\n" +
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANd/rO3/EY0rGeT4Sib6qD6AS26SHDUt\n" +
            "sN9nM11H1ajurrZz4ZKCKPG1jdmqvo/tGXvt5mQyvR9WJCg6+uokSfMCAwEAAQ==\n" +
            "-----END PUBLIC KEY-----\n";

    private static final RSAPublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(new BouncyCastleProvider(), pem);


    public BasicOrganizationClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicOrganizationClient(orgId, transport);
        when(transport.organizationV3DirectoryKeysPost(any(OrganizationV3DirectoryKeysPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.addDirectoryPublicKey(null, publicKey, false, null);
        verify(transport).organizationV3DirectoryKeysPost(any(OrganizationV3DirectoryKeysPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.ORGANIZATION, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.addDirectoryPublicKey(null, publicKey, false, null);
        verify(transport).organizationV3DirectoryKeysPost(any(OrganizationV3DirectoryKeysPostRequest.class), entityCaptor.capture());
        assertEquals(orgId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendDirectoryIdInRequest() throws Exception {
        UUID id = UUID.randomUUID();
        client.addDirectoryPublicKey(id, publicKey, false, null);
        verify(transport).organizationV3DirectoryKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getDirectoryId());
    }

    @Test
    public void sendsPublicKeyPEMInRequest() throws Exception {
        client.addDirectoryPublicKey(null, publicKey, false, null);
        verify(transport).organizationV3DirectoryKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(pem, requestCaptor.getValue().getPublicKey());
    }

    @Test
    public void sendsExpirationDateInRequest() throws Exception {
        Date date = new Date(0L);
        client.addDirectoryPublicKey(null, publicKey, false, date);
        verify(transport).organizationV3DirectoryKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(date, requestCaptor.getValue().getExpires());
    }

    @Test
    public void sendsActiveFlagInRequest() throws Exception {
        client.addDirectoryPublicKey(null, publicKey, true, null);
        verify(transport).organizationV3DirectoryKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }

    @Test
    public void returnsKeyIdAsResponse() throws Exception {
        when(response.getId()).thenReturn("Key ID");
        assertEquals("Key ID", client.addDirectoryPublicKey(null, publicKey, true, null));
    }
}