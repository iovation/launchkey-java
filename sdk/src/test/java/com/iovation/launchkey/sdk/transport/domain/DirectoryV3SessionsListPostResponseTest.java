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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DirectoryV3SessionsListPostResponseTest {
    @Test
    public void getSessions() throws Exception {
        List<DirectoryV3SessionsListPostResponseSession> sessions =
                Arrays.asList(mock(DirectoryV3SessionsListPostResponseSession.class),
                        mock(DirectoryV3SessionsListPostResponseSession.class));
        assertEquals(sessions, new DirectoryV3SessionsListPostResponse(sessions).getSessions());
    }

    @Test
    public void equalsIsTrueForSameValues() throws Exception {
        List<DirectoryV3SessionsListPostResponseSession> sessions =
                Arrays.asList(mock(DirectoryV3SessionsListPostResponseSession.class),
                        mock(DirectoryV3SessionsListPostResponseSession.class));
        assertTrue(new DirectoryV3SessionsListPostResponse(sessions)
                .equals(new DirectoryV3SessionsListPostResponse(sessions)));
    }

    @Test
    public void equalsIsFalseForDifferentValues() throws Exception {
        assertFalse(new DirectoryV3SessionsListPostResponse(
                Collections.singletonList(mock(DirectoryV3SessionsListPostResponseSession.class)))
                .equals(new DirectoryV3SessionsListPostResponse(
                        Collections.singletonList(mock(DirectoryV3SessionsListPostResponseSession.class)))));
    }

    @Test
    public void hashCodeIsEqualForEqualValues() throws Exception {
        List<DirectoryV3SessionsListPostResponseSession> sessions =
                Arrays.asList(mock(DirectoryV3SessionsListPostResponseSession.class),
                        mock(DirectoryV3SessionsListPostResponseSession.class));
        assertEquals(new DirectoryV3SessionsListPostResponse(sessions).hashCode(),
                new DirectoryV3SessionsListPostResponse(sessions).hashCode());
    }

    @Test
    public void hashCodeIsNotEqualForDifferentValues() throws Exception {
        assertNotEquals(new DirectoryV3SessionsListPostResponse(
                        Collections.singletonList(mock(DirectoryV3SessionsListPostResponseSession.class))).hashCode(),
                new DirectoryV3SessionsListPostResponse(
                        Collections.singletonList(mock(DirectoryV3SessionsListPostResponseSession.class))).hashCode());
    }

    @Test
    public void teoStringHasClassName() throws Exception {
        assertThat(new DirectoryV3SessionsListPostResponse(new ArrayList<DirectoryV3SessionsListPostResponseSession>())
                .toString(), containsString(DirectoryV3SessionsListPostResponse.class.getSimpleName()));
    }
}
