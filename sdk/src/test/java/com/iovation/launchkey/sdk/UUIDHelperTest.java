package com.iovation.launchkey.sdk; /**
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.UUID;

import static org.junit.Assert.*;

public class UUIDHelperTest {

    @Test(expected = IllegalArgumentException.class)
    public void fromStringThrowsIllegalArgumentWhenUuidIsNull() throws Exception {
        UUIDHelper.fromString(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromStringThrowsIllegalArgumentWhenUuidIsInvalid() throws Exception {
        UUIDHelper.fromString("a-b-c-d", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromStringThrowsIllegalArgumentWhenUuidIsWrongVersion() throws Exception {
        UUIDHelper.fromString(UUID.randomUUID().toString(), 1);
    }
}