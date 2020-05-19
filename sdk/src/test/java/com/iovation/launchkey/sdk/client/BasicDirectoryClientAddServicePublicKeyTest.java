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
import com.iovation.launchkey.sdk.transport.domain.ServiceKeysPostRequest;
import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
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
public class BasicDirectoryClientAddServicePublicKeyTest {
    private final static UUID directoryId = UUID.fromString("67c87654-aed9-11e7-98e9-0469f8dc10a5");

    @Mock
    public Transport transport;

    @Mock
    private KeysPostResponse response;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Captor
    private ArgumentCaptor<ServiceKeysPostRequest> requestCaptor;

    @SuppressWarnings("SpellCheckingInspection")
    private static final String pem = "-----BEGIN PUBLIC KEY-----\n" +
            "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANd/rO3/EY0rGeT4Sib6qD6AS26SHDUt\n" +
            "sN9nM11H1ajurrZz4ZKCKPG1jdmqvo/tGXvt5mQyvR9WJCg6+uokSfMCAwEAAQ==\n" +
            "-----END PUBLIC KEY-----\n";

    private static final RSAPublicKey publicKey = JCECrypto.getRSAPublicKeyFromPEM(new BouncyCastleFipsProvider(), pem);


    public DirectoryClient client;


    @Before
    public void setUp() throws Exception {
        this.client = new BasicDirectoryClient(directoryId, transport);
        when(transport.directoryV3ServiceKeysPost(any(ServiceKeysPostRequest.class), any(EntityIdentifier.class)))
                .thenReturn(response);
    }

    @Test
    public void sendsSubjectEntityType() throws Exception {
        client.addServicePublicKey(null, publicKey, false, null);
        verify(transport).directoryV3ServiceKeysPost(any(ServiceKeysPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsSubjectEntityId() throws Exception {
        client.addServicePublicKey(null, publicKey, false, null);
        verify(transport).directoryV3ServiceKeysPost(any(ServiceKeysPostRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendServiceIdInRequest() throws Exception {
        UUID id = UUID.randomUUID();
        client.addServicePublicKey(id, publicKey, false, null);
        verify(transport).directoryV3ServiceKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(id, requestCaptor.getValue().getServiceId());
    }

    @Test
    public void sendsPublicKeyPEMInRequest() throws Exception {
        client.addServicePublicKey(null, publicKey, false, null);
        verify(transport).directoryV3ServiceKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(pem, requestCaptor.getValue().getPublicKey());
    }

    @Test
    public void sendsExpirationDateInRequest() throws Exception {
        Date date = new Date(0L);
        client.addServicePublicKey(null, publicKey, false, date);
        verify(transport).directoryV3ServiceKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals(date, requestCaptor.getValue().getExpires());
    }

    @Test
    public void sendsActiveFlagInRequest() throws Exception {
        client.addServicePublicKey(null, publicKey, true, null);
        verify(transport).directoryV3ServiceKeysPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertTrue(requestCaptor.getValue().isActive());
    }

    @Test
    public void returnsKeyIdAsResponse() throws Exception {
        when(response.getId()).thenReturn("Key ID");
        assertEquals("Key ID", client.addServicePublicKey(null, publicKey, true, null));
    }
}