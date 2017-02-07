package com.launchkey.sdk;

import com.launchkey.sdk.cache.Cache;
import org.apache.http.client.HttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.InstanceOf;

import java.security.Provider;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

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
public class ClientFactoryBuilderTest {
    private ClientFactoryBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new ClientFactoryBuilder();
    }

    @After
    public void tearDown() throws Exception {
        builder = null;
    }

    @Test
    public void buildReturnsClientFactory() throws Exception {
        assertThat(builder.build(), new InstanceOf(ClientFactory.class));
    }

    @Test
    public void setRequestExpireSecondsReturnsBuilder() throws Exception {
        assertSame(builder, builder.setRequestExpireSeconds(10));
    }

    @Test
    public void setJCEProviderReturnsBuilder() throws Exception {
        assertSame(builder, builder.setJCEProvider(mock(Provider.class)));
    }

    @Test
    public void setHttpClientReturnsBuilder() throws Exception {
        assertSame(builder, builder.setHttpClient(mock(HttpClient.class)));
    }

    @Test
    public void setHttpClientConnectionTTLSecsReturnsBuilder() throws Exception {
        assertSame(builder, builder.setHttpClientConnectionTTLSecs(10));
    }

    @Test
    public void setHttpMaxClientsReturnsBuilder() throws Exception {
        assertSame(builder, builder.setHttpMaxClients(10));
    }

    @Test
    public void setCurrentPublicKeyTTLReturnsBuilder() throws Exception {
        assertSame(builder, builder.setCurrentPublicKeyTTL(0));
    }

    @Test
    public void setOffsetTTLReturnsBuilder() throws Exception {
        assertSame(builder, builder.setOffsetTTL(10));
    }

    @Test
    public void setAPIBaseURLReturnsBuilder() throws Exception {
        assertSame(builder, builder.setAPIBaseURL(null));
    }

    @Test
    public void setAPIdentifierReturnsBuilder() throws Exception {
        assertSame(builder, builder.setAPIdentifier(null));
    }

    @Test
    public void setKeyCacheReturnsBuilder() throws Exception {
        assertSame(builder, builder.setKeyCache(mock(Cache.class)));
    }
}
