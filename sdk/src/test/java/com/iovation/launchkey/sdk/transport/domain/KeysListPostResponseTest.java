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
package com.iovation.launchkey.sdk.transport.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class KeysListPostResponseTest {
    private static final KeysListPostResponsePublicKey a =
            new KeysListPostResponsePublicKey("a", null, null, null, false);
    private static final List<KeysListPostResponsePublicKey> as = Arrays.asList(a, a);
    private static final KeysListPostResponsePublicKey b =
            new KeysListPostResponsePublicKey("b", null, null, null, false);
    private static final List<KeysListPostResponsePublicKey> bs = Arrays.asList(b, b);
    @Test
    public void getPublicKeys() throws Exception {
        List<KeysListPostResponsePublicKey> expected = new ArrayList<>();
        assertEquals(expected, new KeysListPostResponse(expected).getPublicKeys());
    }

    @Test
    public void equalsIsTrueForSameValue() throws Exception {
        assertTrue(new KeysListPostResponse(as).equals(new KeysListPostResponse(as)));
    }

    @Test
    public void equalsIsFalseForDifferentValues() throws Exception {
        assertFalse(new KeysListPostResponse(as).equals(new KeysListPostResponse(bs)));
    }

    @Test
    public void toStringContainsClass() throws Exception {
        assertThat(new KeysListPostResponse(as).toString(),
                containsString(KeysListPostResponse.class.getSimpleName()));
    }
}