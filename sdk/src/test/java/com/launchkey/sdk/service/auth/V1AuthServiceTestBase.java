package com.launchkey.sdk.service.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.launchkey.sdk.cache.PingResponseCache;
import com.launchkey.sdk.crypto.Crypto;
import com.launchkey.sdk.service.V1ServiceTestBase;
import com.launchkey.sdk.transport.v1.Transport;
import com.launchkey.sdk.transport.v1.domain.*;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        service = new V1AuthService(transport, crypto, pingResponseCache, rocketKey, secretKey);
    }

    @Override @After
    public void tearDown() throws Exception {
        service = null;
        super.tearDown();
    }
}