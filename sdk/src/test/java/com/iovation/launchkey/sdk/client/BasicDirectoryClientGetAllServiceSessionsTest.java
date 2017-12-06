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

import com.iovation.launchkey.sdk.domain.directory.Session;
import com.iovation.launchkey.sdk.transport.Transport;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3SessionsListPostRequest;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3SessionsListPostResponse;
import com.iovation.launchkey.sdk.transport.domain.DirectoryV3SessionsListPostResponseSession;
import com.iovation.launchkey.sdk.transport.domain.EntityIdentifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicDirectoryClientGetAllServiceSessionsTest {
    private BasicDirectoryClient client;
    private final UUID directoryId = UUID.fromString("f6ad3fc7-ae11-11e7-9a1c-0469f8dc10a5");

    @Mock
    private Transport transport;

    @Mock
    private DirectoryV3SessionsListPostResponse response;

    @Captor
    private ArgumentCaptor<DirectoryV3SessionsListPostRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<EntityIdentifier> entityCaptor;

    @Before
    public void setUp() throws Exception {
        client = new BasicDirectoryClient(directoryId, transport);
        when(transport.directoryV3SessionsListPost(any(DirectoryV3SessionsListPostRequest.class),
                any(EntityIdentifier.class))).thenReturn(response);
        when(response.getSessions()).thenReturn(new ArrayList<DirectoryV3SessionsListPostResponseSession>());
    }

    @Test
    public void sendsDirectoryEntity() throws Exception {
        client.getAllServiceSessions(null);
        verify(transport)
                .directoryV3SessionsListPost(any(DirectoryV3SessionsListPostRequest.class), entityCaptor.capture());
        assertEquals(EntityIdentifier.EntityType.DIRECTORY, entityCaptor.getValue().getType());
    }

    @Test
    public void sendsDirectoryId() throws Exception {
        client.getAllServiceSessions(null);
        verify(transport)
                .directoryV3SessionsListPost(any(DirectoryV3SessionsListPostRequest.class), entityCaptor.capture());
        assertEquals(directoryId, entityCaptor.getValue().getId());
    }

    @Test
    public void sendsUserId() throws Exception {
        client.getAllServiceSessions("Expected User Id");
        verify(transport).directoryV3SessionsListPost(requestCaptor.capture(), any(EntityIdentifier.class));
        assertEquals("Expected User Id", requestCaptor.getValue().getIdentifier());
    }

    @Test
    public void returnsAllSessionsAsExpected() throws Exception {
        List<Session> expected = Arrays.asList(
                new Session(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5"), "Name 1", URI.create("http://foo"),
                        UUID.fromString("7e45bb07-b467-11e7-9c84-0469f8dc10a5"), new Date(123456)),
                new Session(UUID.fromString("7ecd274f-b467-11e7-8936-0469f8dc10a5"), "Name 2", URI.create("http://bar"),
                        UUID.fromString("9d151763-b501-11e7-bc68-0469f8dc10a5"), new Date(654321))
        );
        when(response.getSessions()).thenReturn(Arrays.asList(
                new DirectoryV3SessionsListPostResponseSession(UUID.fromString("6a033e54-b460-11e7-a723-0469f8dc10a5"),
                        "Name 1", URI.create("http://foo"), UUID.fromString("7e45bb07-b467-11e7-9c84-0469f8dc10a5"),
                        new Date(123456)),
                new DirectoryV3SessionsListPostResponseSession(UUID.fromString("7ecd274f-b467-11e7-8936-0469f8dc10a5"),
                        "Name 2", URI.create("http://bar"), UUID.fromString("9d151763-b501-11e7-bc68-0469f8dc10a5"),
                        new Date(654321))
        ));
        assertEquals(expected, client.getAllServiceSessions(null));
    }
}