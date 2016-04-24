package com.launchkey.sdk.service.auth;

import com.launchkey.sdk.service.V1ServiceTestBase;
import org.junit.After;
import org.junit.Before;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 * <p/>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public abstract class V1AuthServiceTestBase extends V1ServiceTestBase {

    protected V1AuthService service;

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        service = new V1AuthService(transport, crypto, pingResponseCache, appKey, secretKey);
    }

    @Override @After
    public void tearDown() throws Exception {
        service = null;
        super.tearDown();
    }
}