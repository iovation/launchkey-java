package com.launchkey.sdk;

import com.launchkey.sdk.service.application.auth.AuthService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Provider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class BasicAppClientTest {

    private AuthService auth;
    private AppClient appClient;

    @Before
    public void setUp() throws Exception {
        auth = mock(AuthService.class);
        appClient = new BasicAppClient(auth);
    }

    @After
    public void tearDown() throws Exception {
        appClient = null;
        auth = null;
    }

    @Test
    public void testAuth() throws Exception {
        assertSame(auth, appClient.auth());
    }
}
