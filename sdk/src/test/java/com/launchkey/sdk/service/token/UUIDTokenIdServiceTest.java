package com.launchkey.sdk.service.token;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class UUIDTokenIdServiceTest {
    private TokenIdService service;

    @Before
    public void setUp() throws Exception {
        service = new UUIDTokenIdService();
    }

    @After
    public void tearDown() throws Exception {
        service = null;
    }

    @Test
    public void getTokenReturnsValue() throws Exception {
        assertNotNull(service.getTokenId());
    }

    @Test
    public void getTokenReturnsDifferentValuesOnSubsequentCalls() throws Exception {
        assertNotEquals(service.getTokenId(), service.getTokenId());
    }
}
